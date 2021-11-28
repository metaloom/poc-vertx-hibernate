package io.metaloom.poc.db;

import org.hibernate.reactive.session.impl.ReactiveSessionFactoryImpl;
import org.hibernate.reactive.stage.Stage.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.metaloom.poc.db.hib.HibernateUtil;
import io.metaloom.poc.db.impl.PocUserDaoImpl;
import io.metaloom.poc.option.DatabaseOptions;

public class UserDaoTest extends AbstractDaoTest {

	private SessionFactory factory;

	@Before
	public void setupHibernate() {
		DatabaseOptions options = container.getOptions();
		ReactiveSessionFactoryImpl rxFactory = (ReactiveSessionFactoryImpl) HibernateUtil.sessionFactory(options.getJdbcUrl(), options.getUsername(),
			options.getPassword(), true);
		factory = rxFactory.unwrap(SessionFactory.class);
	}

	@After
	public void cleanup() {
		if (factory != null) {
			factory.close();
		}
	}

	@Test
	public void testUserDao() {
		PocUserDao userDao = new PocUserDaoImpl(factory);
		PocUser user1 = userDao.createUser("Iain M. Banks").blockingGet();
		// PocUser user2 = new PocUserImpl("Neal Stephenson");
		// PocUser user3 = new PocUserImpl("Arthur C. Clarke");
		// groupDao.addUserToGroup(group1, user1);
		// groupDao.addUserToGroup(group1, user2);
	}

	// @Test
	// public void testUserGroup() {
	// PocGroupDao groupDao = new PocGroupDaoImpl(factory);
	// PocGroup group1 = groupDao.createGroup("guests").blockingGet();
	// PocGroup group2 = groupDao.createGroup("admins").blockingGet();
	// PocGroup group3 = groupDao.createGroup("editors").blockingGet();
	// }

}
