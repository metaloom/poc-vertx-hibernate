package io.vertx.howtos.hr;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletionStage;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.reactive.provider.ReactivePersistenceProvider;
import org.hibernate.reactive.provider.ReactiveServiceRegistryBuilder;
import org.hibernate.reactive.stage.Stage;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.core.http.HttpServer;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.RoutingContext;
import io.vertx.rxjava3.ext.web.handler.BodyHandler;

public class RxJava3MainVerticle extends AbstractVerticle {

	private static final Logger logger = LoggerFactory.getLogger(RxJava3MainVerticle.class);

	private static Stage.SessionFactory emf; // <1>
	private HttpServer rxHttpServer;

	public static SessionFactory sessionFactory(String url, String user, String pass, boolean logEnabled, int poolSize) {
		Configuration configuration = new Configuration();
		Properties settings = new Properties();

		// DB
		settings.put(Environment.URL, url);
		settings.put(Environment.USER, user);
		settings.put(Environment.PASS, pass);
		settings.put(Environment.POOL_SIZE, poolSize);

		// Logging
		if (logEnabled) {
			settings.put(Environment.SHOW_SQL, "true");
			settings.put(Environment.FORMAT_SQL, "true");
		}
		settings.put(Environment.HIGHLIGHT_SQL, "false");

		// Control
		// settings.put(Environment.DRIVER, PgDriver.class.getName());
		// settings.put(Environment.DIALECT, "");
		settings.put(Environment.JPA_PERSISTENCE_PROVIDER, ReactivePersistenceProvider.class.getName());
		// settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
		// settings.put(Environment.HBM2DDL_AUTO, "create-drop");
		settings.put(Environment.HBM2DDL_DATABASE_ACTION, "drop-and-create");

		// Model
		configuration.setProperties(settings);
		configuration.addAnnotatedClass(Product.class);

		ServiceRegistry serviceRegistry = new ReactiveServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
		return configuration.buildSessionFactory(serviceRegistry);
	}

	@Override
	public Completable rxStart() {
		Router router = Router.router(vertx);

		HttpServerOptions options = new HttpServerOptions();
		options.setPort(8080);
		rxHttpServer = vertx.createHttpServer(options);
		BodyHandler bodyHandler = BodyHandler.create();
		router.post().handler(bodyHandler::handle);

		router.get("/products").respond(this::listProducts);
		router.get("/products/:id").respond(this::getProduct);
		router.post("/products").respond(this::createProduct);

		rxHttpServer.requestHandler(router::handle);
		return rxHttpServer.rxListen()
			.doOnSubscribe(s -> {
				logger.info("✅ Hibernate Reactive is ready");
				logger.info("✅ Server started..");
			}).ignoreElement();
	}

	@Override
	public Completable rxStop() {
		return rxHttpServer.rxClose()
			.andThen(Completable.fromAction(emf::close));
	}

	private Maybe<List<Product>> listProducts(RoutingContext ctx) {
		CompletionStage<List<Product>> stage = emf.withSession(session -> session
			.createQuery("from Product", Product.class)
			.getResultList());
		return Single.fromCompletionStage(stage).toMaybe();
	}

	private Maybe<Product> getProduct(RoutingContext ctx) {
		long id = Long.parseLong(ctx.pathParam("id"));
		CompletionStage<Product> stage = emf.withSession(session -> session
			.find(Product.class, id));
		return Single.fromCompletionStage(stage).toMaybe();
	}

	private Maybe<Product> createProduct(RoutingContext ctx) {
		Product product = ctx.getBodyAsJson().mapTo(Product.class);
		CompletionStage<Void> stage = emf.withSession(session -> session.persist(product).thenCompose(s -> session.flush()));
		return Completable.fromCompletionStage(stage).toSingle(() -> product).toMaybe();
	}

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		logger.info("🚀 Starting a PostgreSQL container");

		PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:11-alpine")
			.withDatabaseName("postgres")
			.withUsername("postgres")
			.withPassword("vertx-in-action");

		postgreSQLContainer.start();

		long tcTime = System.currentTimeMillis();

		logger.info("🚀 Starting Vert.x");

		Vertx vertx = Vertx.vertx();

		int pgPort = postgreSQLContainer.getMappedPort(5432);
		emf = sessionFactory("jdbc:postgresql://localhost:" + pgPort + "/postgres",
			"postgres",
			"vertx-in-action",
			false,
			10).unwrap(Stage.SessionFactory.class);

		DeploymentOptions options = new DeploymentOptions();
		options.setInstances(10);

		vertx.deployVerticle(RxJava3MainVerticle::new, options).subscribe(d -> {
			long vertxTime = System.currentTimeMillis();
			logger.info("✅ Deployment success");
			logger.info("💡 PostgreSQL container started in {}ms", (tcTime - startTime));
			logger.info("💡 Vert.x app started in {}ms", (vertxTime - tcTime));
		}, err -> logger.error("🔥 Deployment failure", err));
	}
}