package io.metaloom.poc.db;

import java.util.UUID;

public interface PocElement {

	/**
	 * Return the UUID of the element.
	 * 
	 * @return
	 */
	UUID getUuid();

	/**
	 * Set the UUID of the element.
	 * 
	 * @param uuid
	 * @return Fluent API
	 */
	PocElement setUuid(UUID uuid);
}
