package com.example.shiftlabjavatask.repository.entity;

import com.example.shiftlabjavatask.repository.entity.enums.ProductType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

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

    @Column
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
}
