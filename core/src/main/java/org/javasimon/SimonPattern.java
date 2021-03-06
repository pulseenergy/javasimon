package org.javasimon;

import org.javasimon.utils.SimonUtils;

/**
 * Matches Simon name patterns from configuration. Patterns can contain wildcard (*) that
 * can be placed in following fashions:
 * <li>{@code *something} - matches if tested name ends with {@code something}
 * <li>{@code something*} - matches if tested name starts with {@code something}
 * <li>{@code *something*} - matches if tested name contains {@code something} anywhere
 * <li>{@code something*else} - matches if tested name starts with {@code something} and ends with {@code else}
 * <p/>
 * Without wildcard exact match is required. Every wildcarded pattern always matches with the same string
 * without wildcards (in other words - wildcards matches with nothing as well).
 */
public class SimonPattern {
	private static final String WILDCARD_STAR = "*";

	/**
	 * Original pattern from the configuration.
	 */
	private String pattern;

	/**
	 * Used if complete match is expected.
	 */
	private String all;

	/**
	 * Used if head should match.
	 */
	private String start;

	/**
	 * Used if tail should match.
	 */
	private String end;

	/**
	 * Used if anything inside (or everything) should match.
	 */
	private String middle;

	private static final String INVALID_PATTERN = "Invalid configuration pattern: ";

	/**
	 * Creates Simon name pattern used to match config file entries.
	 *
	 * @param pattern Simon name pattern
	 * @throws SimonException if pattern is not valid (runtime exception)
	 */
	SimonPattern(String pattern) {
		this.pattern = pattern;
		if (!pattern.contains(WILDCARD_STAR)) {
			// no wildcard, we're going for complete match (all)
			all = pattern;
			if (!SimonUtils.checkName(all)) {
				throw new SimonException(INVALID_PATTERN + pattern);
			}
			return;
		}
		if (pattern.equals(WILDCARD_STAR)) {
			middle = ""; // everything contains this
			return;
		}
		if (pattern.startsWith(WILDCARD_STAR) && pattern.endsWith(WILDCARD_STAR)) {
			middle = pattern.substring(1, pattern.length() - 2);
			if (!SimonUtils.checkName(middle)) {
				throw new SimonException(INVALID_PATTERN + pattern);
			}
			return;
		}
		int ix = pattern.lastIndexOf('*');
		if (ix != pattern.indexOf('*')) {
			throw new SimonException(INVALID_PATTERN + pattern);
		}
		if (!pattern.endsWith(WILDCARD_STAR)) {
			end = pattern.substring(ix + 1);
			if (!SimonUtils.checkName(end)) {
				throw new SimonException(INVALID_PATTERN + pattern);
			}
		}
		if (!pattern.startsWith(WILDCARD_STAR)) {
			start = pattern.substring(0, ix);
			if (!SimonUtils.checkName(start)) {
				throw new SimonException(INVALID_PATTERN + pattern);
			}
		}
	}

	/**
	 * Checks if Simon name matches this pattern.
	 *
	 * @param name tested name
	 * @return true if tested pattern matches this pattern
	 */
	public boolean matches(String name) {
		if (name == null) {
			return pattern.equals("");
		}
		if (all != null) {
			return all.equals(name);
		}
		if (middle != null) {
			return name.contains(middle);
		}
		if (start != null && !name.startsWith(start)) {
			return false;
		}
		return end == null || name.endsWith(end);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		SimonPattern that = (SimonPattern) o;

		return !(pattern != null ? !pattern.equals(that.pattern) : that.pattern != null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return (pattern != null ? pattern.hashCode() : 0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "ConfigPattern: " + pattern;
	}
}
