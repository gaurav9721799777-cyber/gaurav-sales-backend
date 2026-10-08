package com.gaurav.sales.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String saveProductImage(
            MultipartFile file
    );

    void deleteProductImage(
            String imageUrl
    );
}
