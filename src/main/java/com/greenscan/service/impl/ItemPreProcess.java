package com.greenscan.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.springframework.stereotype.Service;

import com.greenscan.dto.request.AddItem;
import com.greenscan.dto.request.AddItemRequest;
import com.greenscan.entity.CartItem;
import com.greenscan.entity.RecyclingData;
import com.greenscan.enums.MaterialType;
import com.greenscan.exception.custom.CompressionError;
import com.greenscan.exception.custom.FileUploadException;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class ItemPreProcess {
   private final  SupabaseStorageService supabaseStorageService;
   private final RecyclingDetectionService recyclingDetectionService;


    private  AddItem uploadItemImgToSupabase(Long cartId , File img ){
        try{
        String url =  supabaseStorageService.uploadFile(img, cartId+"img_"+System.currentTimeMillis());
        AddItem item = new AddItem(cartId, img ,url);
        return item;

        }catch(IOException e ){
            throw new FileUploadException ("Fail to upload Item img on cloud ");
        }
    }
    public File compressImageToFile(File sourceFile) {
       try{ 
        final float finalQuality = 0.6f; // Fixed to 60% quality (40% compression)
        
        // 1. Read the image into a BufferedImage object
        BufferedImage image = ImageIO.read(sourceFile);
        if (image == null) {
            throw new IOException("Could not read image data. Ensure the file exists and is a valid image format (e.g., JPEG, PNG).");
        }

        // 2. Define the output file (temporary)
        File compressedFile = File.createTempFile("compressed-gemini-", ".jpg");
        compressedFile.deleteOnExit(); 

        // 3. Find and configure the ImageWriter for JPEG format
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpeg");
        if (!writers.hasNext()) {
            throw new IllegalStateException("No built-in JPEG ImageWriter found.");
        }
        ImageWriter writer = writers.next();

        try (ImageOutputStream ios = ImageIO.createImageOutputStream(compressedFile)) {
            writer.setOutput(ios);

            // 4. Set compression parameters
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            
            // Apply the fixed 60% quality factor
            param.setCompressionQuality(finalQuality); 

            // 5. Write the compressed image data
            writer.write(null, new IIOImage(image, null, null), param);
            
        } catch (IOException e) {
            throw new CompressionError("unable to compress the img ");
        } finally {
            // 6. Clean up the writer resource
            writer.dispose(); 
        }
        

        return compressedFile;
    }catch(Exception e ){
        throw new CompressionError("unable to compress the img error occured  ");
    }
  
    }

    public AddItemRequest  getAiScannedItem(Long cartId, Long userId, File img){
        File cImg = compressImageToFile(img);
        AddItem item =uploadItemImgToSupabase(cartId, cImg);
        try{
            RecyclingData data  = recyclingDetectionService.analyzeRecyclingItem(cImg);
            
            AddItemRequest addItemRequest = new AddItemRequest();
            addItemRequest.setCartId(cartId);
            addItemRequest.setEstimatedWeight(data.estimatedWeight()/1000);
            addItemRequest.setImageUrl(item.getImgPath());
            addItemRequest.setItemName(data.itemName());
            addItemRequest.setMaterialType(MaterialType.valueOf(data.materialType()));
            addItemRequest.setEstimatedGreenCoin( MaterialType.valueOf(data.materialType()).getCoinsPerKg() * data.estimatedWeight()/1000 );
            addItemRequest.setAiDetectionData(data.aiDetectionData());
            addItemRequest.setAiConfidenceScore(data.aiConfidenceScore());

            return addItemRequest;
        }catch(Exception e ){
           throw new  UnknownError("Unknowa erorr Error found in Ai item deetection data Extraction  "+e.getLocalizedMessage()) ;
        }


    }
    public boolean removeItemImg (String url){
        return supabaseStorageService.deleteFile(url);
    }

}
