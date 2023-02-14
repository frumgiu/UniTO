package com.example.balonmicroservice.repository;

import com.example.balonmicroservice.model.Category;
import com.example.balonmicroservice.model.SaleAds;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAll();

    Optional<Category> findById(Long id);

    Optional<Category> findByName(String name);
}
