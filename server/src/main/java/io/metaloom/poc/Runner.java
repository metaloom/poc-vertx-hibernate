package io.metaloom.poc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.poc.dagger.DaggerServerComponent;
import io.metaloom.poc.dagger.ServerComponent;
import io.metaloom.poc.option.ServerOption;

public class Runner {

	private static final Logger logger = LoggerFactory.getLogger(Runner.class);

	public static void main(String[] args) {
		logger.info("Starting Vert.x Hibernate PoC");
		ServerOption options = new ServerOption();
		options.setPort(8080);
		options.setVerticleCount(8);
		options.setHibernatePoolSize(16);

		// Inject the options and build the dagger dependency graph
		ServerComponent serverComponent = DaggerServerComponent
			.builder()
			.configuration(options)
			.build();

		// Use the bootstrap to startup the individual components in the right order.
		serverComponent.boot().start();
	}

}
