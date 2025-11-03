package com.ecom_application.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.ecom_application.entity.Cart;
import com.ecom_application.entity.Product;
import com.ecom_application.entity.User;
import com.ecom_application.repository.CartRepository;

@Service
public class CartService {
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserService userService;
	
	public Cart saveCartItem(Integer productId, Integer userId) {
		
		User user = userService.findUserById(userId).get();
		Product product = productService.findByProductId(productId).get();
		
		Cart cartStatus = cartRepository.findByProductIdAndUserId(productId, userId);
		Cart cart = null;
		
		if(ObjectUtils.isEmpty(cartStatus)) {
			cart = new Cart();
			cart.setProduct(product);
			cart.setUser(user);
			cart.setQuantity(1);
			cart.setPrice(1* product.getPrice() - discountPrice(product.getPrice()));
		}
		else {
			cart = cartStatus;
			cart.setQuantity(cart.getQuantity() + 1);
			cart.setPrice(cart.getQuantity() * cart.getProduct().getPrice());
		}
		Cart saveCart = cartRepository.save(cart);
		return saveCart;
	}
	
	public void increaseQuantity(Integer pid) {
		Cart cart = cartRepository.findByProductId(pid);
		if(cart != null) {
			cart.setPrice((cart.getQuantity() + 1) * cart.getProduct().getPrice());
			cart.setQuantity(cart.getQuantity() + 1);
		}
		cartRepository.save(cart);
	}
	
	public void decreaseQuantity(Integer pid) {
		Cart cart = cartRepository.findByProductId(pid);
		if(cart != null && cart.getQuantity() > 1) {
			cart.setPrice((cart.getQuantity() - 1) * cart.getProduct().getPrice());
			cart.setQuantity(cart.getQuantity() - 1);
		}
		cartRepository.save(cart);
	}
	
	public List<Cart> findAllByUserId(Integer userId) {
		return cartRepository.findByUserId(userId);
	}
	
	public void deleteByPrductId(Integer id) {
		cartRepository.deleteByProductId(id);
	}
	
	
	public Float discountPrice(Float total) {
		Float discount = 0.0f;
		  if (total > 5000 && total < 10000) discount = total * 0.05f;
		  else if (total >= 10000 && total < 20000) discount = total * 0.10f;
		  else if (total >= 20000 && total < 50000) discount = total * 0.12f;
		  else if (total >= 50000 && total < 100000) discount = total * 0.15f;
		  else if (total >= 100000 && total < 200000) discount = total * 0.20f;
		  else if (total >= 200000 && total < 300000) discount = total * 0.25f;
		  else if (total >= 300000 && total < 400000) discount = total * 0.30f;
		  else if (total >= 400000 && total < 499999) discount = total * 0.35f;
		  else if (total >= 499999) discount = total * 0.40f;
		  
		  return discount;
	}
	
}
