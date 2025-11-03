package com.ecom_application.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecom_application.entity.Cart;
import com.ecom_application.entity.Product;
import com.ecom_application.entity.ProductOrder;
import com.ecom_application.entity.User;
import com.ecom_application.repository.UserRepository;
import com.ecom_application.service.CartService;
import com.ecom_application.service.CategoryService;
import com.ecom_application.service.ProductOrderService;
import com.ecom_application.service.ProductService;
import com.ecom_application.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final UserRepository userRepository;

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private ProductOrderService productOrderService;
	
	@Autowired
	private UserService userService;

    HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
	
	@GetMapping("/")
	public String homePage(HttpSession session , Model model) {
		User user = (User) session.getAttribute("user");
		if(user == null) {
			return "redirect:/user/login";
		}
		if(session.getAttribute("price") != null) {
			session.removeAttribute("price");
		}
		List<Cart> cartItems = cartService.findAllByUserId(user.getId());
		int cartCount = cartItems.size();

		
		model.addAttribute("user", user);
		model.addAttribute("cartCount", cartCount);
		List<Product> products = productService.allProducts();
		Collections.shuffle(products);
		model.addAttribute("products", products);
		model.addAttribute("categories", categoryService.getAllCategories());
		return "home";
	}
	
	@GetMapping("/profile")
	public String getProfileDetails(HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		if(user == null) {
			return "redirect:/user/login";
		}
		User euser = userRepository.findById(user.getId()).get();
		
		List<ProductOrder> orders = productOrderService.findAllOrdersByUser(user.getId());
		for(ProductOrder order : orders) {
			productOrderService.updateOrderStatus(order.getId());
		}
		model.addAttribute("orders", orders);
		model.addAttribute("user", euser);
		return "profile";
	}
	
	@PostMapping("/update")
	public String updateProfile(@ModelAttribute("user") User updateUser, HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		userService.updateUserProfile(user.getId(), updateUser);
		return "redirect:/home/profile";
	}
	
	@PostMapping("/change-password")
	public String updatePassword(@RequestParam("currentPassword") String oldPassword, @RequestParam("newPassword") String newPassword, HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		if(user == null) {
			return "redirect:/user/login";
		}
		boolean msg = userService.updatePassword(user.getId(), oldPassword, newPassword);
		if(msg == true) {
			model.addAttribute("msg1", "Your password has been changed successfully!");
		}
		else {
			model.addAttribute("msg2", "Current password is incorrect!");
		}
		List<ProductOrder> orders = productOrderService.findAllOrdersByUser(user.getId());
		
		model.addAttribute("orders", orders);
		model.addAttribute("user", user);
		return "profile";
	}
	
	@PostMapping("/cancel/{id}")
	public String cancelOrder (@PathVariable("id") Integer id) {
		productOrderService.cancelOrder(id);
		return "redirect:/home/profile";
	}
	
	
	@GetMapping("/search")
	public String searchProduct(@RequestParam("query") String keyword, HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		if(user == null) {
			return "redirect:/user/login";
		}
		List<Cart> cartItems = cartService.findAllByUserId(user.getId());
		int cartCount = cartItems.size();
		List<Product> products =  productService.searchProducts(keyword);
		
		model.addAttribute("products", products);
		model.addAttribute("user", user);
		model.addAttribute("searchQuery", keyword);
		model.addAttribute("cartCount", cartCount);
		return "searchProduct";
	}
	
}
