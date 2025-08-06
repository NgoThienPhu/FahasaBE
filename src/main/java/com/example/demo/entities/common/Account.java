package com.example.demo.entities.common;

import com.example.demo.entities.Email;
import com.example.demo.utils.view.View;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

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
	@JsonView(View.Self.class)
	protected String username;

	@Column(name = "password", nullable = false)
	@JsonIgnore
	protected String password;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "email")
	@JsonView(View.Self.class)
	protected Email email;

	public Account(String username, String password) {
		this.username = username;
		this.password = password;
	}

}
