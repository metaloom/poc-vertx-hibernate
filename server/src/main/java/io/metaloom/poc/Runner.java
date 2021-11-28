package io.metaloom.poc;

import org.hibernate.reactive.stage.Stage.SessionFactory;

import io.metaloom.poc.db.hib.HibernateUtil;
import io.metaloom.poc.env.PocPostgreSQLContainer;
import io.metaloom.poc.option.DatabaseOptions;
import io.metaloom.poc.server.ServerVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class Runner {

	public static void main(String[] args) {

		PocPostgreSQLContainer container = new PocPostgreSQLContainer();
		container.start();

		DatabaseOptions dbOptions = container.getOptions();
		SessionFactory factory = HibernateUtil.sessionFactory(dbOptions.getJdbcUrl(), dbOptions.getUsername(), dbOptions.getPassword(), false)
			.unwrap(SessionFactory.class);

		VertxOptions vertxOptions = new VertxOptions();
		vertxOptions.setPreferNativeTransport(true);

		Vertx vertx = Vertx.vertx(vertxOptions);
		if (vertx.isNativeTransportEnabled()) {
			System.out.println("Native transports have been enabled.");
		} else {
			System.err.println("Native transports have not been enabled. Maybe you are not running this on x86_64 linux");
			System.err.println("Stopping server..");
			System.exit(10);
		}

		int nVerticles = Runtime.getRuntime().availableProcessors() * 2;

		System.out.println("Deploying {" + nVerticles + "} verticles");
		for (int i = 0; i < nVerticles; i++) {
			vertx.deployVerticle(new ServerVerticle(factory), ch -> {
				if (ch.failed()) {
					ch.cause().printStackTrace();
				} else {
					System.out.println("Server verticles deployed.");
				}
			});
		}

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Stopping container");
			if (factory != null && factory.isOpen()) {
				factory.close();
			}
			if (container != null) {
				container.close();
			}
		}));
	}

}
