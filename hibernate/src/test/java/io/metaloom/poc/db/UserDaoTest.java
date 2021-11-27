package io.metaloom.poc.db;

import static javax.persistence.Persistence.createEntityManagerFactory;

import org.hibernate.reactive.mutiny.Mutiny.SessionFactory;
import org.junit.Test;

import io.metaloom.poc.db.impl.PocGroupImpl;
import io.metaloom.poc.db.impl.PocUserDaoImpl;

public class UserDaoTest extends AbstractDaoTest {

	@Test
	public void testRxHibernate() {

		// obtain a factory for reactive sessions based on the
		// standard JPA configuration properties specified in
		// resources/META-INF/persistence.xml
		String[] args = {};
		SessionFactory factory = createEntityManagerFactory(persistenceUnitName(args)).unwrap(SessionFactory.class);

		PocGroup group1 = new PocGroupImpl("guests");
		PocGroup group2 = new PocGroupImpl("admins");
		PocGroup group3 = new PocGroupImpl("editors");

		PocUserDao userDao = new PocUserDaoImpl();
//		PocUser user1 = userDao.createUser("Iain M. Banks");
//		PocUser user2 = new PocUserImpl("Neal Stephenson");
//		PocUser user3 = new PocUserImpl("Arthur C. Clarke");

//		PocGroupDao groupDao = new PocGroupDaoImpl();
//		groupDao.addUserToGroup(group1, user1);
//		groupDao.addUserToGroup(group1, user2);

		/**
		 * // obtain a reactive session factory.withTransaction( // persist the Authors with their Books in a transaction (session, tx) ->
		 * session.persistAll(author1, author2)) // wait for it to finish .await().indefinitely();
		 * 
		 * factory.withSession( // retrieve a Book session -> session.find(PocUser.class, book1.getId()) // print its title .invoke(book ->
		 * out.println(book.getTitle() + " is a great book!"))) .await().indefinitely();
		 * 
		 * factory.withSession( // retrieve both Authors at once session -> session.find(PocGroup.class, author1.getId(), author2.getId()) .invoke(authors ->
		 * authors.forEach(author -> out.println(author.getName())))) .await().indefinitely();
		 * 
		 * factory.withSession( // retrieve an Author session -> session.find(PocUser.class, author2.getId()) // lazily fetch their books .chain(author ->
		 * fetch(author.getBooks()) // print some info .invoke(books -> { out.println(author.getName() + " wrote " + books.size() + " books");
		 * books.forEach(book -> out.println(book.getTitle())); }))) .await().indefinitely();
		 * 
		 * factory.withSession( // query the Book titles session -> session .createQuery("select title, author.name from Book order by title desc",
		 * Object[].class) .getResultList().invoke(rows -> rows.forEach(row -> out.printf("%s (%s)\n", row[0], row[1])))) .await().indefinitely();
		 * 
		 * factory.withSession( // query the entire Book entities session -> session .createQuery("from Book book join fetch book.author order by book.title
		 * desc", Book.class) .getResultList() .invoke(books -> books.forEach( b -> out.printf("%s: %s (%s)\n", b.getIsbn(), b.getTitle(),
		 * b.getAuthor().getName())))) .await().indefinitely();
		 * 
		 * factory.withSession( // use a criteria query session -> { CriteriaQuery<Book> query = factory.getCriteriaBuilder().createQuery(Book.class);
		 * Root<Author> a = query.from(Author.class); Join<Author, Book> b = a.join(Author_.books); query.where(a.get(Author_.name).in("Neal Stephenson",
		 * "William Gibson")); query.select(b); return session.createQuery(query).getResultList() .invoke(books -> books.forEach(book ->
		 * out.println(book.getTitle()))); }).await().indefinitely();
		 * 
		 * factory.withSession( // retrieve a Book session -> session.find(Book.class, book1.getId()) // fetch a lazy field of the Book .chain(book ->
		 * session.fetch(book, Book_.published) // print the lazy field .invoke(published -> out.printf("'%s' was published in %d\n", book.getTitle(),
		 * published.getYear())))) .await().indefinitely();
		 * 
		 * factory.withTransaction( // retrieve a Book (session, tx) -> session.find(Book.class, book2.getId()) // delete the Book .chain(book ->
		 * session.remove(book))) .await().indefinitely();
		 * 
		 * factory.withTransaction( // delete all the Books in a transaction (session, tx) -> session.createQuery("delete Book").executeUpdate() // delete all
		 * the Authors .call(() -> session.createQuery("delete Author").executeUpdate())) .await().indefinitely();
		 * 
		 * // remember to shut down the connection pool factory.close();
		 */
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
