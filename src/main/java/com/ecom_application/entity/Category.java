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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Category_Id")
	private int id;
	
	@Column(name = "Category_Name")
	private String name;
	
	@Column(name = "Img_Name")
	private String imgName;
	
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Product> product;
	
	public Category(String name, String imgName) {
		this.name = name;
		this.imgName = imgName;
	}
	
	@Override
	public String toString() {
		return name + " - " + imgName;
	}
	
}
