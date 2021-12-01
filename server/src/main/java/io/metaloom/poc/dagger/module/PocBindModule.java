package io.metaloom.poc.dagger.module;

import dagger.Binds;
import dagger.Module;
import io.metaloom.poc.db.PocGroupDao;
import io.metaloom.poc.db.PocUserDao;
import io.metaloom.poc.db.impl.PocGroupDaoImpl;
import io.metaloom.poc.db.impl.PocUserDaoImpl;
import io.metaloom.poc.server.RESTServer;
import io.metaloom.poc.server.impl.RESTServerImpl;

@Module
public abstract class PocBindModule {

	@Binds
	abstract RESTServer bindRESTServer(RESTServerImpl e);

	@Binds
	abstract PocUserDao bindUserDao(PocUserDaoImpl e);

	@Binds
	abstract PocGroupDao bindGroupDao(PocGroupDaoImpl e);
}