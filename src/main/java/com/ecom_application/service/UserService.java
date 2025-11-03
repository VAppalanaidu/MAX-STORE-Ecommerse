package com.ecom_application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecom_application.entity.User;
import com.ecom_application.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public User saveUser(User user) {
		return userRepository.save(user);
	}
	
	public List<User> findAll() {
		return userRepository.findAll();
	}
	
	public void deleteUser(int id) {
		userRepository.deleteById(id);
	}
	
	public Optional<User> findUserById(int id) {
		return userRepository.findById(id);
	}
	
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public User updateUserProfile(int id, User updatedUser) {
		User user = userRepository.findById(id).get();
		
		user.setName(updatedUser.getName());
		user.setEmail(updatedUser.getEmail());
		user.setPhone(updatedUser.getPhone());
		
		return userRepository.save(user);
	}
	
	public Boolean updatePassword(int id, String oldP, String newP) {
		User user = userRepository.findById(id).get();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if(encoder.matches(oldP, user.getPassword())) {
			user.setPassword(encoder.encode(newP));
		}
		else {
			return false;
		}
		userRepository.save(user);
		return true;
	}
	
}
