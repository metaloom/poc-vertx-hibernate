package io.metaloom.poc.env;

import org.testcontainers.containers.PostgreSQLContainer;

import io.metaloom.poc.option.DatabaseOption;

/**
 * Preconfigured {@link PocPostgreSQLContainer}
 */
public class PocPostgreSQLContainer extends PostgreSQLContainer<PocPostgreSQLContainer> {

	public static final String DEFAULT_IMAGE = "postgres:13.2";

	public PocPostgreSQLContainer() {
		super(DEFAULT_IMAGE);
		withDatabaseName("postgres");
		withUsername("sa");
		withPassword("sa");
	}

	public int getPort() {
		return getFirstMappedPort();
	}

	public DatabaseOption getOptions() {
		DatabaseOption options = new DatabaseOption();
		options.setPort(getPort());
		options.setHost(getContainerIpAddress());
		options.setUsername(getUsername());
		options.setPassword(getPassword());
		options.setDatabaseName(getDatabaseName());
		options.setJDBCUrl("jdbc:postgresql://" + getContainerIpAddress() + ":" + getPort() + "/" + getDatabaseName());
		return options;
	}
}
