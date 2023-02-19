package com.example.shiftlabjavatask.repository.entity;

import com.example.shiftlabjavatask.repository.entity.enums.ProductType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(unique = true, nullable = false)
    private String serialNumber;

    @Column
    private String producer;

    @Column
    private double price;

    @Column
    private long quantity;

    @Column
    @Enumerated(EnumType.STRING)
    private ProductType type;

    public Product(Product product) {
        this.id = product.getId();
        this.serialNumber = product.getSerialNumber();
        this.producer = product.getProducer();
        this.price = product.getPrice();
        this.quantity = product.getQuantity();
        this.type = product.getType();
    }
}
