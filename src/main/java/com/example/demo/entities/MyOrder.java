package com.example.demo.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.demo.util.view.View;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
public class MyOrder {

	@Id
	@Column(name = "my_order_id", nullable = false)
	@JsonView(View.Self.class)
	private String myOrderId;
	
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
	
	@Column(name = "created_at", nullable = false)
	@JsonView(View.Employee.class)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	@JsonView(View.Employee.class)
	private LocalDateTime updatedAt;
	
	@PrePersist
	public void onCreate() {
		this.myOrderId = UUID.randomUUID().toString();
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	public void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
	
}
