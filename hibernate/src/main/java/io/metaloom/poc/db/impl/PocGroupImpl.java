package io.metaloom.poc.db.impl;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Table;

import io.metaloom.poc.db.PocGroup;
import io.vertx.core.json.JsonObject;

@Entity
@Table(name = "groups")
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

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PocGroupImpl) {
			return getUuid().equals(((PocGroupImpl) obj).getUuid());
		} else {
			return super.equals(obj);
		}
	}

	@Override
	public int hashCode() {
		return getUuid().hashCode();
	}

	@Override
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		json.put("uuid", getUuid());
		json.put("name", getName());
		return json;
	}

}
