package io.metaloom.poc;

import static io.metaloom.poc.dagger.module.VertxModule.SERVER_HOST;
import static io.metaloom.poc.dagger.module.VertxModule.SERVER_PORT;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dagger.Lazy;
import io.metaloom.poc.db.PocUserDao;
import io.metaloom.poc.env.PocPostgreSQLContainer;
import io.metaloom.poc.server.RESTServer;

@Singleton
public class Bootstrap {

	private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

	private final PocPostgreSQLContainer container;
	private final RESTServer restServer;
	private final Lazy<PocUserDao> userDao;
	private final Lazy<Flyway> flyway;

	@Inject
	public Bootstrap(PocPostgreSQLContainer container, Lazy<PocUserDao> userDao, RESTServer restServer, Lazy<Flyway> flyway) {
		this.container = container;
		this.userDao = userDao;
		this.restServer = restServer;
		this.flyway = flyway;
	}

	public void start() {

		container.start();
		System.out.println(container.getOptions().getJdbcUrl());

		flyway.get().migrate();

		// Create initial test data
		userDao.get().createUser("joedoe", user -> {
			String userUUID = "44dee6f7-879c-4c2b-89e1-462e19c99708";
			user.setUuid(UUID.fromString(userUUID));
			user.setEmail("joedoe@acme.nowhere");
			user.setFirstname("Joe");
			user.setLastname("Doe");
		}).blockingGet();

		// Start the server
		restServer.start().subscribe(() -> {
			logger.info("REST server started");
			logger.info("Now connect to http://" + SERVER_HOST + ":" + SERVER_PORT + "/users");
		}, err -> {
			err.printStackTrace();
		});

	}

}
