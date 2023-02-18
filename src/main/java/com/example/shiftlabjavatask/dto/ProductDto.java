package com.example.shiftlabjavatask.dto;

import com.example.shiftlabjavatask.repository.entity.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private String serialNumber;
    private String producer;
    private double price;
    private long quantity;
    private String type;
    private String description;
}
