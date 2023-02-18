package com.example.shiftlabjavatask.service;

import com.example.shiftlabjavatask.dto.ProductDto;
import com.example.shiftlabjavatask.dto.ProductIdDto;

public interface ProductService {
    ProductIdDto createProduct(ProductDto productDto);
}
