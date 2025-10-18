package com.greenscan.service.impl;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SupabaseStorageService {

    @Value("${supabase.project-ref}")
    private String projectRef;

    @Value("${supabase.bucket-name}")
    private String bucketName;

    @Value("${supabase.api-key}")
    private String apiKey;

    public String uploadFile(File file, String fileName) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + file.getAbsolutePath());
        }

        String contentType = Files.probeContentType(file.toPath());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        String uploadUrl = "https://" + projectRef + ".supabase.co/storage/v1/object/" + bucketName + "/" + fileName;
        URL url = new URL(uploadUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + apiKey);
        conn.setRequestProperty("Content-Type", contentType);

        // Write file bytes to output stream
        try (OutputStream os = conn.getOutputStream(); FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.flush();
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            return "https://" + projectRef + ".supabase.co/storage/v1/object/public/" + bucketName + "/" + fileName;
        } else {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                throw new IOException("Upload failed: " + responseCode + " " + sb.toString());
            }
        }
    }

    // ✅ Fixed deleteFile method
    public boolean deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            System.out.println("File URL is null or empty.");
            return false;
        }

        try {
            // Extract the file path (relative path inside the bucket)
            String filePath = extractFilePath(fileUrl);

            // Construct DELETE endpoint
            String deleteUrl = "https://" + projectRef + ".supabase.co/storage/v1/object/" + bucketName + "/" + filePath;

            URL url = new URL(deleteUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setRequestProperty("apikey", apiKey);

            int responseCode = conn.getResponseCode();

            if (responseCode == 200 || responseCode == 204) {
                System.out.println("✅ File deleted successfully: " + filePath);
                return true;
            } else {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                    String line;
                    StringBuilder sb = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    System.err.println("❌ Failed to delete file: " + responseCode + " " + sb);
                }
                return false;
            }

        } catch (Exception e) {
            System.err.println("⚠️ Error deleting file: " + e.getMessage());
            return false;
        }
    }

    // ✅ Helper method to extract file path from URL
    private String extractFilePath(String fileUrl) {
        // Example:
        // fileUrl = https://xyz.supabase.co/storage/v1/object/public/avatars/user123.png
        // bucketName = avatars
        // → returns: user123.png
        String marker = bucketName + "/";
        int index = fileUrl.indexOf(marker);
        if (index != -1) {
            return fileUrl.substring(index + marker.length());
        }
        return fileUrl;
    }

}
