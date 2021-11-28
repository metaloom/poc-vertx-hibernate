package io.metaloom.poc.db.impl;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.metaloom.poc.db.PocUser;

@Entity
@Table(name = "users")
public class PocUserImpl extends AbstractElement implements PocUser {

	@NotNull
	@Size(max = 100)
	private String username;

	private String email;

	@Size(max = 150)
	private String firstname;

	@Size(max = 150)
	private String lastname;

	private boolean enabled;

	private String passwordHash;

	public PocUserImpl() {
	}

	public PocUserImpl(String username) {
		this.username = username;
	}

	@Override
	public PocUser setUuid(UUID uuid) {
		super.setUuid(uuid);
		return this;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public PocUser setUsername(String username) {
		this.username = username;
		return this;
	}

	@Override
	public String getPasswordHash() {
		return passwordHash;
	}

	@Override
	public PocUser setPasswordHash(String hash) {
		this.passwordHash = hash;
		return this;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public PocUser setEnabled(boolean flag) {
		this.enabled = flag;
		return this;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public PocUser setEmail(String email) {
		this.email = email;
		return this;
	}

	@Override
	public String getFirstname() {
		return firstname;
	}

	@Override
	public PocUser setFirstname(String firstname) {
		this.firstname = firstname;
		return this;
	}

	@Override
	public String getLastname() {
		return lastname;
	}

	@Override
	public PocUser setLastname(String lastname) {
		this.lastname = lastname;
		return this;
	}

}
