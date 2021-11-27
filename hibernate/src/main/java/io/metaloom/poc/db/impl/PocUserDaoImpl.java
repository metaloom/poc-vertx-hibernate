package io.metaloom.poc.db.impl;

import java.util.UUID;

import io.metaloom.poc.db.PocUser;
import io.metaloom.poc.db.PocUserDao;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

public class PocUserDaoImpl extends AbstractDao implements PocUserDao {

	@Override
	public Single<? extends PocUser> createUser(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Maybe<? extends PocUser> loadUser(UUID uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Completable updateUser(PocUser user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Completable deleteUser(PocUser user) {
		// TODO Auto-generated method stub
		return null;
	}

}
