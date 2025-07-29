package com.example.demo.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.demo.util.view.View;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "order_detail")
public class OrderDetail {
	
	@Id
	@Column(name = "order_detail_id")
	@JsonView(View.Self.class)
	private String orderDetailId;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	@JsonView(View.Self.class)
	private Product product;
	
	@Column(name = "quantity")
	@JsonView(View.Self.class)
	private int quantity;
	
	@Column(name = "created_at", nullable = false)
	@JsonView(View.Employee.class)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	@JsonView(View.Employee.class)
	private LocalDateTime updatedAt;
	
	@PrePersist
	public void onCreate() {
		this.orderDetailId = UUID.randomUUID().toString();
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	public void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
	
}
