package io.metaloom.poc.dagger;

import javax.inject.Singleton;

import org.flywaydb.core.Flyway;

import dagger.BindsInstance;
import dagger.Component;
import io.metaloom.poc.Bootstrap;
import io.metaloom.poc.dagger.module.ContainerModule;
import io.metaloom.poc.dagger.module.HibernateModule;
import io.metaloom.poc.dagger.module.PocBindModule;
import io.metaloom.poc.dagger.module.VertxModule;
import io.metaloom.poc.db.PocGroupDao;
import io.metaloom.poc.db.PocUserDao;
import io.metaloom.poc.db.flyway.dagger.FlywayModule;
import io.metaloom.poc.env.PocPostgreSQLContainer;
import io.metaloom.poc.option.ServerOption;
import io.metaloom.poc.server.RESTServer;

@Singleton
@Component(modules = { VertxModule.class, PocBindModule.class, ContainerModule.class, HibernateModule.class, FlywayModule.class })
public interface ServerComponent {

	/**
	 * Builder for the main dagger component.
	 */
	@Component.Builder
	interface Builder {

		/**
		 * Inject the options.
		 * 
		 * @param options
		 * @return
		 */
		@BindsInstance
		Builder configuration(ServerOption options);

		/**
		 * Build the component.
		 * 
		 * @return
		 */
		ServerComponent build();
	}

	Bootstrap boot();

}
