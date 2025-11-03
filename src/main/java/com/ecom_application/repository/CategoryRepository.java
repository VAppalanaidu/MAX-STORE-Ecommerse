package com.ecom_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom_application.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
