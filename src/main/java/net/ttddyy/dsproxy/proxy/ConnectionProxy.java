package net.ttddyy.dsproxy.proxy;

import java.sql.Connection;

/**
 * Created by arthur on 20/07/15.
 */
public interface ConnectionProxy extends Connection, ProxyJdbcObject, DataSourceNameAware {
    InterceptorHolder getInterceptorHolder();
}
