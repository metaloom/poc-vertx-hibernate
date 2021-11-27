package io.metaloom.poc.db.impl;

import java.util.UUID;

import io.metaloom.poc.db.PocGroup;

public class PocGroupImpl extends AbstractElement implements PocGroup {

	private String name;

	public PocGroupImpl(String name) {
		this.name = name;
	}

	@Override
	public PocGroup setUuid(UUID uuid) {
		super.setUuid(uuid);
		return this;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public PocGroup setName(String name) {
		this.name = name;
		return this;
	}

}
