package io.metaloom.poc.db;

import java.util.UUID;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;


public interface PocGroupDao {

	/**
	 * Create a new group.
	 * 
	 * @param name
	 * @return
	 */
	Single<? extends PocGroup> createGroup(String name);

	/**
	 * Delete the group with the uuid.
	 * 
	 * @param uuid
	 * @return
	 */
	Completable deleteGroup(UUID uuid);

	/**
	 * Update the group element.
	 * 
	 * @param group
	 * @return
	 */
	Completable updateGroup(PocGroup group);

	/**
	 * Load the group with the given uuid.
	 * 
	 * @param uuid
	 * @return
	 */
	Maybe<? extends PocGroup> loadGroup(UUID uuid);

	/**
	 * Add the given user to the group.
	 * 
	 * @param group
	 * @param user
	 * @return
	 */
	Completable addUserToGroup(PocGroup group, PocUser user);

	/**
	 * Remove the given user from the group.
	 * 
	 * @param group
	 * @param user
	 * @return
	 */
	Completable removeUserFromGroup(PocGroup group, PocUser user);

	/**
	 * Load all users that assigned to the group.
	 * 
	 * @param group
	 * @return
	 */
	Observable<PocUser> loadUsers(PocGroup group);

	/**
	 * Invoke a transactional operation which modifies multiple tables.
	 * 
	 * @return
	 */
	Observable<PocUser> addTwoUsers();

}
