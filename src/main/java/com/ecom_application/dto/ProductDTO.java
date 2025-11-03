package com.ecom_application.dto;

import com.ecom_application.entity.Category;
import com.ecom_application.entity.Seller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductDTO {

	private int id;
	private String name;
	private float price;
	private String imgName;
	private String description;
	private Category category;
	private Seller sellerID;
	
}