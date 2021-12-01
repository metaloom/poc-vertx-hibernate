package io.metaloom.poc.dagger.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.metaloom.poc.option.ServerOption;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.file.FileSystem;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;

@Module
public class VertxModule {

	public static final int SERVER_PORT = 8080;
	public static final String SERVER_HOST = "localhost";

	@Provides
	@Singleton
	public VertxOptions vertxOptions() {
		VertxOptions vertxOptions = new VertxOptions();
		vertxOptions.setPreferNativeTransport(true);
		return vertxOptions;
	}

	@Provides
	@Singleton
	public Vertx vertx(VertxOptions options) {
		Vertx vertx = Vertx.vertx(options);

		if (vertx.isNativeTransportEnabled()) {
			System.out.println("Native transports have been enabled.");
		} else {
			System.err.println("Native transports have not been enabled. Maybe you are not running this on x86_64 linux");
			System.err.println("Stopping server..");
			System.exit(10);
		}

		return vertx;
	}

	@Provides
	@Singleton
	public io.vertx.rxjava3.core.Vertx rxVertx(Vertx vertx) {
		return new io.vertx.rxjava3.core.Vertx(vertx);
	}

	@Provides
	@Singleton
	public FileSystem filesystem(Vertx vertx) {
		return vertx.fileSystem();
	}

	@Provides
	@Singleton
	public io.vertx.rxjava3.core.file.FileSystem rxFilesystem(io.vertx.rxjava3.core.Vertx rxVertx) {
		return rxVertx.fileSystem();
	}

	@Provides
	@Singleton
	public EventBus eventBus(Vertx vertx) {
		return vertx.eventBus();
	}

	@Provides
	@Singleton
	public io.vertx.rxjava3.core.eventbus.EventBus rxEventBus(io.vertx.rxjava3.core.Vertx rxVertx) {
		return rxVertx.eventBus();
	}

	@Provides
	public HttpServer httpServer(Vertx vertx, ServerOption pocOption) {
		HttpServerOptions options = new HttpServerOptions();
		options
			.setPort(SERVER_PORT)
			.setHost(SERVER_HOST)
			.setCompressionSupported(true)
			.setHandle100ContinueAutomatically(true)
			.setTcpFastOpen(true)
			.setTcpNoDelay(true)
			.setTcpQuickAck(true);
		options.setPort(pocOption.getPort());
		return vertx.createHttpServer(options);
	}

	@Provides
	public io.vertx.rxjava3.core.http.HttpServer rxHttpServer(HttpServer httpServer) {
		return new io.vertx.rxjava3.core.http.HttpServer(httpServer);
	}

	@Provides
	public Router router(Vertx vertx) {
		return Router.router(vertx);
	}

	@Provides
	public io.vertx.rxjava3.ext.web.Router rxRouter(io.vertx.rxjava3.core.Vertx rxVertx) {
		return io.vertx.rxjava3.ext.web.Router.router(rxVertx);
	}

}