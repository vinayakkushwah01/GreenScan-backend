package com.greenscan.repository;

import com.greenscan.entity.CloudinaryFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CloudinaryFileRepository extends JpaRepository<CloudinaryFile, Long> {

    // Optional: find by publicId if needed
    CloudinaryFile findByPublicId(String publicId);
}
