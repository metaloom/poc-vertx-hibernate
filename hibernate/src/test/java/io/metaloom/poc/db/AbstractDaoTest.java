package io.metaloom.poc.db;

import org.junit.Before;
import org.junit.Rule;

import io.metaloom.poc.db.flyway.FlywayHelper;
import io.metaloom.poc.env.PocPostgreSQLContainer;
import io.vertx.core.Vertx;
import io.vertx.sqlclient.SqlClient;

public class AbstractDaoTest {

	public static Vertx vertx = Vertx.vertx();

	@Rule
	public PocPostgreSQLContainer container = new PocPostgreSQLContainer();

	private SqlClient sqlClient;

	@Before
	public void setupClient() {
		System.out.println("---");
		System.out.println(container.getContainerIpAddress() + ":" + container.getPort());
		System.out.println("JDBCUrl: " + container.getJdbcUrl());
		System.out.println("Username: " + container.getUsername());
		System.out.println("Password: " + container.getPassword());
		System.out.println("---");
		FlywayHelper.migrate(container.getOptions());
		//this.sqlClient = setupSQLClient(vertx, container.getOptions());
	}
}
