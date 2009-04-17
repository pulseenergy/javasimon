package org.javasimon.jmx;

import java.util.Date;
import java.beans.ConstructorProperties;

/**
 * Object is used as transfer object for jdbc mbean. Object holds data for jdbc runtime objects: connection, statements
 * and result sets. These objects has same data structure, so it's used for transfering data from jdbc javasimon hierarchy
 * through jmx.
 *
 * @author Radovan Sninsky
 * @version $Revision$ $Date$
 * @created 9.1.2009 15:08:50
 * @since 2
 */
public class JdbcObjectInfo {

	private long active;
	private long peak;
	private Date peakTime;
	private long opened;
	private long closed;
	private long min;
	private long max;
	private long total;

	/**
	 * Class constructor. Constructor is used on both side, in server and also in client code to initialize all properties
	 * of object. On client side constructor is used by jmx internal mechanism to initialize object from composite data object.
	 *
	 * @param active actual active objects (conn, stmt, rset) in runtime
	 * @param peak max active count
	 * @param peakTime time when max active count occured
	 * @param opened count of opened objects (conn, stmt, rset)
	 * @param closed count of closed objects (conn, stmt, rset)
	 * @param min minimal lifetime of objects (conn, stmt, rset)
	 * @param max maximum lifetime of objects (conn, stmt, rset)
	 * @param total sum of all lifetimes of object (conn, stmt, rset)
	 */
	@ConstructorProperties({"active", "peak", "peakTime", "opened", "closed", "min", "max", "total"})
	public JdbcObjectInfo(long active, long peak, Date peakTime, long opened, long closed, long min, long max, long total) {
		this.active = active;
		this.peak = peak;
		this.peakTime = peakTime;
		this.opened = opened;
		this.closed = closed;
		this.min = min;
		this.max = max;
		this.total = total;
	}

	/**
	 * Getter for actual active objects (conn, stmt, rset) in runtime.
	 *
	 * @return active object
	 */
	public long getActive() {
		return active;
	}

	/**
	 * Getter for max active count.
	 *
	 * @return max active count
	 */
	public long getPeak() {
		return peak;
	}

	/**
	 * Getter for peak time.
	 *
	 * @return time when max active count occured
	 */
	public Date getPeakTime() {
		return peakTime;
	}

	/**
	 * Getter for count of opened objects (conn, stmt, rset).
	 *
	 * @return count of opened objects (conn, stmt, rset)
	 */
	public long getOpened() {
		return opened;
	}

	/**
	 * Getter for count of closed objects (conn, stmt, rset).
	 *
	 * @return count of closed objects (conn, stmt, rset)
	 */
	public long getClosed() {
		return closed;
	}

	/**
	 * Getter for minimal lifetime of objects (conn, stmt, rset).
	 *
	 * @return minimal lifetime of objects (conn, stmt, rset)
	 */
	public long getMin() {
		return min;
	}

	/**
	 * Getter for maximum lifetime of objects (conn, stmt, rset).
	 *
	 * @return maximum lifetime of objects (conn, stmt, rset)
	 */
	public long getMax() {
		return max;
	}

	/**
	 * Getter for sum of all lifetimes of object (conn, stmt, rset)
	 *
	 * @return sum of all lifetimes of object (conn, stmt, rset)
	 */
	public long getTotal() {
		return total;
	}
}
