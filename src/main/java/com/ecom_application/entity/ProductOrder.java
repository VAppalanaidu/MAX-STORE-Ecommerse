package com.ecom_application.entity;



import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class ProductOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String orderId;
	private LocalDate orderDate;
	
	private LocalDate deliveryDate;
	
	@ManyToOne
	@JoinColumn(name = "Product_Id", referencedColumnName = "Product_Id")
	private Product product;
	private double price;
	private Integer quantity;
	
	@ManyToOne
	@JoinColumn(name = "User_Id", referencedColumnName = "User_Id")
	private User user;
	
	
	private String Status;
	private String paymentType;
	
	@OneToOne(cascade = CascadeType.ALL)
	private OrderAddress orderAddress;
	
}
