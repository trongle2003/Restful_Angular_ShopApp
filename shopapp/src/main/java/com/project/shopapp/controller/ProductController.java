package com.project.shopapp.controller;

import com.github.javafaker.Faker;
import com.project.shopapp.domain.dtos.ProductDTO;
import com.project.shopapp.domain.dtos.ProductImageDTO;
import com.project.shopapp.domain.entity.Product;
import com.project.shopapp.domain.entity.ProductImage;
import com.project.shopapp.domain.response.ResultPaginationDTO;
import com.project.shopapp.service.ProductImageService;
import com.project.shopapp.service.ProductService;
import com.project.shopapp.ultil.anotation.ApiMessage;
import com.project.shopapp.ultil.error.InvalidException;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;
    private final ProductImageService productImageService;

    public ProductController(ProductService productService, ProductImageService productImageService) {
        this.productService = productService;
        this.productImageService = productImageService;
    }

    @GetMapping("")
    @ApiMessage("Get All Products")
    public ResponseEntity<ResultPaginationDTO> getAllProducts(@RequestParam("page") Optional<String> page, @RequestParam("size") Optional<String> size) {
        String sCurrent = page.isPresent() ? page.get() : "";
        String sPageSize = size.isPresent() ? size.get() : "";

        int current = Integer.parseInt(sCurrent) - 1;
        int pageSize = Integer.parseInt(sPageSize);

        Pageable pageable = PageRequest.of(current, pageSize);

        return ResponseEntity.ok().body(this.productService.handleGetAllProducts(pageable));
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

    @PostMapping(value = "")
    @ApiMessage("Create Product")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDTO productDTO) throws InvalidException {
        Product newProduct = this.productService.handleCreateProduct(productDTO);
        return ResponseEntity.ok().body(newProduct);
    }

    @PostMapping(value = "/upload/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@PathVariable("id") long productId,
                                          @RequestParam("files") List<MultipartFile> files) throws IOException, InvalidException {
        Product product = this.productService.handleGetProductWithId(productId).orElseThrow(() -> new InvalidException("Product id invalid"));
        ArrayList<ProductImage> productImages = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (file.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File is too large! Maximum size is 10Mb");
                }
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be an image");
                }
                String filename = storeFile(file);
                ProductImage productImage = this.productService.handleCreateProductImage(product.getId(), ProductImageDTO.builder()
                        .imageUrl(filename)
                        .build());
                productImages.add(productImage);
            }
        }
        return ResponseEntity.ok().body(productImages);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiMessage("Update Product")
    public ResponseEntity<?> updateProduct(@Valid @ModelAttribute ProductDTO productDTO, @ModelAttribute MultipartFile file) throws IOException {
        if (file != null) {
            if (file.getSize() > 10 * 1024 * 1024) {
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File is too large! Maximum size is 10Mb");
            }
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be an image");
            }
            String filename = storeFile(file);
        }
        //Product updateProduct = this.productService.handleUpdateProduct(productDTO);
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete Product")
    public ResponseEntity<String> deleteProduct(@PathVariable long id) {
        this.productImageService.handleDeleteProductImagesByProductId(id);
        this.productService.handleDeleteProduct(id);
        return ResponseEntity.ok().body("Delete Successfully");
    }

    @PostMapping("/generateFakeProducts")
    public ResponseEntity<?> generateFakeProducts() {
        Faker faker = new Faker();
        int max = 10;
        int created = 0;

        for (int i = 0; i < max; i++) {
            String name = faker.commerce().productName();
            if (productService.handleCheckProductName(name)) continue;

            try {
                Product product = productService.handleCreateProduct(ProductDTO.builder()
                        .name(name)
                        .description(faker.lorem().sentence())
                        .price(Float.valueOf(faker.number().numberBetween(1000, 100_000_000)))
                        .thumbnail("https://picsum.photos/300/300?random=" + i)
                        .categoryId(Long.valueOf(faker.number().numberBetween(1, 3)))
                        .build());

                for (int j = 0; j < 5; j++) {
                    productImageService.handleCreateProductImage(ProductImage.builder()
                            .product(product)
                            .imageUrl("https://picsum.photos/300/300?random=" + faker.random().nextInt(1000))
                            .build());
                }
                created++;
            } catch (Exception ignored) {
            }
        }
        return ResponseEntity.ok().body("Generated " + created + " fake products.");
    }
}
