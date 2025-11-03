package com.ecom_application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecom_application.dto.ProductDTO;
import com.ecom_application.entity.Product;
import com.ecom_application.entity.ProductOrder;
import com.ecom_application.entity.Seller;
import com.ecom_application.service.ProductOrderService;
import com.ecom_application.service.ProductService;
import com.ecom_application.service.SellerService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/update")
public class SellerUpdateController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private SellerService sellerService;
	
	@Autowired
	private ProductOrderService productOrderService;
	
	@GetMapping("/product/{id}")
	public String getUpdateProduct(@PathVariable("id") int id, Model model, HttpSession session) {
		Seller seller = (Seller) session.getAttribute("seller");
		if (seller == null) {
	        return "redirect:/seller/login";
	    }
		Product product = productService.findByProductId(id).get();
		session.setAttribute("id", id);
		model.addAttribute("seller", seller);
		model.addAttribute("msg", "updateProduct");
		model.addAttribute("existingProduct", product);
		model.addAttribute("product", new ProductDTO());
		return "sellerHome";
	}
	
	@PostMapping("/item")
	public String updateProduct(@ModelAttribute("product") ProductDTO productDTO, HttpSession session, Model model) {
		int productId = (int) session.getAttribute("id");
		productService.updateProduct(productId, productDTO);
		session.removeAttribute("id");
		return "redirect:/seller/allproducts";
	}
	
	@GetMapping("/delete/{id}")
	public String availabilityOfProductById(@PathVariable("id") int id) {
		Product product = productService.findByProductId(id).get();
		product.setAvailable(!product.getAvailable());
		productService.saveProduct(product);
		return "redirect:/seller/allproducts"; 
	}
	
	@GetMapping("/seller")
	public String sellerProfile(HttpSession session, Model model) {
		Seller seller = (Seller) session.getAttribute("seller");
		if (seller == null) {
	        return "redirect:/seller/login";
	    }
		Seller s = sellerService.findSeller(seller.getId()).get();
		model.addAttribute("msg", "profile");
		model.addAttribute("seller", s);
		return "sellerHome";
	}
	
	@PostMapping("/seller-profile")
	public String updateSellerProfile (@ModelAttribute("seller") Seller updatedSeller, HttpSession session) {
		Seller seller = (Seller) session.getAttribute("seller");
		if (seller == null) {
	        return "redirect:/seller/login";
	    }
		
		Seller s = sellerService.findSeller(seller.getId()).get();
		s.setName(updatedSeller.getName());
		s.setEmail(updatedSeller.getEmail());
		s.setPhone(updatedSeller.getPhone());
		s.setStatus(updatedSeller.getStatus());
		sellerService.saveSeller(s);
		return "redirect:/update/seller";
	}
	
	@GetMapping("/view-order/{id}")
	public String viewOrder (@PathVariable("id") int id, HttpSession session, Model model) {
		Seller seller = (Seller) session.getAttribute("seller");
		if (seller == null) {
	        return "redirect:/seller/login";
	    }
		productOrderService.updateOrderStatus(id);
		ProductOrder order = productOrderService.findByOrderById(id).get();
		model.addAttribute("msg", "orderView");
		model.addAttribute("productOrder", order);
		model.addAttribute("seller", seller);
		return "sellerHome";
	}
}
