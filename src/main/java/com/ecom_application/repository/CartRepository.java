package com.ecom_application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ecom_application.entity.Cart;
import com.ecom_application.entity.User;

import jakarta.transaction.Transactional;

public interface CartRepository extends JpaRepository<Cart, Integer> {

	public Cart findByProductIdAndUserId(Integer productId, Integer userId);
	
	public List<Cart> findByUserId(Integer id);
	
	public Cart findByProductId(Integer id);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM Cart c WHERE c.product.id = :id")
	public void deleteByProductId(Integer id);
	
	@Transactional
	@Modifying
	public void deleteByUser(User user);
	
}
