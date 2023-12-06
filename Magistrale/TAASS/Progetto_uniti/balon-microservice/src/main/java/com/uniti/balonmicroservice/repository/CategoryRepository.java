package com.uniti.balonmicroservice.repository;

import com.uniti.balonmicroservice.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAll();

    Optional<Category> findById(Long id);

    Optional<Category> findByName(String name);
}
