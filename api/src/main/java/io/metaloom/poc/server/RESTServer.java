package io.metaloom.poc.server;

import io.reactivex.rxjava3.core.Completable;

public interface RESTServer {

	Completable start();

	Completable stop();
}