package com.example.demo.entities;

import com.example.demo.entities.bases.Account;
import com.example.demo.entities.enums.AccountRole;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("ADMIN")
public class AdminAccount extends Account {

	public AdminAccount(String username, String password, AccountRole role) {
		super(username, password, role);
	}

	@Override
	public String toString() {
		return "AdminAccount [id=" + id + ", username=" + username + ", password=" + password + ", role=" + role
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}

}
