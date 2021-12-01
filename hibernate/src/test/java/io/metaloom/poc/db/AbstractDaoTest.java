package io.metaloom.poc.db;

import org.junit.Before;
import org.junit.Rule;

import io.metaloom.poc.db.flyway.FlywayHelper;
import io.metaloom.poc.env.PocPostgreSQLContainer;
import io.vertx.core.Vertx;

public class AbstractDaoTest {

	public static Vertx vertx = Vertx.vertx();

	@Rule
	public PocPostgreSQLContainer container = new PocPostgreSQLContainer();

	@Before
	public void setupClient() {
		System.out.println("---");
		System.out.println(container.getContainerIpAddress() + ":" + container.getPort());
		System.out.println("JDBCUrl: " + container.getJdbcUrl());
		System.out.println("Username: " + container.getUsername());
		System.out.println("Password: " + container.getPassword());
		System.out.println("---");
		FlywayHelper.migrate(container.getOptions());
	}
}
