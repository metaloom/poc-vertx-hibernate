package io.metaloom.poc.option;

public class DatabaseOptions {

	public static final String DEFAULT_HOST = "127.0.0.1";

	public static final int DEFAULT_PORT = 5432;

	public static final String DEFAULT_USERNAME = "postgres";

	public static final String DEFAULT_PASSWORD = "finger";

	public static final String DEFAULT_DATABASE_NAME = "loom";

	private String host = DEFAULT_HOST;

	private int port = DEFAULT_PORT;

	private String username = DEFAULT_USERNAME;

	private String password = DEFAULT_PASSWORD;

	private String databaseName = DEFAULT_DATABASE_NAME;

	public String getHost() {
		return host;
	}

	public DatabaseOptions setHost(String host) {
		this.host = host;
		return this;
	}

	public int getPort() {
		return port;
	}

	public DatabaseOptions setPort(int port) {
		this.port = port;
		return this;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public DatabaseOptions setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public DatabaseOptions setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public DatabaseOptions setUsername(String username) {
		this.username = username;
		return this;
	}

}
