package io.metaloom.poc.server.impl;

import javax.inject.Inject;
import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.poc.option.ServerOption;
import io.metaloom.poc.server.RESTServer;
import io.metaloom.poc.server.ServerVerticle;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.vertx.rxjava3.core.Vertx;

public class RESTServerImpl implements RESTServer {

	private static final Logger logger = LoggerFactory.getLogger(RESTServerImpl.class);

	private Vertx rxVertx;
	private Provider<ServerVerticle> verticleProvider;
	private ServerOption options;

	@Inject
	public RESTServerImpl(Vertx rxVertx, Provider<ServerVerticle> verticleProvider, ServerOption options) {
		this.rxVertx = rxVertx;
		this.verticleProvider = verticleProvider;
		this.options = options;
	}

	@Override
	public Completable start() {
		int nVerticles = 1;
		if (options.getVerticleCount() == null) {
			nVerticles = Runtime.getRuntime().availableProcessors() * 2;
		} else {
			nVerticles = options.getVerticleCount();
		}

		logger.info("Deploying {" + nVerticles + "} verticles");
		return Observable.range(0, nVerticles).flatMapCompletable(n -> {
			return rxVertx.rxDeployVerticle(verticleProvider.get()).doOnSuccess(id -> {
				logger.info("Server verticle " + id + " deployed.");
			}).ignoreElement();
		});
	}

	@Override
	public Completable stop() {
		return rxVertx.rxClose();
	}

}
