package com.ecom_application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom_application.entity.Category;
import com.ecom_application.repository.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository cRepo;
	
	public void saveCategory(Category c) {
		cRepo.save(c);
	}
	
	public List<Category> getAllCategories() {
		return cRepo.findAll();
	}
	
	public void deleteCategory(int id) {
		cRepo.deleteById(id);
	}
	
	public Optional<Category> findCategory(int id) {
		return cRepo.findById(id);
	}
	
}
