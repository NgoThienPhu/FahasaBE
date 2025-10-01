package com.example.demo.account.entity.base;

import java.time.LocalDateTime;

import com.example.demo.common.base.entity.BaseEntity;
import com.example.demo.common.base.entity.Email;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
	protected String username;

	@Column(name = "password", nullable = false)
	@JsonIgnore
	protected String password;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "email")
	protected Email email;
	
	@Column(name = "is_active", nullable = false)
	protected Boolean isActived = true;
	
	@Column(name = "is_delete", nullable = false)
	protected Boolean isDeleted = false;
	
    @Column(name = "deleted_at", nullable = true)
    protected LocalDateTime deletedAt;

	public Account(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public enum AccountType {
		ADMIN, USER
	}
	
	public enum TokenType {
		REFRESH, ACCESS
	}
	
	public void activate() {
        this.isActived = true;
    }

    public void deactivate() {
        this.isActived = false;
    }

    public void softDelete() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    public void restore() {
        this.isDeleted = false;
        this.deletedAt = null;
    }

}
