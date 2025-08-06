package com.example.demo.entities;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.entities.common.BaseEntity;
import com.example.demo.utils.view.View;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "my_order")
public class MyOrder extends BaseEntity {

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonView(View.Self.class)
	private List<OrderDetail> orderDetails = new ArrayList<>();
	
	@ManyToOne
	@JoinColumn(name = "address_id", nullable = false)
	@JsonView(View.Self.class)
	private Address address;
	
	@Column(name = "payment_method", nullable = false)
	@JsonView(View.Self.class)
	private String paymentMethod;
	
}
