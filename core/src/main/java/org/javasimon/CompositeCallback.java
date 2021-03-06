package org.javasimon;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Composite callbacks holds child-callbacks and delegates any operations to all of them.
 * It implements {@link #callbacks()}, {@link #addCallback(Callback)} and {@link #removeCallback(Callback)}.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class CompositeCallback implements Callback {
	private List<Callback> callbacks = new CopyOnWriteArrayList<Callback>();

	private boolean initialized; // should also indicate whether this callback is joined to manager

	/**
	 * Returns the list of all child-callbacks.
	 *
	 * @return children list
	 */
	public List<Callback> callbacks() {
		return callbacks;
	}

	/**
	 * Adds another callback as a child to this callback.
	 *
	 * @param callback added callback
	 */
	public void addCallback(Callback callback) {
		if (initialized) {
			callback.initialize();
		}
		callbacks.add(callback);
	}

	/**
	 * Removes specified callback from this callback.
	 *
	 * @param callback removed child-callback
	 */
	public void removeCallback(Callback callback) {
		callbacks.remove(callback);
		if (initialized) {
			callback.cleanup();
		}
	}

	/**
	 * Calls initialize on all children.
	 */
	public void initialize() {
		initialized = true;
		for (Callback c : callbacks) {
			try {
				c.initialize();
			} catch (Exception e) {
				warning("Initialization error", e);
			}
		}
	}

	/**
	 * Calls deactivate on all children.
	 */
	public void cleanup() {
		initialized = false;
		for (Callback c : callbacks) {
			try {
				c.cleanup();
			} catch (Exception e) {
				warning("Deactivation error", e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void reset(Simon simon) {
		for (Callback c : callbacks) {
			c.reset(simon);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void stopwatchAdd(Stopwatch stopwatch, long ns) {
		for (Callback c : callbacks) {
			c.stopwatchAdd(stopwatch, ns);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void stopwatchStart(Split split) {
		for (Callback c : callbacks) {
			c.stopwatchStart(split);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void stopwatchStop(Split split) {
		for (Callback c : callbacks) {
			c.stopwatchStop(split);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void counterDecrease(Counter counter, long dec) {
		for (Callback c : callbacks) {
			c.counterDecrease(counter, dec);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void counterIncrease(Counter counter, long inc) {
		for (Callback c : callbacks) {
			c.counterIncrease(counter, inc);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void counterSet(Counter counter, long val) {
		for (Callback c : callbacks) {
			c.counterSet(counter, val);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void simonCreated(Simon simon) {
		for (Callback c : callbacks) {
			c.simonCreated(simon);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void simonDestroyed(Simon simon) {
		for (Callback c : callbacks) {
			c.simonDestroyed(simon);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void clear() {
		for (Callback c : callbacks) {
			c.clear();
		}
	}


	/**
	 * {@inheritDoc}
	 */
	public void message(String message) {
		for (Callback c : callbacks) {
			c.message(message);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void warning(String warning, Exception cause) {
		for (Callback c : callbacks) {
			c.warning(warning, cause);
		}
	}
}