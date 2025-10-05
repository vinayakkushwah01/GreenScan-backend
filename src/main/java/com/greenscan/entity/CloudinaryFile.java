package com.greenscan.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cloudinary_files")
public class CloudinaryFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Original file name
    @Column(nullable = false)
    private String fileName;

    // File format (jpg, png, pdf, etc.)
    @Column(nullable = false)
    private String fileType;

    // Cloudinary public ID
    @Column(nullable = false, unique = true)
    private String publicId;

    // Cloudinary URL (secured or normal)
    @Column(nullable = false)
    private String url;

    // Cloudinary version
    @Column(nullable = false)
    private Long version;

    // Optional: size in bytes
    private Long size;

    // Optional: uploaded timestamp
    @Column(nullable = false)
    private Long uploadedAt;
}
