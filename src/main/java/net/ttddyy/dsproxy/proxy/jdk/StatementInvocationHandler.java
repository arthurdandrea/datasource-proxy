package net.ttddyy.dsproxy.proxy.jdk;

import net.ttddyy.dsproxy.proxy.ConnectionProxy;
import net.ttddyy.dsproxy.proxy.StatementProxyLogic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Statement;

/**
 * Proxy InvocationHandler for {@link java.sql.Statement}.
 *
 * @author Tadaya Tsuyukubo
 */
public class StatementInvocationHandler implements InvocationHandler {
    private final StatementProxyLogic delegate;

    public StatementInvocationHandler(Statement stmt, ConnectionProxy connectionProxy) {
        delegate = new StatementProxyLogic(stmt, connectionProxy);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return delegate.invoke(method, args);
    }
}
