package io.metaloom.poc.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.UUID;

import org.hibernate.reactive.session.impl.ReactiveSessionFactoryImpl;
import org.hibernate.reactive.stage.Stage.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.metaloom.poc.db.hib.HibernateUtil;
import io.metaloom.poc.db.impl.PocGroupDaoImpl;
import io.metaloom.poc.db.impl.PocUserDaoImpl;
import io.metaloom.poc.option.DatabaseOption;

public class UserDaoTest extends AbstractDaoTest {

	private SessionFactory factory;
	private PocUserDao userDao;
	private PocGroupDao groupDao;

	@Before
	public void setupHibernate() {
		DatabaseOption options = container.getOptions();
		ReactiveSessionFactoryImpl rxFactory = (ReactiveSessionFactoryImpl) HibernateUtil.sessionFactory(options.getJdbcUrl(), options.getUsername(),
			options.getPassword(), true, 4);
		factory = rxFactory.unwrap(SessionFactory.class);
		userDao = new PocUserDaoImpl(factory);
		groupDao = new PocGroupDaoImpl(factory);
	}

	@After
	public void cleanup() {
		if (factory != null) {
			factory.close();
		}
	}

	@Test
	public void testUserDao() {
		PocUser user = userDao.createUser("Iain M. Banks", u -> {
			u.setEmail("ABCD");
		}).blockingGet();

		assertNotNull(user);
		assertEquals("ABCD", user.getEmail());
	}

	@Test
	public void testCreateWithUuid() {
		String userUUID = "44dee6f7-879c-4c2b-89e1-462e19c99708";
		PocUser user = userDao.createUser("Iain M. Banks", u -> {
			u.setEmail("ABCD");
			u.setUuid(UUID.fromString(userUUID));
		}).blockingGet();
		assertEquals(userUUID, user.getUuid().toString());
	}

	@Test
	public void testUserDaoLoad() {

		// 1. Create user
		String userUUID = "44dee6f7-879c-4c2b-89e1-462e19c99708";
		PocUser user = userDao.createUser("Iain M. Banks", u -> {
			u.setEmail("ABCD");
			u.setUuid(UUID.fromString(userUUID));
		}).blockingGet();

		// for (int i = 0; i < 10_000; i++) {
		// userDao.loadUser(user.getUuid()).subscribe(s -> {
		// System.out.println("Loaded " + s.getUuid());
		// }, err -> {
		// err.printStackTrace();
		// });
		// }

		for (int i = 0; i < 10_000; i++) {
			try {
				userDao.loadUser(user.getUuid()).blockingGet();
			} catch (Exception e) {
				e.printStackTrace();
				fail("Failed after " + i + " runs");
			}
		}
	}

	@Test
	public void testUserGroup() {
		PocGroup group = groupDao.createGroup("guests").blockingGet();
		assertNotNull(group);
	}

}
