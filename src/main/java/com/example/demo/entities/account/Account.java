package com.example.demo.entities.account;

import java.time.LocalDateTime;

import com.example.demo.util.view.View;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PreUpdate;
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
public abstract class Account {

	@Id
	@Column(name = "account_id")
	@JsonView(View.Self.class)
	protected String accountId;

	@Column(name = "username", nullable = false, unique = true)
	@JsonView(View.Self.class)
	protected String username;

	@Column(name = "password", nullable = false)
	@JsonIgnore
	protected String password;

	@Column(name = "created_at", nullable = false)
	@JsonView(View.Employee.class)
	protected LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	@JsonView(View.Employee.class)
	protected LocalDateTime updatedAt;

	public Account(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

}
