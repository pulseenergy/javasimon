package org.javasimon;

import java.util.concurrent.atomic.AtomicLong;

/**
 * SimonCounter.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
final class CounterImpl extends AbstractSimon implements Counter {

	/**
	 * An internal counter.
	 */
	private final AtomicLong counter = new AtomicLong(0);

	/**
	 * A maximum tracker.
	 */
	private final AtomicLong max = new AtomicLong(0);

	/**
	 * A minimum tracker.
	 */
	private final AtomicLong min = new AtomicLong(0);

	public CounterImpl(String name) {
		super(name);
	}

	public Counter increment() {
		long val = counter.incrementAndGet();
		if (val > max.longValue()) {
			max.set(val);
		}
		return this;
	}

	public Counter decrement() {
		final long val = counter.decrementAndGet();
		if (val < min.longValue()) {
			min.set(val);
		}
		return this;
	}

	public Counter increment(long inc) {
		final long val = counter.addAndGet(inc);
		if (val > max.longValue()) {
			max.set(val);
		} else if (val < min.longValue()) {
			min.set(val);
		}
		return this;
	}

	public Counter decrement(long dec) {
		final long val = counter.addAndGet(-dec);
		if (val < min.longValue()) {
			min.set(val);
		} else if (val > max.longValue()) {
			max.set(val);
		}
		return this;
	}

	public Counter reset() {
		counter.set(0);
		max.set(0);
		min.set(0);
		return this;
	}

	public long getCounter() {
		return counter.longValue();
	}

	public long getMin() {
		return min.longValue();
	}

	public long getMax() {
		return max.longValue();
	}

	public String toString() {
		return "Simon Counter: " + super.toString() + " counter=" + counter;
	}
}
