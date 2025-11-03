package com.ecom_application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecom_application.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	List<Product> findByCategory_Id(int categoryId);
	List<Product> findBySellerId(int sellerId);
	
	
	@Query("SELECT p FROM Product p WHERE " +
		       "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		       "LOWER(p.Description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		       "LOWER(p.category.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<Product> searchProducts(@Param("keyword") String keyword);

}
