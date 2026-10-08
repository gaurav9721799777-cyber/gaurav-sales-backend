package com.gaurav.sales.serviceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gaurav.sales.service.FileStorageService;

@Service
public class FileStorageServiceImpl
        implements FileStorageService {

    private final Path uploadDirectory =
            Paths.get("uploads/products");


    // =====================================================
    // SAVE PRODUCT IMAGE
    // =====================================================

    @Override
    public String saveProductImage(
            MultipartFile file) {

        if (file == null ||
                file.isEmpty()) {

            throw new IllegalArgumentException(
                    "Product image is required"
            );
        }


        // ---------------------------------------------
        // Validate content type
        // ---------------------------------------------

        String contentType =
                file.getContentType();

        if (contentType == null ||
                !contentType.startsWith("image/")) {

            throw new IllegalArgumentException(
                    "Only image files are allowed"
            );
        }


        // ---------------------------------------------
        // Validate size
        // 5 MB
        // ---------------------------------------------

        long maxSize =
                5L * 1024 * 1024;

        if (file.getSize() > maxSize) {

            throw new IllegalArgumentException(
                    "Image size must not exceed 5 MB"
            );
        }


        try {

            // Create directory
            Files.createDirectories(
                    uploadDirectory
            );


            // Get extension
            String originalName =
                    file.getOriginalFilename();

            String extension = "";

            if (originalName != null &&
                    originalName.contains(".")) {

                extension =
                        originalName.substring(
                                originalName.lastIndexOf(".")
                        );
            }


            // Generate unique name
            String fileName =
                    UUID.randomUUID()
                            + extension;


            // Final path
            Path filePath =
                    uploadDirectory.resolve(
                            fileName
                    );


            // Save file
            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );


            // Save this in database
            return "/uploads/products/"
                    + fileName;


        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to save product image",
                    e
            );
        }
    }


    // =====================================================
    // DELETE IMAGE
    // =====================================================

    @Override
    public void deleteProductImage(
            String imageUrl) {

        if (imageUrl == null ||
                imageUrl.isBlank()) {
            return;
        }

        try {

            String fileName =
                    Paths.get(imageUrl)
                            .getFileName()
                            .toString();

            Path filePath =
                    uploadDirectory.resolve(
                            fileName
                    );

            Files.deleteIfExists(
                    filePath
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to delete product image",
                    e
            );
        }
    }
}