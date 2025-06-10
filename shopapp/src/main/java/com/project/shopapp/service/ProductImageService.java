package com.project.shopapp.service;

import com.project.shopapp.domain.entity.ProductImage;
import com.project.shopapp.repository.ProductImageRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ProductImageService {
    private final ProductImageRepository productImageRepository;

    public ProductImageService(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }

    public ProductImage handleCreateProductImage(ProductImage productImage) {
        return this.productImageRepository.save(productImage);
    }

    @Transactional
    public void handleDeleteProductImagesByProductId(long productId) {
        productImageRepository.deleteByProductId(productId);
    }

}
