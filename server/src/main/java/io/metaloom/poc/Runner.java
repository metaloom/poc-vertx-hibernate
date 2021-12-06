package io.metaloom.poc;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.poc.dagger.DaggerServerComponent;
import io.metaloom.poc.dagger.ServerComponent;
import io.metaloom.poc.option.ServerOption;

public class Runner {

	public static void main(String[] args) {
		// Use logback for logging
		File logbackFile = new File("config", "logback.xml");
		System.setProperty("logback.configurationFile", logbackFile.getAbsolutePath());
		Logger log = LoggerFactory.getLogger(Runner.class);
		log.info("Starting Vert.x Hibernate PoC");
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
