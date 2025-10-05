package com.greenscan.dto.request;


import lombok.Data;

@Data
public class CloudinaryFileRequest {
    private String fileName;
    private String fileType;
    private String publicId;
    private String url;
    private Long version;
    private Long size;
}