package io.metaloom.poc.db.impl;

import org.hibernate.reactive.mutiny.Mutiny;

public abstract class AbstractDao {

	protected Mutiny.SessionFactory factory;

	public AbstractDao(Mutiny.SessionFactory factory) {
		this.factory = factory;
	}

}
