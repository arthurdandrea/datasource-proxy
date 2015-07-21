package net.ttddyy.dsproxy.proxy.jdk;

import net.ttddyy.dsproxy.proxy.ConnectionProxy;
import net.ttddyy.dsproxy.proxy.InterceptorHolder;
import net.ttddyy.dsproxy.proxy.JdbcProxyFactory;
import net.ttddyy.dsproxy.proxy.ProxyJdbcObject;

import javax.sql.DataSource;
import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * Dynamic Proxy Class(Jdk Proxy) based {@link net.ttddyy.dsproxy.proxy.JdbcProxyFactory} implementation.
 *
 * @author Tadaya Tsuyukubo
 * @since 1.2
 */
public class JdkJdbcProxyFactory implements JdbcProxyFactory {

    public DataSource createDataSource(DataSource dataSource, InterceptorHolder interceptorHolder, String dataSourceName) {
        return (DataSource) Proxy.newProxyInstance(ProxyJdbcObject.class.getClassLoader(),
                new Class[]{ProxyJdbcObject.class, DataSource.class},
                new DataSourceInvocationHandler(dataSource, interceptorHolder, dataSourceName, this));
    }

    public ConnectionProxy createConnection(Connection connection, InterceptorHolder interceptorHolder) {
        return createConnection(connection, interceptorHolder, "");
    }

    public ConnectionProxy createConnection(Connection connection, InterceptorHolder interceptorHolder, String dataSourceName) {
        return (ConnectionProxy) Proxy.newProxyInstance(ProxyJdbcObject.class.getClassLoader(),
                new Class[]{ConnectionProxy.class},
                new ConnectionInvocationHandler(connection, interceptorHolder, dataSourceName, this));
    }

    @Override
    public Statement createStatement(Statement statement, ConnectionProxy connectionProxy) {
        return (Statement) Proxy.newProxyInstance(ProxyJdbcObject.class.getClassLoader(),
                new Class[]{ProxyJdbcObject.class, Statement.class},
                new StatementInvocationHandler(statement, connectionProxy));
    }

    @Override
    public PreparedStatement createPreparedStatement(PreparedStatement preparedStatement, String query, ConnectionProxy connectionProxy) {
        return (PreparedStatement) Proxy.newProxyInstance(ProxyJdbcObject.class.getClassLoader(),
                new Class[]{ProxyJdbcObject.class, PreparedStatement.class},
                new PreparedStatementInvocationHandler(preparedStatement, query, connectionProxy));

    }

    @Override
    public CallableStatement createCallableStatement(CallableStatement callableStatement, String query, ConnectionProxy connectionProxy) {
        return (CallableStatement) Proxy.newProxyInstance(ProxyJdbcObject.class.getClassLoader(),
                new Class[]{ProxyJdbcObject.class, CallableStatement.class},
                new CallableStatementInvocationHandler(callableStatement, query, connectionProxy));
    }
}
