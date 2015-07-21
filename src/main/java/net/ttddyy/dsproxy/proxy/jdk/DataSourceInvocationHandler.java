package net.ttddyy.dsproxy.proxy.jdk;

import net.ttddyy.dsproxy.proxy.DataSourceProxyLogic;
import net.ttddyy.dsproxy.proxy.InterceptorHolder;
import net.ttddyy.dsproxy.proxy.JdbcProxyFactory;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Proxy InvocationHandler for {@link javax.sql.DataSource}.
 *
 * @author Tadaya Tsuyukubo
 */
public class DataSourceInvocationHandler implements InvocationHandler {

    private final DataSourceProxyLogic delegate;

    public DataSourceInvocationHandler(DataSource dataSource, InterceptorHolder interceptorHolder, String dataSourceName,
                                       JdbcProxyFactory jdbcProxyFactory) {
        delegate = new DataSourceProxyLogic(dataSource, interceptorHolder, dataSourceName, jdbcProxyFactory);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return delegate.invoke(method, args);
    }
}
