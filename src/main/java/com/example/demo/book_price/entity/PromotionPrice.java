package com.example.demo.book_price.entity;

import com.example.demo.book_price.base.BookPrice;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("PROMOTION")
public class PromotionPrice extends BookPrice {

}
