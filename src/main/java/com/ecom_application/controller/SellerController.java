package com.ecom_application.controller;


import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecom_application.dto.CategoryDto;
import com.ecom_application.dto.LoginSellerDTO;
import com.ecom_application.dto.ProductDTO;
import com.ecom_application.dto.SellerDTO;
import com.ecom_application.entity.Product;
import com.ecom_application.entity.ProductOrder;
import com.ecom_application.entity.Seller;
import com.ecom_application.service.CategoryService;
import com.ecom_application.service.ProductOrderService;
import com.ecom_application.service.ProductService;
import com.ecom_application.service.SellerService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/seller")
public class SellerController {

	@Autowired
	private SellerService sellerService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductOrderService productOrderService;
	

	
	public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/p_images";
	
	@GetMapping("/add")
	public String getAddProduct(Model model, HttpSession session) {
		
		Seller seller = (Seller) session.getAttribute("seller");
		if(seller == null) {
			return "redirect:/seller/login";
		}
		model.addAttribute("productDTO", new ProductDTO());
		model.addAttribute("msg", "addProduct");
		model.addAttribute("seller", seller);
		model.addAttribute("categories", categoryService.getAllCategories());
		return "sellerHome";
	}
	
	
	@GetMapping("/dashboard")
	public String getDashBoard(Model model, HttpSession session) {
	    Seller seller = (Seller) session.getAttribute("seller"); 

	    if (seller == null) {
	        return "redirect:/seller/login";
	    }
	    List<Product> products = productService.getProductBySeller(seller.getId());
	    Long totalSales = (long) 0;
	    Long monthelyRevenue = (long) 0;
	    List<ProductOrder> orders = productOrderService.findListOrdersBySellerId(seller.getId());
	    for(ProductOrder order : orders) {
	    	totalSales = (long) (totalSales + order.getPrice());
	    	if( (order.getOrderDate().isAfter(LocalDate.now().minusDays(30)) || order.getOrderDate().isEqual(LocalDate.now().minusDays(30))) &&
	    			(order.getOrderDate().isBefore(LocalDate.now()) || order.getOrderDate().isEqual(LocalDate.now())) ) {
	    		
	    		monthelyRevenue = (long) (monthelyRevenue + order.getPrice());
	    	}
	    }
	    
	    model.addAttribute("monthelyRevenue", monthelyRevenue);
	    model.addAttribute("totalSales", totalSales);
	    model.addAttribute("totalOrders", orders.size());
	    model.addAttribute("seller", seller); 
	    model.addAttribute("msg", "dashboard");
	    model.addAttribute("productDTO", new ProductDTO());
	    model.addAttribute("products", products);
	    model.addAttribute("totalProducts", products.size());
	    return "sellerHome";
	}
	
	@GetMapping("/addCategory")
	public String addCategory(Model model, HttpSession session) {
		Seller seller = (Seller) session.getAttribute("seller");
		if (seller == null) {
	        return "redirect:/seller/login";
	    }
		model.addAttribute("msg", "addCategory");
		model.addAttribute("seller", seller);
		model.addAttribute("categoryDTO", new CategoryDto());
		return "sellerHome";
	}
	
	
	
	@GetMapping("/allproducts")
	public String getProductsBySellerId(Model model, HttpSession session) {
		
		Seller seller = (Seller) session.getAttribute("seller");
		if (seller == null) {
	        return "redirect:/seller/login";
	    }
		model.addAttribute("msg", "products");
		List<Product> products =  productService.getProductBySeller(seller.getId());
		model.addAttribute("products", products);
		model.addAttribute("seller", seller);
		return "sellerHome";
	}
	
	@GetMapping("/orders")
	public String getOrdersBySeller(HttpSession session, Model model) {
		Seller seller = (Seller) session.getAttribute("seller");
		if (seller == null) {
	        return "redirect:/seller/login";
	    }
		
		List<ProductOrder> orders = sellerService.listOrdersBySellerId(seller.getId());
		for(ProductOrder order : orders) {
			productOrderService.updateOrderStatus(order.getId());
		}
		
		model.addAttribute("msg", "orders");
		model.addAttribute("orders", orders);
		model.addAttribute("seller", seller);
		return "sellerHome";
	}
	
	@GetMapping("/login")
	public String getSelletrLogin(@RequestParam(value = "success", required = false) String success, HttpSession session, @RequestParam(value = "email", required = false) String email, @RequestParam(value = "login", required = false) String login , Model model) {
		
		Seller seller = (Seller) session.getAttribute("seller");
		if(seller != null) {
			model.addAttribute("seller", seller);
			return "redirect:/seller/dashboard";
		}
		if (success != null) {
            model.addAttribute("message", "✅ Register successful! Please login.");
        }
		if (email != null) {
			model.addAttribute("message2", "Seller not found!. Please Register..");
		}
		if (login != null) {
			model.addAttribute("message3", "Invalid credentials!. Please try again.");
		}
		model.addAttribute("loginSellerDTO", new LoginSellerDTO());
		return "sellerLogin";
	}
	
	@PostMapping("/save")
	public String saveSeller(@ModelAttribute("sellerDTO") SellerDTO sellerDTO, Model model) {
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(sellerDTO.getPassword());
		
		Seller seller = new Seller();
		seller.setName(sellerDTO.getName());
		seller.setStatus("Active");
		seller.setPhone(sellerDTO.getPhone());
		seller.setEmail(sellerDTO.getEmail());
		seller.setPassword(encodedPassword);
		sellerService.saveSeller(seller);
		model.addAttribute("loginSellerDTO", new LoginSellerDTO());
		return "redirect:/seller/login?success=true";
	}
	
	@GetMapping("/register")
	public String getRegisterUser(Model model) {
		model.addAttribute("sellerDTO", new SellerDTO());
		return "createSeller";
	}
	
	@PostMapping("/check")
	public String loginCredintials(@ModelAttribute("loginSellerDTO") LoginSellerDTO loginSellerDTO, HttpSession session, Model model) {
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		if((Seller) session.getAttribute("seller") != null) {
			return "redirect:/seller/dashboard";
		}
		Seller seller = sellerService.findSeller(loginSellerDTO.getEmail());
		if(seller != null) {
			if(encoder.matches(loginSellerDTO.getPassword(), seller.getPassword())) {
				session.setAttribute("seller", seller);
				return "redirect:/seller/dashboard";
			}
		}
		else if(seller == null) {
			return "redirect:/seller/login?email=true";
		}
		return "redirect:/seller/login?login=true";
	}

	@GetMapping("/logout")
	public String logoutSeller(HttpSession session) {
		session.removeAttribute("seller");
		return "index";
	}
	
	
}
