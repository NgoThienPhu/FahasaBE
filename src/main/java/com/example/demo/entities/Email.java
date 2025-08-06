package com.example.demo.entities;

import com.example.demo.entities.common.BaseEntity;
import com.example.demo.utils.view.View;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
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
	@JsonView(View.Self.class)
	private String email;
	
	@Column(name = "is_verify", nullable = false)
	@JsonView(View.Self.class)
	private Boolean isVerify;
	
	public Email(String email) {
		this.email = email;
	}
	
	@PrePersist
	public void onCreate() {
		super.onCreate();
		this.isVerify = false;
	}
		
}
