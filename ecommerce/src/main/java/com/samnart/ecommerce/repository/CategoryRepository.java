package com.samnart.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samnart.ecommerce.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    
}