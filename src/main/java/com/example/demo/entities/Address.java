package com.example.demo.entities;

import com.example.demo.entities.common.BaseEntity;
import com.example.demo.utils.view.View;
import com.fasterxml.jackson.annotation.JsonView;

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
@Table(name = "address")
public class Address extends BaseEntity {
	
	@Column(name = "fullname")
	@JsonView(View.Self.class)
	private String fullName;
	
	@Column(name = "phone_number")
	@JsonView(View.Self.class)
	private String phoneNumber;
	
	@Column(name = "address")
	@JsonView(View.Self.class)
	private String address;
	
}
