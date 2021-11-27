package io.metaloom.poc.db.impl;

import java.util.UUID;
import java.util.concurrent.CompletionStage;

import org.hibernate.reactive.stage.Stage.SessionFactory;

import io.metaloom.poc.db.PocUser;
import io.metaloom.poc.db.PocUserDao;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

public class PocUserDaoImpl extends AbstractDao implements PocUserDao {

	public PocUserDaoImpl(SessionFactory factory) {
		super(factory);
	}

	@Override
	public Single<? extends PocUser> createUser(String username) {
		if (username == null) {
			return Single.error(new NullPointerException("Username must be set"));
		}
		PocUser user = new PocUserImpl(username);
		Completable c = Completable.fromCompletionStage(factory.withTransaction((session, tx) -> session.persist(user)));
		return c.toSingle(() -> user);
	}

	@Override
	public Maybe<? extends PocUser> loadUser(UUID uuid) {
		CompletionStage<PocUserImpl> stage = factory.withSession(session -> session.find(PocUserImpl.class, uuid));
		return Maybe.fromCompletionStage(stage);
	}

	@Override
	public Completable updateUser(PocUser user) {
		CompletionStage<Void> stage = factory.withSession(session -> {
			return session.persist(user);
		});
		return Completable.fromCompletionStage(stage);
	}

	@Override
	public Completable deleteUser(PocUser user) {
		CompletionStage<Void> stage = factory.withSession(session -> {
			return session.remove(user).thenAccept(v -> session.flush());
		});
		return Completable.fromCompletionStage(stage);
	}

}
