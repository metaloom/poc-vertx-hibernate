package io.metaloom.poc;

import io.metaloom.poc.dagger.DaggerServerComponent;
import io.metaloom.poc.dagger.ServerComponent;
import io.metaloom.poc.dagger.module.VertxModule;
import io.metaloom.poc.option.ServerOption;

public class Runner {

	public static void main(String[] args) {

		ServerOption options = new ServerOption();
		options.setPort(8888);

		// Inject the options and build the dagger dependency graph
		ServerComponent serverComponent = DaggerServerComponent
			.builder()
			.configuration(options)
			.build();

		// Start the server
		serverComponent.restServer().start().subscribe(() -> {
			System.out.println("REST server started");
			System.out.println("Now connect to http://" + VertxModule.SERVER_HOST + ":" + VertxModule.SERVER_PORT + "/users");
		}, err -> {
			err.printStackTrace();
		});
	}

}
