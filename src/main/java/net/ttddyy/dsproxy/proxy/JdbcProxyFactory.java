package net.ttddyy.dsproxy.proxy;

import net.ttddyy.dsproxy.TimeProvider;
import net.ttddyy.dsproxy.proxy.jdk.JdkJdbcProxyFactory;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * Factory interface to return a proxy with InvocationHandler used by datasource-proxy.
 *
 * @author Tadaya Tsuyukubo
 */
public interface JdbcProxyFactory {

    /**
     * use JDK proxy as default.
     */
    static final JdbcProxyFactory DEFAULT = new JdkJdbcProxyFactory();

    DataSource createDataSource(DataSource dataSource, InterceptorHolder interceptorHolder, String dataSourceName);

    ConnectionProxy createConnection(Connection connection, InterceptorHolder interceptorHolder);

    ConnectionProxy createConnection(Connection connection, InterceptorHolder interceptorHolder, String dataSourceName);

    ConnectionProxy createConnection(Connection connection, InterceptorHolder interceptorHolder, String dataSourceName, TimeProvider timeProvider);

    Statement createStatement(Statement statement, ConnectionProxy connectionProxy);

    PreparedStatement createPreparedStatement(PreparedStatement preparedStatement, String query, ConnectionProxy connectionProxy);

    CallableStatement createCallableStatement(CallableStatement callableStatement, String query, ConnectionProxy connectionProxy);
}
