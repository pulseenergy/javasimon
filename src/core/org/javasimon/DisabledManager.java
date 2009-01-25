package org.javasimon;

import java.util.Collection;
import java.util.Collections;

/**
 * DisabledManager implements methods called from SimonManager to do nothing or return NullSimon.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 16, 2008
 */
public final class DisabledManager implements Manager {
	/**
	 * {@inheritDoc}
	 */
	public Simon getSimon(String name) {
		return NullSimon.INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 */
	public void destroySimon(String name) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void clear() {
	}

	/**
	 * {@inheritDoc}
	 */
	public void installCallback(Callback callback) {
	}

	/**
	 * {@inheritDoc}
	 */
	public Callback callback() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public ManagerConfiguration configuration() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Counter getCounter(String name) {
		return NullSimon.INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 */
	public Stopwatch getStopwatch(String name) {
		return NullSimon.INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 */
	public Simon getRootSimon() {
		return NullSimon.INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<String> simonNames() {
		return Collections.emptySet();
	}
}
