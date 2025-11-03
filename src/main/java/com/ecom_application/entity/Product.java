package com.ecom_application.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Product_Id")
	private int id;
	
	@Column(name = "Product_Name")
	private String name;
	
	@Column(name = "Product_Price")
	private float price;
	
	@Column(name = "Image_Name")
	private String imgName;
	
	@Column
	private String Description;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Category_Id", referencedColumnName = "Category_Id")
	private Category category;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Seller_Id", referencedColumnName = "Seller_Id")
	private Seller seller;
	
	@Column
	private Boolean available;
	
}
