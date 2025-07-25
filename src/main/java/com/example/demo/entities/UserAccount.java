package com.example.demo.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.demo.entities.bases.Account;
import com.example.demo.entities.enums.Gender;
import com.example.demo.util.view.View;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
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
	@JsonView(View.Public.class)
	private String fullName;
	
	@Column(name = "gender", nullable = false)
	@Enumerated(EnumType.STRING)
	@JsonView(View.Public.class)
	private Gender gender;
	
	@Column(name = "date_of_birth", nullable = true)
	@JsonView(View.Public.class)
	private LocalDate dateOfBirth;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "email")
	@JsonView(View.Self.class)
	private Email email;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "phone_number")
	@JsonView(View.Self.class)
	private PhoneNumber phoneNumber;
	
	@Column(name = "is_active", nullable = false)
	@JsonView(View.Admin.class)
	protected Boolean isActive;

	public UserAccount(String username, String password, String fullName) {
		super(username, password);
		this.fullName = fullName;
		this.isActive = true;
	}
	
	@PrePersist
	public void onCreate() {
		this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
		this.isActive = true;
		this.gender = Gender.UNSPECIFIED;
    }

}
