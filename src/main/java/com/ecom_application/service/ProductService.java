package com.ecom_application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom_application.dto.ProductDTO;
import com.ecom_application.entity.Product;
import com.ecom_application.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	public Product saveProduct(Product p) {
		return productRepository.save(p);
	}
	
	public List<Product> allProducts() {
		return productRepository.findAll();
	}
	
	public void deleteProduct(int id) {
		productRepository.deleteById(id);;
	}
	
	public Optional<Product> findByProductId(int id) {
		return productRepository.findById(id);
	}
	
	public List<Product> getproductByCategory(int id) {
		return productRepository.findByCategory_Id(id);
	}
	
	public List<Product> getProductBySeller(int id) {
		return productRepository.findBySellerId(id);
	}
	
	public void updateProduct(int id, ProductDTO productDTO) {
		Product product = productRepository.findById(id).get();
		product.setName(productDTO.getName());
		product.setDescription(productDTO.getDescription());
		product.setPrice(productDTO.getPrice());
		productRepository.save(product);
	}
	
	public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return productRepository.findAll();
        }
        return productRepository.searchProducts(keyword);
    }
}
