package com.ecom_application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom_application.entity.ProductOrder;
import com.ecom_application.entity.Seller;
import com.ecom_application.repository.ProductOrderRepository;
import com.ecom_application.repository.SellerRepository;

@Service
public class SellerService {

	@Autowired
	private SellerRepository sellerRepository;
	
	@Autowired
	private ProductOrderRepository productOrderRepository;
	
	public Seller saveSeller(Seller seller) {
		return sellerRepository.save(seller);
	}
	
	public List<Seller> getAllSellers() {
		return sellerRepository.findAll();
	}
	
	public void deleteSeller(int id) {
		sellerRepository.deleteById(id);
	}
	
	public Optional<Seller> findSeller(int id) {
		return sellerRepository.findById(id);
	}
	
	public Seller findSeller(String email) {
		return sellerRepository.findByEmail(email);
	}
	
	public List<ProductOrder> listOrdersBySellerId(Integer id) {
		List<ProductOrder> orders = productOrderRepository.findByProductSellerId(id);
		return orders;
	}
}
