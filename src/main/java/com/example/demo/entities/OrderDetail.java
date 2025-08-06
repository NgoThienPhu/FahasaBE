package com.example.demo.entities;

import com.example.demo.entities.common.BaseEntity;
import com.example.demo.utils.view.View;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class OrderDetail extends BaseEntity {
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	@JsonView(View.Self.class)
	private Product product;
	
	@Column(name = "quantity")
	@JsonView(View.Self.class)
	private int quantity;
	
}
