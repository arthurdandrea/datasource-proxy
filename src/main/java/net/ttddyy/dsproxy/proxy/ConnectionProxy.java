package net.ttddyy.dsproxy.proxy;

import net.ttddyy.dsproxy.TimeProvider;

import java.sql.Connection;

/**
 * Created by arthur on 20/07/15.
 */
public interface ConnectionProxy extends Connection, ProxyJdbcObject, DataSourceNameAware {
    InterceptorHolder getInterceptorHolder();
    TimeProvider getTimeProvider();
}
