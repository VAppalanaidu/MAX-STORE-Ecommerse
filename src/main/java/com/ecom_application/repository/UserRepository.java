package com.ecom_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom_application.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	User findByEmail(String email);
	
}
