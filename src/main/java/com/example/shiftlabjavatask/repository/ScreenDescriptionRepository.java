package com.example.shiftlabjavatask.repository;

import com.example.shiftlabjavatask.repository.entity.ScreenDescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreenDescriptionRepository extends JpaRepository<ScreenDescription, Long> {
    ScreenDescription findByProductId(Long productId);
}
