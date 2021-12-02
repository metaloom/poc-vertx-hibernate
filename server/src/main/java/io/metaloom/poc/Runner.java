package io.metaloom.poc;

import static io.metaloom.poc.dagger.module.VertxModule.SERVER_HOST;
import static io.metaloom.poc.dagger.module.VertxModule.SERVER_PORT;

import java.util.UUID;

import io.metaloom.poc.dagger.DaggerServerComponent;
import io.metaloom.poc.dagger.ServerComponent;
import io.metaloom.poc.option.ServerOption;

public class Runner {

	public static void main(String[] args) {
		ServerOption options = new ServerOption();
		options.setPort(8888);
		options.setVerticleCount(4);
		options.setHibernatePoolSize(10);

		// Inject the options and build the dagger dependency graph
		ServerComponent serverComponent = DaggerServerComponent
			.builder()
			.configuration(options)
			.build();

		// Create initial test data
		serverComponent.userDao().createUser("joedoe", user -> {
			String userUUID = "44dee6f7-879c-4c2b-89e1-462e19c99708";
			user.setUuid(UUID.fromString(userUUID));
			user.setEmail("joedoe@acme.nowhere");
			user.setFirstname("Joe");
			user.setLastname("Doe");
		}).blockingGet();

		// Start the server
		serverComponent.restServer().start().subscribe(() -> {
			System.out.println("REST server started");
			System.out.println("Now connect to http://" + SERVER_HOST + ":" + SERVER_PORT + "/users");
		}, err -> {
			err.printStackTrace();
		});
	}

}
