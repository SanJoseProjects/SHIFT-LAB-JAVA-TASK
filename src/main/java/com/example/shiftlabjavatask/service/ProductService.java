package com.example.shiftlabjavatask.service;

import com.example.shiftlabjavatask.dto.ProductDto;
import com.example.shiftlabjavatask.dto.ProductIdDto;
import com.example.shiftlabjavatask.repository.entity.Product;

import java.util.List;

public interface ProductService {
    ProductIdDto createProduct(ProductDto productDto);

    void updateProduct(ProductDto productDto);

    void deleteProduct(ProductIdDto productIdDto);

    List<ProductDto> findAllByType(String type);

    ProductDto findById(Long id);
}
