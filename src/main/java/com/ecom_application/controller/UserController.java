package com.ecom_application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecom_application.dto.LoginDTO;
import com.ecom_application.dto.UserDTO;
import com.ecom_application.entity.User;
import com.ecom_application.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/register")
	public String getRegisterUser(Model model) {
		model.addAttribute("userDTO", new UserDTO());
		return "createUser";
	}
	
	@GetMapping("/login")
	public String getLoginUser(@RequestParam(value = "success", required = false) String success, HttpSession session,  @RequestParam(value = "email", required = false) String email, @RequestParam(value = "login", required = false) String login, Model model) {
		User user = (User) session.getAttribute("user");
		if(user != null) {
			return "redirect:/home/";
		}
		if (success != null) {
            model.addAttribute("message", "✅ Register successful! Please login.");
        }
		if(login != null) {
			model.addAttribute("message2", "Invalid credentials!. Please try again.");
		}
		if(email != null) {
			model.addAttribute("message3", "User not found!. Please create an account.");
		}
		model.addAttribute("loginDTO", new LoginDTO());
		return "loginUser";
	}
	
	@PostMapping("/save")
	public String saveUser(@ModelAttribute("userDTO") UserDTO userDTO, Model model) {
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodePassword = encoder.encode(userDTO.getPassword());
		
		User user = new User();
		user.setName(userDTO.getName());
		user.setPhone(userDTO.getPhone());
		user.setEmail(userDTO.getEmail());
		user.setPassword(encodePassword);
		userService.saveUser(user);
		model.addAttribute("loginDTO", new LoginDTO());
		return "redirect:/user/login?success=true";
	}
	
	@PostMapping("/check")
	public String loginUser(@ModelAttribute("loginDTO") LoginDTO loginDTO, HttpSession session, Model model) {
		User user = userService.findByEmail(loginDTO.getEmail());
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if(user != null ) {
			if(encoder.matches(loginDTO.getPassword(), user.getPassword())) {
				session.setAttribute("user", user);
				return "redirect:/home/";
			}
		}
		else if(user == null) {
			return "redirect:/user/login?email=true";
		}
		
		return "redirect:/user/login?login=true";
	}
	
	@GetMapping("/logout")
	public String logoutUser(HttpSession session) {
		session.removeAttribute("user");
		return "index";
	}
	
}
