package com.example.demo.account.entity.base;

import com.example.demo.email.entity.Email;
import com.example.demo.util.entity.BaseEntity;
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

@Entity
@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "account_type")
@Table(name = "account")
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
	
	public void changePassword(String newPasswordEncoded) {
		this.password = newPasswordEncoded;
	}
    
    public void changeEmail(Email email) {
		this.email = email;
	}

}
