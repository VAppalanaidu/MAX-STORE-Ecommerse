package com.ecom_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom_application.entity.Seller;

public interface SellerRepository extends JpaRepository<Seller, Integer> {

	public Seller findByEmail(String email);
	
}
