package com.project.shopapp.service;

import com.project.shopapp.domain.dtos.ProductDTO;
import com.project.shopapp.domain.dtos.ProductImageDTO;
import com.project.shopapp.domain.entity.Category;
import com.project.shopapp.domain.entity.Product;
import com.project.shopapp.domain.entity.ProductImage;
import com.project.shopapp.domain.response.ResultPaginationDTO;
import com.project.shopapp.repository.CategoryRepository;
import com.project.shopapp.repository.ProductImageRepository;
import com.project.shopapp.repository.ProductRepository;
import com.project.shopapp.ultil.error.InvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    public ResultPaginationDTO handleGetAllProducts(Pageable pageable) {
        Page<Product> page = this.productRepository.findAll(pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(page.getNumber() + 1);
        meta.setPageSize(page.getSize());

        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());

        result.setMeta(meta);
        result.setResult(page.getContent());
        return result;
    }

    public boolean handleCheckProductWithId(long id) {
        return this.productRepository.existsById(id);
    }

    public Optional<Product> handleGetProductWithId(long id) {
        return this.productRepository.findById(id);
    }

    public Product handleCreateProduct(ProductDTO productDTO) throws InvalidException {
        Category category = this.categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(() -> new InvalidException("Cannot find category with id: " + productDTO.getCategoryId()));
        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .category(category)
                .createdAt(Instant.now())
                .updatedAt(Instant.now()).build();
        return this.productRepository.save(newProduct);
    }

    public Product handleUpdateProduct(ProductDTO productDTO, long id) throws InvalidException {
        Product product = this.productRepository.findById(id).orElseThrow(() -> new InvalidException("Cannot find product with id: " + id));

        if (product != null) {
            Category category = this.categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(() -> new InvalidException("Cannot find category with id: " + productDTO.getCategoryId()));
            product.setName(productDTO.getName());
            product.setCategory(category);
            product.setPrice(productDTO.getPrice());
            product.setDescription(productDTO.getDescription());
            product.setThumbnail(productDTO.getThumbnail());
            product.setUpdatedAt(Instant.now());
            return this.productRepository.save(product);
        }
        return null;
    }

    public void handleDeleteProduct(long id) {
        this.productRepository.deleteById(id);
    }

    public boolean handleCheckProductName(String name) {
        return this.productRepository.existsByName(name);
    }

    public ProductImage handleCreateProductImage(Long productId, ProductImageDTO productImageDTO) throws InvalidException {
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new InvalidException("Cannot find product with id: " + productImageDTO.getProductId()));
        ProductImage newProductImage = ProductImage.builder().product(product).imageUrl(productImageDTO.getImageUrl()).build();
        int size = productImageRepository.findByProductId(productId).size();
        if (size >= 5) {
            throw new InvalidException("Number of images must be less than 5");
        }
        return this.productImageRepository.save(newProductImage);
    }
}
