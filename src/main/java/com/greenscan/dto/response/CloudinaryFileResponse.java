package com.greenscan.dto.response;

import lombok.Data;

@Data
public class CloudinaryFileResponse {
    private Long id;
    private String fileName;
    private String fileType;
    private String url;
    private Long version;
    private Long size;
    private Long uploadedAt;
}