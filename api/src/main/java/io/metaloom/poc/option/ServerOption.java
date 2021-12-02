package io.metaloom.poc.option;

public class ServerOption {

	public static final int DEFAULT_POOL_SIZE = 10;

	private int port;

	private Integer verticleCount = null;

	private int hibernatePoolSize = DEFAULT_POOL_SIZE;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Integer getVerticleCount() {
		return verticleCount;
	}

	public ServerOption setVerticleCount(int vertcielCount) {
		this.verticleCount = vertcielCount;
		return this;
	}

	public ServerOption setHibernatePoolSize(int hibernatePoolSize) {
		this.hibernatePoolSize = hibernatePoolSize;
		return this;
	}

	public int getHibernatePoolSize() {
		return hibernatePoolSize;
	}
}