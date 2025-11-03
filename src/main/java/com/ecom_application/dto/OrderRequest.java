package com.ecom_application.dto;

import lombok.Data;

@Data
public class OrderRequest {

	private String firstName;
	private String lastName;
	private String email;
	private long mobile;
	private String address;
	private String city;
	private String state;
	private int pincode;
	private String paymentType;
	private float price;
	
}
