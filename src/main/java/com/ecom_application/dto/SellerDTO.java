package com.ecom_application.dto;

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
public class SellerDTO {

	private int id;
	private String name;
	private long phone;
	private String email;
	private String password;
	
}
