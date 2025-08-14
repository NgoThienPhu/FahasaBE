package com.example.demo.attributevalue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.attributevalue.entity.AttributeValue;

@Repository
public interface AttributeValueRepository extends JpaRepository<AttributeValue ,String> {

}
