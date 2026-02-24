package com.example.demo.book.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateBookRequestDTO extends UpdateBookRequestDTO {	

    @Min(value = 1_000, message = "Giá sản phẩm tối thiểu là 1.000 VND")
    private BigDecimal price;

}