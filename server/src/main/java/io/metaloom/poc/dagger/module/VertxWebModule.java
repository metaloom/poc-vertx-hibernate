package io.metaloom.poc.dagger.module;

import dagger.Module;
import dagger.Provides;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.ext.web.Router;

@Module
public class VertxWebModule {

	@Provides
	public Router rxRouter(Vertx rxVertx) {
		return Router.router(rxVertx);
	}

}