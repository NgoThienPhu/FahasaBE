package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.AttributeValue;

@Repository
public interface AttributeValueRepository extends JpaRepository<AttributeValue ,String> {

}
