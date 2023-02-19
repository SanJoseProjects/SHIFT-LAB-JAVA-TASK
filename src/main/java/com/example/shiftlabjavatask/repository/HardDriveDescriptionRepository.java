package com.example.shiftlabjavatask.repository;

import com.example.shiftlabjavatask.repository.entity.HardDriveDescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HardDriveDescriptionRepository extends JpaRepository<HardDriveDescription, Long> {
    HardDriveDescription findByProductId(Long productId);
}
