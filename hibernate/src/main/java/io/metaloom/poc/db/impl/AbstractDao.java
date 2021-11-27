package io.metaloom.poc.db.impl;

import org.hibernate.reactive.stage.Stage.SessionFactory;

public abstract class AbstractDao {

	protected SessionFactory factory;

	public AbstractDao(SessionFactory factory) {
		this.factory = factory;
	}

}
