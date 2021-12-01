package io.metaloom.poc.server.crud;

import io.vertx.rxjava3.ext.web.RoutingContext;

/**
 * Rudimentary handler definition for CRUD operations.
 */
public interface CrudHandler {

	void create(RoutingContext rc);

	void list(RoutingContext rc);

	void read(RoutingContext rc);

	void delete(RoutingContext rc);

}
