package com.example.demo.email.entity;

import com.example.demo.util.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "email")
public class Email extends BaseEntity {
	
	@Column(name = "email", nullable = false)
	private String email;
	
	@Column(name = "is_verify", nullable = false)
	private Boolean isVerify;
	
	public Email(String email) {
		this.email = email;
	}
	
	public void verify() {
		this.isVerify = true;
	}
		
}
