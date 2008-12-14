package org.javasimon;

import java.util.Map;
import java.util.Collections;

/**
 * UnknownSimon represents Simon node in the hierarchy without known type. It may be replaced
 * in the hierarchy for real Simon in the future.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
final class UnknownSimon extends AbstractSimon {
	UnknownSimon(String name) {
		super(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public Simon reset() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public Map<String, String> sample(boolean reset) {
		return Collections.emptyMap();
	}

	@Override
	public String toString() {
		return "Unknown Simon: " + super.toString();
	}
}
