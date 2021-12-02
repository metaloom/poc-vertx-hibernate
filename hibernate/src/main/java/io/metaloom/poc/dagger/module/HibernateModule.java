package io.metaloom.poc.dagger.module;

import javax.inject.Singleton;

import org.hibernate.reactive.stage.Stage.SessionFactory;

import dagger.Provides;
import dagger.Module;
import io.metaloom.poc.db.hib.HibernateUtil;
import io.metaloom.poc.env.PocPostgreSQLContainer;
import io.metaloom.poc.option.DatabaseOption;
import io.metaloom.poc.option.ServerOption;

@Module
public class HibernateModule {

	@Provides
	@Singleton
	public SessionFactory sessionFactory(PocPostgreSQLContainer container, ServerOption serverOptions) {
		container.start();
		DatabaseOption options = container.getOptions();
		boolean logging = false;
		int poolSize = serverOptions.getHibernatePoolSize();
		String jdbcUrl = options.getJdbcUrl();
		String user = options.getUsername();
		String pass = options.getPassword();
		SessionFactory factory = HibernateUtil.sessionFactory(jdbcUrl, user, pass, logging, poolSize)
			.unwrap(SessionFactory.class);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Closing factory");
			if (factory != null && factory.isOpen()) {
				factory.close();
			}
		}));
		return factory;
	}
}
