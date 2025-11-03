package com.ecom_application.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.ecom_application.dto.ProductDTO;
import com.ecom_application.entity.Cart;
import com.ecom_application.entity.Category;
import com.ecom_application.entity.Product;
import com.ecom_application.entity.Seller;
import com.ecom_application.entity.User;
import com.ecom_application.service.CartService;
import com.ecom_application.service.CategoryService;
import com.ecom_application.service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/product")
public class ProductController {

	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CartService cartService;
		
	public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/p_images";

	@GetMapping("/category/{id}")
	public String productsByCategory(@PathVariable int id, HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		if(user == null) {
			return "redirect:/user/login";
		}
	    Optional<Category> categoryOptional = categoryService.findCategory(id);
	    
	    Category category;
	    if (categoryOptional.isPresent()) {
	        category = categoryOptional.get();
	    } else {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with ID " + id + " not found.");
	    }
	    List<Cart> cartItems = cartService.findAllByUserId(user.getId());
		int cartCount = cartItems.size();
	    
		model.addAttribute("cartCount", cartCount);
	    model.addAttribute("category", category);
	    List<Product> products = productService.getproductByCategory(id);
	    model.addAttribute("products", products);
	    model.addAttribute("user", session.getAttribute("user"));

	    return "productsByCategory";
	}
	
	@PostMapping("/save")
	public String saveProduct(@ModelAttribute("productDTO") ProductDTO productDTO, @RequestParam("image") MultipartFile file, HttpSession session, Model model) throws IOException {
		Product product = new Product();
		product.setName(productDTO.getName());
		product.setPrice(productDTO.getPrice());
		product.setDescription(productDTO.getDescription());
		product.setCategory(productDTO.getCategory());
		product.setAvailable(true);
		
		product.setSeller((Seller)session.getAttribute("seller"));
		
		
		String imageUUID;
		if(!file.isEmpty()) {
			imageUUID = file.getOriginalFilename();
	        Path path = Paths.get(uploadDir, imageUUID);
	        Files.write(path, file.getBytes());
		}
		else {
			imageUUID = productDTO.getImgName();
		}
		
		product.setImgName(imageUUID);
		productService.saveProduct(product);
		return "redirect:/seller/dashboard";
	}
	

	
}
