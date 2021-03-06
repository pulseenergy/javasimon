package org.javasimon.jdbcx4;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;
import java.sql.SQLException;
import java.lang.reflect.Method;

/**
 * Wrapper class for real ConnectionPoolDataSource implementation, produces pooled
 * {@link javax.sql.PooledConnection} object.
 * <p/>
 * See the {@link org.javasimon.jdbcx4.SimonDataSource} for more information.
 *
 * @author Radovan Sninsky
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @version $Revision: $ $Date: $
 * @since 2.4
 */
public final class SimonConnectionPoolDataSource extends AbstractSimonDataSource implements ConnectionPoolDataSource {
	private ConnectionPoolDataSource ds;

	private ConnectionPoolDataSource datasource() throws SQLException {
		if (ds == null) {
			if (realDataSourceClassName == null || realDataSourceClassName.length() == 0) {
				throw new SQLException("Property realdatasourceclassname is not set");
			}
			Object o;
			try {
				o = Class.forName(realDataSourceClassName).newInstance();
			} catch (Exception e) {
				throw new SQLException(e.getMessage());
			}
			if (o instanceof ConnectionPoolDataSource) {
				ds = (ConnectionPoolDataSource) o;
				try {
					for (Method m : ds.getClass().getMethods()) {
						String methodName = m.getName();
						if (methodName.equalsIgnoreCase("setUser")) {
							m.invoke(ds, user);
						} else if (methodName.equalsIgnoreCase("setPassword")) {
							m.invoke(ds, password);
						} else if (methodName.equalsIgnoreCase("setUrl")) {
							m.invoke(ds, url);
						}
					}
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
				ds.setLoginTimeout(loginTimeout);
			} else {
				throw new SQLException("Class in realdatasourceclassname is not a ConnectionPoolDataSource");
			}
		}
		return ds;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PooledConnection getPooledConnection() throws SQLException {
		return new SimonPooledConnection(datasource().getPooledConnection(), prefix);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PooledConnection getPooledConnection(String user, String password) throws SQLException {
		return new SimonPooledConnection(datasource().getPooledConnection(user, password), prefix);
	}
}
