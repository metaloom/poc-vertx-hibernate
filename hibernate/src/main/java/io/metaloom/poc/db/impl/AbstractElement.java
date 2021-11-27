package io.metaloom.poc.db.impl;

import java.util.UUID;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import io.metaloom.poc.db.PocElement;

public abstract class AbstractElement implements PocElement {

	@Id
	@GeneratedValue
	private UUID uuid;

	@Override
	public UUID getUuid() {
		return uuid;
	}

	@Override
	public PocElement setUuid(UUID uuid) {
		this.uuid = uuid;
		return this;
	}

}
