package com.Ecom.Controllers;

import com.Ecom.Dtos.ProdResponse;
import com.Ecom.Dtos.ProductDto;
import com.Ecom.Entities.FileMetaData;
import com.Ecom.Entities.Product;
import com.Ecom.Services.FileStorageService;
import com.Ecom.Services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final FileStorageService fileStorageService;

    // Get all products
    @GetMapping
    public ProdResponse<Product> getProducts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "title") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir
    ) {
        return productService.getAll(page, size, sortBy, sortDir);
    }

    // Get single product
    @GetMapping("/{productId}")
    public Product getSingleProduct(@PathVariable Long productId) {
        return productService.get(productId);
    }

    // Create product
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto) {
        ProductDto savedEntity = productService.createProduct(productDto);
        return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);
    }

    // Update product
    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long productId,
            @RequestBody Product product
    ) {
        Product savedProduct = productService.udpateProduct(productId, product);
        return ResponseEntity.ok(savedProduct);
    }

    // Delete product
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.delete(productId);
        return ResponseEntity.noContent().build();
    }

    // Upload product image
    @PostMapping("/{productId}/image")
    public ResponseEntity<FileMetaData> uploadProductImage(
            @PathVariable Long productId,
            @RequestParam("productImage") MultipartFile file
    ) throws IOException {

        if (file.isEmpty()) {
            throw new BadRequestException("File is empty!");
        }

        String contentType = file.getContentType();

        if (contentType == null ||
                (!contentType.equalsIgnoreCase("image/png")
                        && !contentType.equalsIgnoreCase("image/jpeg"))) {
            throw new BadRequestException("Only PNG and JPEG images are allowed!");
        }

        Product product = productService.get(productId);

        FileMetaData fileMetaData = fileStorageService.uploadFile(file);
        product.setImage(fileMetaData);

        productService.udpateProduct(productId, product);

        return ResponseEntity.ok(fileMetaData);
    }

    // Serve product image
    @GetMapping("/{productId}/image")
    public ResponseEntity<Resource> serveFile(@PathVariable Long productId)
            throws MalformedURLException, BadRequestException {

        Product product = productService.get(productId);
        FileMetaData fileMetaData = product.getImage();

        if (fileMetaData == null) {
            throw new BadRequestException("Image not found for this product!");
        }

        Resource resource = fileStorageService.loadFile(fileMetaData);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileMetaData.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}