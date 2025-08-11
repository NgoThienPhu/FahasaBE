package com.example.demo.entities.base;

import com.example.demo.entities.Email;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "account")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "account_type")
public abstract class Account extends BaseEntity {

	@Column(name = "username", nullable = false, unique = true, updatable = false)
	protected String username;

	@Column(name = "password", nullable = false)
	@JsonIgnore
	protected String password;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "email")
	protected Email email;

	public Account(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public enum AccountType {
		ADMIN, USER
	}
	
	public enum TokenType {
		REFRESH, ACCESS
	}

}
