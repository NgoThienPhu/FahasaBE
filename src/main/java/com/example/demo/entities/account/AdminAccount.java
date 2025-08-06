package com.example.demo.entities.account;

import com.example.demo.entities.common.Account;

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

	public AdminAccount(String username, String password) {
		super(username, password);
	}
	
}
