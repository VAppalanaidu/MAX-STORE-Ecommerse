package com.ecom_application.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecom_application.entity.Cart;
import com.ecom_application.entity.Product;
import com.ecom_application.entity.Seller;
import com.ecom_application.entity.User;
import com.ecom_application.service.CartService;
import com.ecom_application.service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private ProductService productService;

	
	
	@GetMapping("/")
	public String getCart(Model model, HttpSession session) {

		int cartCount = 0; float totalPrice = 0.0f;
		User user = (User) session.getAttribute("user");
		if(user == null) {
			return "redirect:/user/login";
		}
		
		List<Cart> carts = cartService.findAllByUserId(user.getId());
		if(carts == null) {
			carts = new ArrayList<>();
		}
		for(Cart c : carts) {
			cartCount += c.getQuantity();
			totalPrice += c.getProduct().getPrice() * c.getQuantity();
		}
		float discount = cartService.discountPrice(totalPrice);
		float finalAmount = totalPrice - discount;
		int deliveryfee = finalAmount < 499 && finalAmount > 0 ? 80 : 0;
		model.addAttribute("totalPrice", totalPrice);
		model.addAttribute("discount", discount);
		model.addAttribute("finalAmount", totalPrice - discount + deliveryfee);
		
		if(deliveryfee != 0) {
			model.addAttribute("deliveryFee", 80);
		}
		else if(deliveryfee == 0) {
			model.addAttribute("deliveryFee", "Free");
		}
		
		model.addAttribute("cartCount", cartCount);
		model.addAttribute("user", user);
		model.addAttribute("products", carts);
		return "cart";
	}
	
	
	@GetMapping("/{id}")
	public String getProductDetails(@PathVariable("id") int id , Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		if(user == null) {
			return "redirect:/user/login";
		}
		Product product = productService.findByProductId(id).get();
		
		Seller seller = product.getSeller();
		List<Cart> cartItems = cartService.findAllByUserId(user.getId());
		int cartCount = cartItems.size();
		
		model.addAttribute("cartCount", cartCount);
		model.addAttribute("product", product);
		model.addAttribute("user", user);
		model.addAttribute("seller", seller);
		return "product";
	}
	
	@PostMapping("/add/{id}")
	public String addItemToCart(@PathVariable("id") int id, HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		try {
			Cart added = cartService.saveCartItem(id, user.getId());
			if(added != null) {
				model.addAttribute("succMsg", "Product added to cart");
			}
			else {
				 model.addAttribute("errorMsg", "Product add to cart failed");
			}
		} catch (Exception e) {
			session.setAttribute("errorMsg", "Something went wrong!");
		}
		Seller seller = (Seller) session.getAttribute("seller");
		Product product = productService.findByProductId(id).get();
		List<Cart> cartItems = cartService.findAllByUserId(user.getId());
		int cartCount = cartItems.size();
	    
		model.addAttribute("cartCount", cartCount);
		model.addAttribute("product", product);
		model.addAttribute("user", user);
		model.addAttribute("seller", seller);
		return "product";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteByCart(@PathVariable("id") int id, Model model, HttpSession session) {
		cartService.deleteByPrductId(id);
		return "redirect:/cart/";
	}
	
	
	@GetMapping("/plusItem/{id}")
	public String plusCartItemById(@PathVariable("id") Integer id, HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		if(user == null) {
			return "redirect:/user/login";
		}
		cartService.increaseQuantity(id);
		
		return "redirect:/cart/";
	}
	
	@GetMapping("/decrItem/{id}")
	public String decreaseCartItemById(@PathVariable("id") Integer id, HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		if(user == null) {
			return "redirect:/user/login";
		}
		cartService.decreaseQuantity(id);
		
		return "redirect:/cart/";
	}
}
