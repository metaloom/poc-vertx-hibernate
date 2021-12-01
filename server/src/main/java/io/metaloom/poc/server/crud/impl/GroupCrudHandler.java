package io.metaloom.poc.server.crud.impl;

import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;

import io.metaloom.poc.db.PocGroupDao;
import io.metaloom.poc.server.crud.CrudHandler;
import io.vertx.rxjava3.ext.web.RoutingContext;

public class GroupCrudHandler implements CrudHandler {

	private final PocGroupDao groupDao;

	AtomicLong count = new AtomicLong(0);

	@Inject
	public GroupCrudHandler(PocGroupDao groupDao) {
		this.groupDao = groupDao;
	}

	@Override
	public void create(RoutingContext rc) {
		// TODO Auto-generated method stub
	}

	@Override
	public void list(RoutingContext rc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void read(RoutingContext rc) {
		String uuid = rc.pathParam("uuid");
		// TODO Auto-generated method stub
	}

	@Override
	public void delete(RoutingContext rc) {
		String uuid = rc.pathParam("uuid");
	}
}
