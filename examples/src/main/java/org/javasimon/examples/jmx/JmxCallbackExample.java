package org.javasimon.examples.jmx;

import org.javasimon.*;
import org.javasimon.jmx.JmxRegisterCallback;

/**
 * JmxCallbackExample demonstrates {@link JmxRegisterCallback} in action. It creates one Counter
 * and one Stopwatch that can be monitored via {@code jconsole} or any other/custom JMX client.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class JmxCallbackExample {
	/**
	 * Entry point to the JMX Callback Example.
	 *
	 * @param args unused
	 * @throws Exception whatever may happen in this crazy world
	 */
	public static void main(String[] args) throws Exception {
		SimonManager.callback().addCallback(new JmxRegisterCallback());

		Counter counter = SimonManager.getCounter("org.javasimon.examples.jmx.counter");
		Stopwatch stopwatch = SimonManager.getStopwatch("org.javasimon.examples.jmx.stopwatch");
		System.out.println("Now open jconsole and check it out! :-)");
		while (true) {
			counter.increase();
			Split split = stopwatch.start();
			Thread.sleep(500);
			split.stop();
		}
	}
}
