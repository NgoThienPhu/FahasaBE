package com.example.demo.address.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.address.entity.Address;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface AddressRepository extends JpaRepository<Address, String>{

	@Query("""
			SELECT a FROM Address a
			WHERE a.id = :addressId
			AND a.userAccount.id = :userAccountId
			""")
	Optional<Address> findByIdAndUserAccountId(@Param("addressId") String addressId, @Param("userAccountId") String userAccountId);
	
	@Query("""
			SELECT a FROM Address a
			WHERE a.isDefault = true
			""")
	Optional<Address> findDefaultAddress(@Param("userAccountId") String userAccountId);
	
	@Query("""
			SELECT a FROM Address a
			WHERE a.userAccount.id = :userAccountId
			ORDER BY a.isDefault DESC
			""")
	List<Address> findAllByUserAccountId(@Param("userAccountId") String userAccountId);
	
}
