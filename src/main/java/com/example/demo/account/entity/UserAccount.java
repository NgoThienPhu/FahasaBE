package com.example.demo.account.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.account.entity.base.Account;
import com.example.demo.address.entity.Address;
import com.example.demo.util.entity.PhoneNumber;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("USER")
public class UserAccount extends Account {
	
	@Column(name = "full_name", nullable = false)
	private String fullName;
	
	@Column(name = "gender", nullable = false)
	@Enumerated(EnumType.STRING)
	private Gender gender = Gender.OTHER;
	
	@Column(name = "date_of_birth", nullable = true)
	private LocalDate dateOfBirth;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "phone_number")
	private PhoneNumber phoneNumber;
	
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "userAccount", orphanRemoval = true)
	private List<Address> addresses = new ArrayList<>();

	public UserAccount(String username, String password, String fullName) {
		super(username, password);
		this.fullName = fullName;
	}
	
	public enum Gender {
		MALE, FEMALE, OTHER
	}
	
	public void activePhoneNumber() {
		this.phoneNumber.setIsVerify(true);
	}

}
