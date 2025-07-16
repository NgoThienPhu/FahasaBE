package com.example.demo.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.dto.FilterConditionDTO;
import com.example.demo.entities.Product;
import com.example.demo.repository.interfaces.ProductRepositoryCustom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Product> filterProductByAttributes(List<FilterConditionDTO> filters) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private String buildDynamicFilterProductByAttributesQuery() {
		return null;
	}

}
