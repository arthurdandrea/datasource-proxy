package net.ttddyy.dsproxy.proxy.jdk;

import net.ttddyy.dsproxy.proxy.ConnectionProxy;
import net.ttddyy.dsproxy.proxy.InterceptorHolder;
import net.ttddyy.dsproxy.proxy.JdbcProxyFactory;
import net.ttddyy.dsproxy.proxy.PreparedStatementProxyLogic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.CallableStatement;

/**
 * Proxy InvocationHandler for {@link java.sql.CallableStatement}.
 *
 * @author Tadaya Tsuyukubo
 */
public class CallableStatementInvocationHandler implements InvocationHandler {

    private final PreparedStatementProxyLogic delegate;

    public CallableStatementInvocationHandler(CallableStatement cs, String query, ConnectionProxy connectionProxy) {
        delegate = new PreparedStatementProxyLogic(cs, query, connectionProxy);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return delegate.invoke(method, args);
    }

}
