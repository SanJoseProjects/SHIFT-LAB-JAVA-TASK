package com.example.shiftlabjavatask.controller;

import com.example.shiftlabjavatask.dto.ProductDto;
import com.example.shiftlabjavatask.dto.ProductIdDto;
import com.example.shiftlabjavatask.repository.entity.Product;
import com.example.shiftlabjavatask.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "shop", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(value = "products", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductIdDto> createProduct(@RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productService.createProduct(productDto));
    }

    @PutMapping(value = "products")
    public ResponseEntity<Void> updateProduct(@RequestBody ProductDto productDto)
    {
        productService.updateProduct(productDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "products")
    public ResponseEntity<Void> deleteProduct(@RequestBody ProductIdDto productIdDto)
    {
        productService.deleteProduct(productIdDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "products")
    public ResponseEntity<List<ProductDto>> findAllProductsByType(@RequestParam String type) {
        return ResponseEntity.ok(productService.findAllByType(type));
    }

    @GetMapping(value = "product")
    public ResponseEntity<ProductDto> findProductById(@RequestParam Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }
}
