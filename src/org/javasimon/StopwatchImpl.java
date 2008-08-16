package org.javasimon;

import org.javasimon.utils.SimonUtils;

/**
 * Simonatch.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
final class StopwatchImpl extends AbstractSimon implements Stopwatch {
	private long total = 0;

	private long counter = 0;

	private ThreadLocal<Long> start = new ThreadLocal<Long>();

	private long max = 0;

	private long min = Long.MAX_VALUE;

	public StopwatchImpl(String name) {
		super(name);
	}

	public synchronized Stopwatch addTime(long ns) {
		if (enabled) {
			addSplit(ns);
		}
		return this;
	}

	public synchronized Stopwatch reset() {
		total = 0;
		counter = 0;
		max = 0;
		min = Long.MAX_VALUE;
		start = new ThreadLocal<Long>();
		return this;
	}

	public Stopwatch start() {
		if (enabled) {
			start.set(System.nanoTime());
		}
		return this;
	}

	public long stop() {
		if (enabled) {
			Long end = start.get();
			if (end != null) {
				return addSplit(System.nanoTime() - end);
			}
		}
		return 0;
	}

	private synchronized long addSplit(long split) {
		total += split;
		counter++;
		if (split > max) {
			max = split;
		}
		if (split < min) {
			min = split;
		}
		return split;
	}

	public long getTotal() {
		return total;
	}

	public long getCounter() {
		return counter;
	}

	public long getMax() {
		return max;
	}

	public long getMin() {
		return min;
	}

	protected void enabledObserver() {
		start = new ThreadLocal<Long>();
	}

	public String toString() {
		return "Simon Stopwatch: " + super.toString() +
			" total " + SimonUtils.presentNanoTime(total) +
			", counter " + counter +
			", max " + SimonUtils.presentNanoTime(max) +
			", min " + SimonUtils.presentNanoTime(min) +
			(getNote() != null && !getNote().isEmpty() ? ", note '" + getNote() + "'" : "");
	}
}