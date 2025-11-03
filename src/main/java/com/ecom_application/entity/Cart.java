package com.ecom_application.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Cart {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Cart_ID")
	private int id;
	
	
	@ManyToOne
	@JoinColumn(name = "Product_Id", referencedColumnName = "Product_Id")
	private Product product;
	
	
	@ManyToOne
	@JoinColumn(name = "User_Id", referencedColumnName = "User_Id")
	private User user;
	
	@Column()
	private int quantity;
	
	@Column()
	private Float price;
}
