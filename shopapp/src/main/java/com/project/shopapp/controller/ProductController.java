package com.project.shopapp.controller;

import com.project.shopapp.entity.Product;
import com.project.shopapp.service.ProductService;
import com.project.shopapp.ultil.anotation.ApiMessage;
import com.project.shopapp.ultil.error.InvalidException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    @ApiMessage("Get All Products")
    public ResponseEntity<List<Product>> getAllProducts(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        List<Product> product = this.productService.handleGetAllProducts();
        return ResponseEntity.ok().body(product);
    }

    @GetMapping("/{id}")
    @ApiMessage("Get Product With Id")
    public ResponseEntity<Optional<Product>> getProductById(@RequestParam("page") int page, @RequestParam("limit") int limit, @PathVariable long id) throws InvalidException {
        boolean check = this.productService.handleCheckProductWithId(id);
        if (!check) {
            throw new InvalidException("Id not exist");
        }
        Optional<Product> product = this.productService.handleGetProductWithId(id);
        return ResponseEntity.ok().body(product);
    }

    private String storeFile(MultipartFile file) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        //Thêm UUID vào trước tên file để đảm bảo tên file là duy nhất
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        //Đường dẫn đến thư mục mà bạn muốn lưu file
        Path updateDir = Paths.get("uploads");
        //Kiểm tra và tạo thư mục nếu nó không tồn tại
        if (!Files.exists(updateDir)) {
            Files.createDirectories(updateDir);
        }
        //Đường dẫn đầy đủ đến file
        Path destination = Paths.get(updateDir.toString(), uniqueFilename);
        //Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiMessage("Create Product")
    public ResponseEntity<?> createProduct(@Valid @ModelAttribute Product product, @ModelAttribute MultipartFile file) throws IOException {
        if (file != null) {
            if (file.getSize() > 10 * 1024 * 1024) {
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File is too large! Maximum size is 10Mb");
            }
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be an image");
            }
            String filename = storeFile(file);
            product.setThumbnail(filename);
        }
        product.setCreatedAt(Instant.now());
        product.setUpdatedAt(Instant.now());
        Product newProduct = this.productService.handleCreateProduct(product);
        return ResponseEntity.ok().body(newProduct);
    }

    @PutMapping(value = "/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiMessage("Update Product")
    public ResponseEntity<?> updateProduct(@Valid @ModelAttribute Product product ,@ModelAttribute MultipartFile file) throws IOException {
        if (file != null) {
            if (file.getSize() > 10 * 1024 * 1024) {
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File is too large! Maximum size is 10Mb");
            }
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be an image");
            }
            String filename = storeFile(file);
            product.setThumbnail(filename);
        }
        product.setCreatedAt(Instant.now());
        product.setUpdatedAt(Instant.now());
        product.setCategoryId(product.getCategoryId());
        Product updateProduct = this.productService.handleUpdateProduct(product);
        return ResponseEntity.ok().body(updateProduct);
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete Product")
    public ResponseEntity<String> deleteProduct(@PathVariable long id) {
        this.productService.handleDeleteProduct(id);
        return ResponseEntity.ok().body("Delete Successfully");
    }
}
