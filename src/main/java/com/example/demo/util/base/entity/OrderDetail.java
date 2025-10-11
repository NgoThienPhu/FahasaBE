package com.example.demo.util.base.entity;

import com.example.demo.book.entity.Book;

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
	@JoinColumn(name = "my_order_id")
	private MyOrder myOrder;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Book product;
	
	@Column(name = "quantity")
	private int quantity;
	
}
