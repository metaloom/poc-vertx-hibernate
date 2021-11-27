package io.metaloom.poc.db;

public interface PocGroup extends PocElement {

	/**
	 * Return the name of the group.
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Set the name of the group.
	 * 
	 * @param name
	 * @return
	 */
	PocGroup setName(String name);
}
