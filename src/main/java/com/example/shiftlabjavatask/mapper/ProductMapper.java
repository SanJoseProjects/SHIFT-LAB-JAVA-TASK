package com.example.shiftlabjavatask.mapper;

import com.example.shiftlabjavatask.dto.ProductDto;
import com.example.shiftlabjavatask.repository.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "description", target = "description")
    ProductDto productToProductDto(Product product, String description);
}
