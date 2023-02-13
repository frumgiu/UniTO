package com.example.balonmicroservice.repository;

import com.example.balonmicroservice.model.SaleAds;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SaleAdsRepository extends JpaRepository<SaleAds, Long> {
    List<SaleAds> findAll();

    List<SaleAds> findByActive(boolean active);

    List<SaleAds> findByOwnerEmail(String ownerEmail);

    Optional<SaleAds> findById(Long id);

    void deleteById(Long id);
}