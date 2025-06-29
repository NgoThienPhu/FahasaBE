package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Attribute;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, String>  {
	
	Boolean existsByName(String attributeName);
	
	@Query("SELECT a FROM Attribute a WHERE a.name LIKE CONCAT('%', :attributeName, '%')")
	List<Attribute> findByName(@Param("attributeName") String attributeName, Sort sort);
	
}
