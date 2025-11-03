package com.ecom_application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom_application.entity.ProductOrder;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Integer> {
	
	public List<ProductOrder> findAllByUserId(Integer userId);
	
	public List<ProductOrder> findByProductSellerId(Integer productSellerId);
}
