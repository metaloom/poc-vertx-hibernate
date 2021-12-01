package io.metaloom.poc.db.impl;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;

import javax.inject.Inject;

import org.hibernate.reactive.stage.Stage.Query;
import org.hibernate.reactive.stage.Stage.SessionFactory;

import io.metaloom.poc.db.PocUser;
import io.metaloom.poc.db.PocUserDao;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class PocUserDaoImpl extends AbstractDao implements PocUserDao {

	@Inject
	public PocUserDaoImpl(SessionFactory factory) {
		super(factory);
	}

	@Override
	public Single<? extends PocUser> createUser(String username, Consumer<PocUser> modifier) {
		return Single.defer(() -> {
			if (username == null) {
				return Single.error(new NullPointerException("Username must be set"));
			}
			PocUser user = new PocUserImpl(username);
			if (modifier != null) {
				modifier.accept(user);
			}
			CompletionStage<Void> stage = factory.withTransaction((session, tx) -> session.persist(user));
			Completable c = Completable.fromCompletionStage(stage);
			return c.toSingle(() -> user);
		});
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

	@Override
	public Observable<? extends PocUser> loadUsers() {
		CompletionStage<List<PocUserImpl>> stage = factory.withSession(session -> {
			Query<PocUserImpl> q = session.createQuery("from PocUserImpl", PocUserImpl.class);
			CompletionStage<List<PocUserImpl>> list = q.getResultList();
			return list;
		});
		return Single.fromCompletionStage(stage).flatMapObservable(list -> {
			return Observable.fromIterable(list);
		});
	}

}
