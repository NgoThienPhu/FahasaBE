package com.example.demo.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.demo.entities.bases.Account;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
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
	
	@PrePersist
	public void onCreate() {
		this.accountId = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

}
