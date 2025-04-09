package com.example.demo.entities.bases;

import java.time.LocalDateTime;

import com.example.demo.entities.enums.AccountRole;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
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
	protected String id;
	
	@Column(name = "username", nullable = false, unique = true)
	protected String username;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "password", nullable = false)
	protected String password;
	
	@Column(name = "role", nullable = false)
	@Enumerated(EnumType.STRING)
	protected AccountRole role;
	
	@Column(name = "created_at", nullable = false)
	protected LocalDateTime createdAt;
	
	@Column(name = "updated_at", nullable = false)
	protected LocalDateTime updatedAt;
	
	public Account(String username, String password, AccountRole role) {
		this.username = username;
		this.password = password;
		this.role = role;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", username=" + username + ", password=" + password + ", role=" + role
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
	
}
