package org.javasimon.spring;

import org.aopalliance.intercept.MethodInvocation;
import org.javasimon.aop.Monitored;
import org.springframework.aop.support.AopUtils;

/**
 * Utility class that can be used along side the {@link Monitored} annotation.
 *
 * @author Erik van Oosten
 */
final class MonitoredHelper {
	private MonitoredHelper() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Determine monitor name for a method invocation.
	 *
	 * @param invocation the method invocation (not null)
	 * @return the monitor name for this invocation
	 */
	public static String getMonitorName(MethodInvocation invocation) {
		String classPart = getClassPart(invocation);
		String methodPart = getMethodPart(invocation);
		return classPart + '.' + methodPart;
	}

	private static String getClassPart(MethodInvocation invocation) {
		Class targetClass = AopUtils.getTargetClass(invocation.getThis());
		Monitored classAnnotation = (Monitored) targetClass.getAnnotation(Monitored.class);
		if (classAnnotation == null || classAnnotation.name() == null || classAnnotation.name().length() == 0) {
			return targetClass.getName();
		} else {
			return classAnnotation.name();
		}
	}

	private static String getMethodPart(MethodInvocation invocation) {
		Monitored methodAnnotation = invocation.getMethod().getAnnotation(Monitored.class);
		if (methodAnnotation == null || methodAnnotation.name() == null || methodAnnotation.name().length() == 0) {
			return invocation.getMethod().getName();
		} else {
			return methodAnnotation.name();
		}
	}
}
