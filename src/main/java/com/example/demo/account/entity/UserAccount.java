package com.example.demo.account.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.account.entity.base.Account;
import com.example.demo.common.base.entity.Address;
import com.example.demo.common.base.entity.PhoneNumber;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
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
	private Gender gender;
	
	@Column(name = "date_of_birth", nullable = true)
	private LocalDate dateOfBirth;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "phone_number")
	private PhoneNumber phoneNumber;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Address> addresses = new ArrayList<>();
	
	@Column(name = "is_active", nullable = false)
	protected Boolean isActive;

	public UserAccount(String username, String password, String fullName) {
		super(username, password);
		this.fullName = fullName;
		this.isActive = true;
	}
	
	public enum Gender {
		MALE, FEMALE, OTHER, UNSPECIFIED
	}
	
	@PrePersist
	public void onCreate() {
		super.onCreate();
		this.isActive = true;
		this.gender = Gender.UNSPECIFIED;
    }

}
