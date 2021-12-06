package io.metaloom.poc.db.impl;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import javax.inject.Inject;

import org.hibernate.reactive.mutiny.Mutiny;

import io.metaloom.poc.db.PocUser;
import io.metaloom.poc.db.PocUserDao;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.smallrye.mutiny.Uni;

public class PocUserDaoImpl extends AbstractDao implements PocUserDao {

	@Inject
	public PocUserDaoImpl(Mutiny.SessionFactory factory) {
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
			Uni<PocUser> uni = factory.withSession(session -> session.persist(user).call(session::flush)).replaceWith(user);
			return Single.fromCompletionStage(uni.subscribeAsCompletionStage());
		});
	}

	@Override
	public Maybe<? extends PocUser> loadUser(UUID uuid) {
		Uni<PocUserImpl> uni = factory.withSession(session -> session.find(PocUserImpl.class, uuid));
		return Maybe.fromCompletionStage(uni.subscribeAsCompletionStage());
	}

	@Override
	public Completable updateUser(PocUser user) {
		Uni<Void> uni = factory.withSession(session -> {
			return session.persist(user).call(session::flush);
		});
		return Completable.fromCompletionStage(uni.subscribeAsCompletionStage());
	}

	@Override
	public Completable deleteUser(PocUser user) {
		Uni<Void> uni = factory.withSession(session -> {
			return session.remove(user).call(session::flush);
		});
		return Completable.fromCompletionStage(uni.subscribeAsCompletionStage());
	}

	@Override
	public Observable<? extends PocUser> loadUsers() {
		Uni<List<PocUserImpl>> uni = factory.withSession(session -> {
			return session.createQuery("from PocUserImpl", PocUserImpl.class)
				.getResultList();
		});
		return Single.fromCompletionStage(uni.subscribeAsCompletionStage()).flatMapObservable(list -> {
			return Observable.fromIterable(list);
		});
	}

}
