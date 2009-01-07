package org.javasimon;

import org.javasimon.utils.SimonUtils;

/**
 * Class implements {@link org.javasimon.Stopwatch} interface - see there for how to use Stopwatch.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 * @see org.javasimon.Stopwatch
 */
final class StopwatchImpl extends AbstractSimon implements Stopwatch {
	private long total;

	private long counter;

	private long active;

	private long max;

	private long maxTimestamp;

	private long maxActive;

	private long maxActiveTimestamp;

	private long min = Long.MAX_VALUE;

	private long minTimestamp;

	private long last;

	private long firstUsageNanos;

	private long currentNanos;

	StopwatchImpl(String name) {
		super(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Stopwatch addTime(long ns) {
		if (enabled) {
			updateUsages();
			addSplit(ns);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Split start() {
		if (enabled) {
			updateUsages();
			activeStart();
			return new Split(this, currentNanos);
		}
		return new Split(null, 0);
	}

	/**
	 * Protected method doing the stop work based on provided start nano-time.
	 *
	 * @param start start nano-time of the split
	 * @return split time in ns
	 */
	synchronized long stop(long start) {
		if (start == 0) {
			return 0;
		}
		active--;
		updateUsages();
		return addSplit(currentNanos - start);
	}

	// Uses last usage, hence it must be placed after usages update
	private void activeStart() {
		active++;
		if (active >= maxActive) {
			maxActive = active;
			maxActiveTimestamp = getLastUsage();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Stopwatch reset() {
		total = 0;
		counter = 0;
		max = 0;
		min = Long.MAX_VALUE;
		maxTimestamp = 0;
		minTimestamp = 0;
		// active is not reset, because
		maxActive = 0;
		maxActiveTimestamp = 0;
		getStatProcessor().reset();
		return this;
	}

	private long addSplit(long split) {
		last = split;
		total += split;
		counter++;
		if (split > max) {
			max = split;
			maxTimestamp = getLastUsage();
		}
		if (split < min) {
			min = split;
			minTimestamp = getLastUsage();
		}
		if (getStatProcessor() != null) {
			getStatProcessor().process(split);
		}
		return split;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized long getTotal() {
		return total;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getLast() {
		return last;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized long getCounter() {
		return counter;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized long getMax() {
		return max;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized long getMin() {
		return min;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized long getMaxTimestamp() {
		return maxTimestamp;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized long getMinTimestamp() {
		return minTimestamp;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getActive() {
		return active;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getMaxActive() {
		return maxActive;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getMaxActiveTimestamp() {
		return maxActiveTimestamp;
	}

	@Override
	public void setStatProcessor(StatProcessor statProcessor) {
		super.setStatProcessor(statProcessor);
		statProcessor.setInterpreter(StatProcessor.NanoInterpreter.INSTANCE);
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized StopwatchSample sampleAndReset() {
		StopwatchSample sample = sample();
		reset();
		return sample;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized StopwatchSample sample() {
		return new StopwatchSample(total, counter, min, max, minTimestamp, maxTimestamp,
			active, maxActive, maxActiveTimestamp, getStatProcessor());
	}

	/**
	 * Updates usage statistics.
	 */
	protected void updateUsages() {
		currentNanos = System.nanoTime();
		if (firstUsage == 0) {
			firstUsage = System.currentTimeMillis();
			firstUsageNanos = currentNanos;
		}
		lastUsage = firstUsage + (currentNanos - firstUsageNanos) / SimonUtils.NANOS_IN_MILLIS;
	}

	@Override
	public synchronized String toString() {
		return "Simon Stopwatch: " + super.toString() +
			" total " + SimonUtils.presentNanoTime(total) +
			", counter " + counter +
			", max " + SimonUtils.presentNanoTime(max) +
			", min " + SimonUtils.presentNanoTime(min) +
			(getNote() != null && getNote().length() != 0 ? ", note '" + getNote() + "'" : "");
	}
}
