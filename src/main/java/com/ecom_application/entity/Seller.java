package com.ecom_application.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table
public class Seller {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Seller_Id")
	private int id;
	@Column(name = "Seller_Name")
	private String name;
	@Column(name = "Seller_Phone", unique = true)
	private long phone;
	@Column(name = "Seller_Email", unique = true)
	private String email;
	@Column
	private String status;
	@Column()
	private String password;
	@OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Product> product;
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Id - " + id + " Name - " + name ; 
	}
	
}
