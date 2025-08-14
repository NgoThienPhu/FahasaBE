package com.example.demo.account.entity;

import com.example.demo.account.entity.base.Account;

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
