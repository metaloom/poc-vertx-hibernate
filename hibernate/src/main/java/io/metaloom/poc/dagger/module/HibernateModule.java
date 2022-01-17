package io.metaloom.poc.dagger.module;

import javax.inject.Singleton;

import org.hibernate.reactive.mutiny.Mutiny;

import dagger.Module;
import dagger.Provides;
import io.metaloom.poc.db.hib.HibernateUtil;
import io.metaloom.poc.env.PocPostgreSQLContainer;
import io.metaloom.poc.option.DatabaseOption;
import io.metaloom.poc.option.ServerOption;
import io.vertx.rxjava3.core.Promise;
import io.vertx.rxjava3.core.Vertx;

@Module
public class HibernateModule {

	@Provides
	@Singleton
	public Mutiny.SessionFactory sessionFactory(Vertx rxVertx, PocPostgreSQLContainer container,
			ServerOption serverOptions) {
		if (!container.isRunning()) {
			throw new RuntimeException("The database testcontainer was not yet started.");
		}
		Mutiny.SessionFactory cemf = rxVertx.rxExecuteBlocking((Promise<Mutiny.SessionFactory> bc) -> {
			try {
				DatabaseOption options = container.getOptions();
				boolean logging = false;
				int poolSize = serverOptions.getHibernatePoolSize();
				String jdbcUrl = options.getJdbcUrl();
				String user = options.getUsername();
				String pass = options.getPassword();
				Mutiny.SessionFactory emf = HibernateUtil.sessionFactory(jdbcUrl, user, pass, logging, poolSize)
						.unwrap(Mutiny.SessionFactory.class);

				Runtime.getRuntime().addShutdownHook(new Thread(() -> {
					System.out.println("Closing factory");
					if (emf != null && emf.isOpen()) {
						emf.close();
					}
				}));
				bc.complete(emf);
			} catch (Exception e) {
				bc.fail(e);
			}
		}).blockingGet();

		return cemf;
	}
}
