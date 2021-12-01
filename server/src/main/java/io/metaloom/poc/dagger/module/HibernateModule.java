package io.metaloom.poc.dagger.module;

import javax.inject.Singleton;

import org.hibernate.reactive.stage.Stage.SessionFactory;

import dagger.Provides;
import dagger.Module;
import io.metaloom.poc.db.hib.HibernateUtil;
import io.metaloom.poc.env.PocPostgreSQLContainer;
import io.metaloom.poc.option.DatabaseOption;

@Module
public class HibernateModule {

	@Provides
	@Singleton
	public SessionFactory sessionFactory(PocPostgreSQLContainer container) {
		container.start();
		DatabaseOption dbOptions = container.getOptions();
		SessionFactory factory = HibernateUtil.sessionFactory(dbOptions.getJdbcUrl(), dbOptions.getUsername(), dbOptions.getPassword(), false)
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
