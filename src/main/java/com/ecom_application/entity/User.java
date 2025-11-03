package com.ecom_application.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "User_Id")
	private int id;
	
	@Column(name = "User_Name")
	private String name;
	
	@Column(name = "User_Phone", unique = true)
	private long phone;
	
	@Column(name = "User_Email", unique = true)
	private String email;
	
	@Column(name = "Password")
	private String password; 
	
	
	
}
