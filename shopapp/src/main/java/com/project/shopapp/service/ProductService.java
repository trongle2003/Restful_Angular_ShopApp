package com.project.shopapp.service;

import com.project.shopapp.entity.Product;
import com.project.shopapp.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> handleGetAllProducts() {
        return this.productRepository.findAll();
    }

    public boolean handleCheckProductWithId(long id) {
        return this.productRepository.existsById(id);
    }

    public Optional<Product> handleGetProductWithId(long id) {
        return this.productRepository.findById(id);
    }

    public Product handleCreateProduct(Product product) {
        return this.productRepository.save(product);
    }

    public Product handleUpdateProduct(Product product) {
        return this.productRepository.save(product);
    }

    public void handleDeleteProduct(long id) {
        this.productRepository.deleteById(id);
    }
}
