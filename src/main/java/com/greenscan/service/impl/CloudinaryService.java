// package com.greenscan.service.impl;

// import com.cloudinary.AuthToken;
// import com.cloudinary.Cloudinary;
// import com.cloudinary.Transformation;
// import com.cloudinary.utils.ObjectUtils;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.web.multipart.MultipartFile;

// import com.greenscan.dto.response.CloudinaryResponse;

// import java.io.IOException; // Standard Java I/O IOException
// import java.nio.charset.StandardCharsets;
// import java.security.MessageDigest;
// import java.security.NoSuchAlgorithmException;
// import java.util.HashMap;
// import java.util.Map;
// import java.util.TreeMap;

// @Service
// public class CloudinaryService {

//     private static final String KYC_UPLOAD_FOLDER = "GRENNSCANKYC";
//     //   private static final long EXPIRY_TIME_SECONDS = 10 * 60; // 10 minutes
    
//     // Using final and constructor injection (best practice)
//     private final Cloudinary cloudinary;

//     @Autowired
//     public CloudinaryService(Cloudinary cloudinary) {
//         this.cloudinary = cloudinary;
//     }
    
//     /**
//      * Uploads a MultipartFile to Cloudinary with secure settings, 
//      * using a byte array buffer for maximum stability.
//      */
//     public CloudinaryResponse uploadToCloudnary (MultipartFile file, String fileName) throws IOException {
        
//         if (file.isEmpty()) {
//             throw new IllegalArgumentException("Cannot upload empty file.");
//         }
        
//         try {
//             // CRITICAL FIX: Convert the file to a byte array for maximum compatibility 
//             // with the Cloudinary SDK's upload method. This prevents the "Unrecognized file parameter" error.
//             byte[] fileBytes = file.getBytes();
            
//             // 1. Define upload parameters:
//             Map<String, Object> uploadOptions = ObjectUtils.asMap(
//                 "public_id", fileName, 
//                 "folder", KYC_UPLOAD_FOLDER,
//                 "type", "authenticated", 
//                 "resource_type", "auto",
//                 "overwrite", true 
//             );

//             // 2. Upload the file, passing the byte array directly.
//             // This is safer than passing the raw InputStream in some environments.
//             Map uploadResult = cloudinary.uploader().upload(
//                 fileBytes, 
//                 uploadOptions
//             );

//             // 3. Extract the required unique identity data and version
//             String publicId = (String) uploadResult.get("public_id");
//             Long version = Long.parseLong(uploadResult.get("version").toString());

//             // 4. Return the DTO
//             return new CloudinaryResponse(publicId, version);

//         } catch (java.io.IOException e) {
//             // Handles physical I/O errors during conversion or streaming
//             System.err.println("File stream I/O failed: " + e.getMessage());
//             // Re-throw as RuntimeException since it's an unrecoverable server issue here
//             throw new RuntimeException("Failed to process file stream: " + e.getMessage(), e); 
//         } catch (RuntimeException e) {
//              // Catches unchecked runtime exceptions, often wrapping API failures from Cloudinary
//              System.err.println("Cloudinary API interaction failed: " + e.getMessage());
//              throw new RuntimeException("Error during Cloudinary API call or processing: " + e.getMessage(), e);
//         } catch (Exception e) {
//              // Catch-all for other unexpected issues
//              System.err.println("Unexpected upload error: " + e.getMessage());
//              throw new RuntimeException("Unexpected error during file upload: " + e.getMessage(), e);
//         }
//     }
    
//     public String generateSecuredUrl(String publicId, Long version) {
//        // Expiration time = now + 15 minutes
//         long expiresAt = System.currentTimeMillis() / 1000L + 900;

//         // Build sorted parameters for signature
//         Map<String, Object> params = new TreeMap<>();
//         params.put("public_id", publicId);
//         params.put("expires_at", expiresAt);

//         // Create the string to sign: key1=value1&key2=value2 + api_secret
//         StringBuilder toSign = new StringBuilder();
//         params.forEach((k, v) -> toSign.append(k).append("=").append(v).append("&"));
//         toSign.deleteCharAt(toSign.length() - 1); // remove trailing &
//         toSign.append(cloudinary.config.apiSecret);

//         // Generate SHA-1 signature
//         String signature = sha1Hex(toSign.toString());

//         // Construct the URL
//         return String.format(
//                 "https://res.cloudinary.com/%s/image/upload/%s?expires_at=%d&api_key=%s&signature=%s",
//                 cloudinary.config.cloudName,
//                 publicId,
//                 expiresAt,
//                 cloudinary.config.apiKey,
//                 signature
//         );
//     }

//     private String sha1Hex(String data) {
//         try {
//             MessageDigest md = MessageDigest.getInstance("SHA-1");
//             byte[] digest = md.digest(data.getBytes(StandardCharsets.UTF_8));
//             StringBuilder sb = new StringBuilder();
//             for (byte b : digest) {
//                 sb.append(String.format("%02x", b));
//             }
//             return sb.toString();
//         } catch (NoSuchAlgorithmException e) {
//             throw new RuntimeException("SHA-1 not supported", e);
//         }
//     }
        
    
// }


package com.greenscan.service.impl;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.greenscan.entity.CloudinaryFile;
import com.greenscan.entity.VendorProfile;
import com.greenscan.repository.CloudinaryFileRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

@Service
public class CloudinaryService {


    private final Cloudinary cloudinary;
    private CloudinaryFileRepository cloudinaryFileRepository;
    @Autowired
    private FileEncryptor fileEncryptor;

    @Autowired
    public CloudinaryService(Cloudinary cloudinary, CloudinaryFileRepository cloudinaryFileRepository) {
        this.cloudinary = cloudinary;
        this.cloudinaryFileRepository = cloudinaryFileRepository;
    }

 
    public CloudinaryFile uploadFile(MultipartFile file, String fileName) throws Exception {
        byte[] encryptedBytes = fileEncryptor.encrypt(file.getBytes());
           // encryptedBytes = FileEncryptor.encrypt(file.getBytes());
      
        Map uploadResult = cloudinary.uploader().upload(encryptedBytes,
        ObjectUtils.asMap(
                "public_id", fileName,        // custom public ID
                "resource_type", "auto",      // supports all file types
                "use_filename", true,         // preserve original filename
                "unique_filename", false,     // avoid Cloudinary renaming
                "overwrite", true,            // overwrite if same public_id exists
                "invalidate", true ,
                "folder", "kyc_docs" ,
                "access_mode", "authenticated"           // invalidate CDN cache
        ));

        CloudinaryFile cloudinaryFile = new CloudinaryFile();
        cloudinaryFile.setFileName(file.getOriginalFilename());
        cloudinaryFile.setFileType(file.getContentType());
        cloudinaryFile.setPublicId((String) uploadResult.get("public_id"));
        cloudinaryFile.setUrl((String) uploadResult.get("secure_url"));
        cloudinaryFile.setVersion(Long.parseLong(uploadResult.get("version").toString()));
        cloudinaryFile.setSize(file.getSize());
        cloudinaryFile.setUploadedAt(System.currentTimeMillis());
       return cloudinaryFileRepository.save(cloudinaryFile);
        //return cloudinaryFile;
    }

    public byte[] getFile(CloudinaryFile cloudinaryFile) throws Exception {
         URL fileUrl = new URL(cloudinaryFile.getUrl());
        try (var in = fileUrl.openStream()) {
            byte[] encryptedBytes = in.readAllBytes();
            return fileEncryptor.decrypt(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving or decrypting file", e);
        }

    }
}

