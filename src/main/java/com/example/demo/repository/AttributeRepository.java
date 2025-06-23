package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Attribute;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, String>  {

}
