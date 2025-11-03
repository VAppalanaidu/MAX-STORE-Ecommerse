package com.ecom_application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecom_application.dto.OrderRequest;
import com.ecom_application.entity.Cart;
import com.ecom_application.entity.User;
import com.ecom_application.service.CartService;
import com.ecom_application.service.ProductOrderService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private CartService cartService;
	
	@Autowired
	private ProductOrderService productOrderService;
	
	@PostMapping("/")
	public String getCheckout ( HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		if(user == null) {
			return "redirect:/user/login";
		}
		List<Cart> carts = cartService.findAllByUserId(user.getId());
		
		float totalPrice = 0.0f;
		int cartCount = 0;
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
		session.setAttribute("price", finalAmount);
		model.addAttribute("cartCount", cartCount);
		model.addAttribute("user", user);
		model.addAttribute("orderRequest", new OrderRequest());
		return "checkout";
	}
	
	@PostMapping("/placeOrder")
	public String placeAnOrder (@ModelAttribute("orderRequest") OrderRequest request , HttpSession session, Model model) {
		
		User user = (User) session.getAttribute("user");
		if(user == null) {
			return "redirect:/user/login";
		}
		
		String orderId = productOrderService.saveOrder(user.getId(), request);
		System.out.println("Order is successfully placed...!");
		
		model.addAttribute("totalAmount", session.getAttribute("price"));
		model.addAttribute("orderId", orderId);
		model.addAttribute("user", user);
		return "successOrder";
	}
	
}
