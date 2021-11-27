package io.metaloom.poc.db;

import static javax.persistence.Persistence.createEntityManagerFactory;

import org.hibernate.reactive.stage.Stage.SessionFactory;
import org.junit.Test;

import io.metaloom.poc.db.impl.PocGroupDaoImpl;
import io.metaloom.poc.db.impl.PocUserDaoImpl;

public class UserDaoTest extends AbstractDaoTest {

	@Test
	public void testUserDao() {

		// obtain a factory for reactive sessions based on the
		// standard JPA configuration properties specified in
		// resources/META-INF/persistence.xml
		String[] args = {};
		SessionFactory factory = createEntityManagerFactory(persistenceUnitName(args)).unwrap(SessionFactory.class);

		PocUserDao userDao = new PocUserDaoImpl(factory);
		PocUser user1 = userDao.createUser("Iain M. Banks").blockingGet();
		// PocUser user2 = new PocUserImpl("Neal Stephenson");
		// PocUser user3 = new PocUserImpl("Arthur C. Clarke");

		// groupDao.addUserToGroup(group1, user1);
		// groupDao.addUserToGroup(group1, user2);

	}

	@Test
	public void testUserGroup() {
		String[] args = {};
		SessionFactory factory = createEntityManagerFactory(persistenceUnitName(args)).unwrap(SessionFactory.class);
		PocGroupDao groupDao = new PocGroupDaoImpl(factory);
		PocGroup group1 = groupDao.createGroup("guests").blockingGet();
		PocGroup group2 = groupDao.createGroup("admins").blockingGet();
		PocGroup group3 = groupDao.createGroup("editors").blockingGet();
	}

	/**
	 * Return the persistence unit name to use in the example.
	 *
	 * @param args
	 *            the first element is the persistence unit name if present
	 * @return the selected persistence unit name or the default one
	 */
	public static String persistenceUnitName(String[] args) {
		return args.length > 0 ? args[0] : "postgresql-example";
	}
}
