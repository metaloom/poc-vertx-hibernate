package io.metaloom.poc.db.impl;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.hibernate.reactive.mutiny.Mutiny;

import io.metaloom.poc.db.PocGroup;
import io.metaloom.poc.db.PocGroupDao;
import io.metaloom.poc.db.PocUser;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.smallrye.mutiny.Uni;

public class PocGroupDaoImpl extends AbstractDao implements PocGroupDao {

	@Inject
	public PocGroupDaoImpl(Mutiny.SessionFactory factory) {
		super(factory);
	}

	@Override
	public Single<? extends PocGroup> createGroup(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Completable deleteGroup(UUID uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Completable updateGroup(PocGroup group) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Maybe<? extends PocGroup> loadGroup(UUID uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Completable addUserToGroup(PocGroup group, PocUser user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Completable removeUserFromGroup(PocGroup group, PocUser user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Observable<PocUser> loadUsers(PocGroup group) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Observable<PocUser> addTwoUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Observable<? extends PocGroup> loadGroups() {
		Uni<List<PocGroupImpl>> stage = factory.withSession(session -> {
			return session.createQuery("from PocGroupImpl", PocGroupImpl.class)
				.getResultList();
		});
		return Single.fromCompletionStage(stage.subscribeAsCompletionStage())
			.flatMapObservable(list -> {
				return Observable.fromIterable(list);
			});
	}

}
