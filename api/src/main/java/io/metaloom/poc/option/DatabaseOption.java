package io.metaloom.poc.option;

public class DatabaseOption {

	public static final String DEFAULT_HOST = "127.0.0.1";

	public static final int DEFAULT_PORT = 5432;

	public static final String DEFAULT_USERNAME = "postgres";

	public static final String DEFAULT_PASSWORD = "finger";

	public static final String DEFAULT_DATABASE_NAME = "loom";

	public static final String DEFAULT_JDBC_URL = "jdbc:postgresql://" + DEFAULT_HOST + ":" + DEFAULT_PORT + "/" + DEFAULT_DATABASE_NAME;

	private String host = DEFAULT_HOST;

	private int port = DEFAULT_PORT;

	private String username = DEFAULT_USERNAME;

	private String password = DEFAULT_PASSWORD;

	private String databaseName = DEFAULT_DATABASE_NAME;

	private String jdbcUrl = DEFAULT_JDBC_URL;

	public String getHost() {
		return host;
	}

	public DatabaseOption setHost(String host) {
		this.host = host;
		return this;
	}

	public int getPort() {
		return port;
	}

	public DatabaseOption setPort(int port) {
		this.port = port;
		return this;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public DatabaseOption setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public DatabaseOption setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public DatabaseOption setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public DatabaseOption setJDBCUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
		return this;
	}

}
