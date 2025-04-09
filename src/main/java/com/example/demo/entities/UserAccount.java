package com.example.demo.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.demo.entities.bases.Account;
import com.example.demo.entities.enums.AccountRole;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
	
	@Column(name = "email", nullable = false, unique = true)
	private  String email;
	
	@Column(name = "phone_number", nullable = false, unique = true)
	private  String phoneNumber;
	
	@Column(name = "isActive", nullable = false)
	protected Boolean isActive;

	public UserAccount(String username, String password, String fullName, String email, String phoneNumber) {
		super(username, password, AccountRole.USER);
		this.fullName = fullName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.isActive = true;
	}
	
	@PrePersist
	public void onCreate() {
		this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
		this.role = AccountRole.USER;
		this.isActive = true;
    }
	
	@PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

	@Override
	public String toString() {
		return "UserAccount [fullName=" + fullName + ", email=" + email + ", phoneNumber=" + phoneNumber + ", isActive="
				+ isActive + ", id=" + id + ", username=" + username + ", password=" + password + ", role=" + role
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	} 

}
