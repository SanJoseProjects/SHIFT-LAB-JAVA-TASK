package com.example.shiftlabjavatask.repository;

import com.example.shiftlabjavatask.repository.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findBySerialNumber(String serialNumber);
}
