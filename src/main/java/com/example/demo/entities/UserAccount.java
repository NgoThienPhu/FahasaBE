package com.example.demo.entities;

import com.example.demo.entities.bases.Account;
import com.example.demo.entities.enums.AccountRole;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
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

	public UserAccount(String username, String password, String email, String phoneNumber, String fullName) {
		super(username, password, email, phoneNumber, AccountRole.USER);
		this.fullName = fullName;
	}

	@Override
	public String toString() {
		return "UserAccount [fullName=" + fullName + ", id=" + id + ", username=" + username + ", password=" + password
				+ ", email=" + email + ", phoneNumber=" + phoneNumber + ", role=" + role + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + "]";
	}

}
