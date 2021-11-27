package io.metaloom.poc.db;

public interface PocUser extends PocElement {

	/**
	 * Return the username.
	 * 
	 * @return
	 */
	String getUsername();

	/**
	 * Set the username.
	 * 
	 * @param username
	 * @return Fluent API
	 */
	PocUser setUsername(String username);

	/**
	 * Return the stored password hash for the user.
	 * 
	 * @return
	 */
	String getPasswordHash();

	/**
	 * Set the password hash.
	 * 
	 * @param hash
	 * @return Fluent API
	 */
	PocUser setPasswordHash(String hash);

	/**
	 * Check whether the user is enabled.
	 * 
	 * @return
	 */
	boolean isEnabled();

	/**
	 * Set the enabled flag for the user.
	 * 
	 * @param flag
	 * @return Fluent API
	 */
	PocUser setEnabled(boolean flag);

	/**
	 * Enable the user. This will enable login.
	 * 
	 * @return
	 */
	default PocUser enable() {
		setEnabled(true);
		return this;
	}

	/**
	 * Disable the user. This will prevent login.
	 * 
	 * @return
	 */
	default PocUser disable() {
		setEnabled(false);
		return this;
	}

	/**
	 * Return the email of the user.
	 * 
	 * @return
	 */
	String getEmail();

	/**
	 * Set the email of the user
	 * 
	 * @param email
	 * @return Fluent API
	 */
	PocUser setEmail(String email);

	/**
	 * Return the firstname of the user.
	 * 
	 * @return
	 */
	String getFirstname();

	/**
	 * Set the firstname of the user.
	 * 
	 * @param firstname
	 * @return Fluent API
	 */
	PocUser setFirstname(String firstname);

	/**
	 * Return the lastname of the user.
	 * 
	 * @return
	 */
	String getLastname();

	/**
	 * Set the lastname of the user.
	 * 
	 * @param lastname
	 * @return Fluent API.
	 */
	PocUser setLastname(String lastname);

}
