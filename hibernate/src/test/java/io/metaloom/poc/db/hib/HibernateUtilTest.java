package io.metaloom.poc.db.hib;

import org.hibernate.SessionFactory;
import org.junit.Rule;
import org.junit.Test;

import io.metaloom.poc.env.PocPostgreSQLContainer;
import io.metaloom.poc.option.DatabaseOption;

public class HibernateUtilTest {

	@Rule
	public PocPostgreSQLContainer container = new PocPostgreSQLContainer();

	@Test
	public void testCreateSessionFactory() {
		DatabaseOption options = container.getOptions();
		SessionFactory factory = HibernateUtil.sessionFactory(options.getJdbcUrl(), options.getUsername(), options.getPassword(), true);
		System.out.println(factory.getClass().getName());
	}
}
