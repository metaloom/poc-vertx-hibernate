package io.metaloom.poc.server.impl;

import javax.inject.Inject;
import javax.inject.Provider;

import io.metaloom.poc.server.RESTServer;
import io.metaloom.poc.server.ServerVerticle;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.vertx.rxjava3.core.Vertx;

public class RESTServerImpl implements RESTServer {


	private Vertx rxVertx;
	private Provider<ServerVerticle> verticleProvider;

	@Inject
	public RESTServerImpl(Vertx rxVertx, Provider<ServerVerticle> verticleProvider) {
		this.rxVertx = rxVertx;
		this.verticleProvider = verticleProvider;
	}

	@Override
	public Completable start() {
		int nVerticles = Runtime.getRuntime().availableProcessors() * 2;

		System.out.println("Deploying {" + nVerticles + "} verticles");
		return Observable.range(0, nVerticles).flatMapCompletable(n -> {
			return rxVertx.rxDeployVerticle(verticleProvider.get()).doOnSuccess(id -> {
				System.out.println("Server verticle " + id + " deployed.");
			}).ignoreElement();
		});
	}

	@Override
	public Completable stop() {
		return rxVertx.rxClose();
	}

}
