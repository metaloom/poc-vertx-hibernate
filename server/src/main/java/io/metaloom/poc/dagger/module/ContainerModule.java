package io.metaloom.poc.dagger.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.metaloom.poc.env.PocPostgreSQLContainer;

@Module
public class ContainerModule {

	@Provides
	@Singleton
	public PocPostgreSQLContainer postgresqlContainer() {
		PocPostgreSQLContainer container = new PocPostgreSQLContainer();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Stopping container");
			if (container != null) {
				container.close();
			}
		}));

		return container;
	}

}
