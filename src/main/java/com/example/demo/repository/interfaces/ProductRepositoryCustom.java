package com.example.demo.repository.interfaces;

import java.util.List;

import com.example.demo.dto.FilterConditionDTO;
import com.example.demo.entities.Product;

public interface ProductRepositoryCustom {
	
	List<Product> filterProductByAttributes(List<FilterConditionDTO> filters);
	
}
