package org.javasimon.jmx;

import org.javasimon.Stopwatch;
import org.javasimon.Simon;

/**
 * StopwatchMXBeanImpl.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Mar 6, 2009
 */
public class StopwatchMXBeanImpl extends SimonSuperMXBeanImpl implements StopwatchMXBean {
	private Stopwatch stopwatch;

	public StopwatchMXBeanImpl(Stopwatch stopwatch) {
		this.stopwatch = stopwatch;
	}

	public void addTime(long ns) {
	}

	public long getLast() {
		return stopwatch.getLast();
	}

	@Override
	public StopwatchSample sample() {
		return new StopwatchSample((org.javasimon.StopwatchSample) stopwatch.sample());
	}

	@Override
	public StopwatchSample sampleAndReset() {
		return new StopwatchSample((org.javasimon.StopwatchSample) stopwatch.sampleAndReset());
	}

	protected Simon simon() {
		return stopwatch;
	}

	public String getType() {
		return "Stopwatch";
	}
}
