package com.ecom_application.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ecom_application.dto.CategoryDto;
import com.ecom_application.entity.Category;
import com.ecom_application.service.CategoryService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/c_images";
	
	@GetMapping("/add")
	public String addCategory(Model model) {
		model.addAttribute("categoryDTO", new CategoryDto());
		return "addCategory";
	}
	
	@PostMapping("/save")
	public String saveCategory(@ModelAttribute("categoryDTO") CategoryDto categoryDto,
	                           @RequestParam("image") MultipartFile file,
	                           HttpSession session) throws IOException {

	    Category c = new Category();
	    c.setName(categoryDto.getName());

	    String imageUUID;
	    if (!file.isEmpty()) {
	        imageUUID = file.getOriginalFilename();
	        Path path = Paths.get(uploadDir, imageUUID);
	        Files.write(path, file.getBytes());
	    } else {
	        imageUUID = categoryDto.getImgName();
	    }

	    c.setImgName(imageUUID);
	    categoryService.saveCategory(c);
	    return "redirect:/seller/dashboard";
	}

	
	
}
