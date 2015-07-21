package net.ttddyy.dsproxy.proxy.jdk;

import net.ttddyy.dsproxy.TimeProvider;
import net.ttddyy.dsproxy.listener.QueryExecutionListener;
import net.ttddyy.dsproxy.proxy.ConnectionProxy;
import net.ttddyy.dsproxy.proxy.ConnectionProxyLogic;
import net.ttddyy.dsproxy.proxy.InterceptorHolder;
import net.ttddyy.dsproxy.proxy.JdbcProxyFactory;
import net.ttddyy.dsproxy.transform.QueryTransformer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;

/**
 * Proxy InvocationHandler for {@link java.sql.Connection}.
 *
 * @author Tadaya Tsuyukubo
 */
public class ConnectionInvocationHandler implements InvocationHandler {

    private final ConnectionProxyLogic delegate;

    public ConnectionInvocationHandler(
            Connection connection, InterceptorHolder interceptorHolder, String dataSourceName, TimeProvider timeProvider, JdbcProxyFactory jdbcProxyFactory) {
        this.delegate = new ConnectionProxyLogic(connection, interceptorHolder, dataSourceName, timeProvider, jdbcProxyFactory);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return delegate.invoke((ConnectionProxy) proxy, method, args);
    }

}
