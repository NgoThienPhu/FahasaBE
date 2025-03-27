package com.example.demo.entities.bases;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.demo.entities.enums.AccountRole;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrePersist;
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
	protected String id;
	
	@Column(name = "username", nullable = false, unique = true)
	protected String username;
	
	@Column(name = "password", nullable = false)
	protected String password;
	
	@Column(name = "email", nullable = false, unique = true)
	protected String email;
	
	@Column(name = "phone_number", nullable = false, unique = true)
	protected String phoneNumber;
	
	@Column(name = "role", nullable = false)
	protected AccountRole role;
	
	@Column(name = "created_at", nullable = false)
	protected LocalDateTime createdAt;
	
	@Column(name = "updated_at", nullable = false)
	protected LocalDateTime updatedAt;
	
	public Account(String username, String password, String email, String phoneNumber, AccountRole role) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.role = role;
	}
	
	@PrePersist
	protected void onCreate() {
		this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
	
	@PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

	@Override
	public String toString() {
		return "Account [id=" + id + ", username=" + username + ", password=" + password + ", email=" + email
				+ ", phoneNumber=" + phoneNumber + ", role=" + role + ", createdAt=" + createdAt + ", updatedAt="
				+ updatedAt + "]";
	}
	
}
