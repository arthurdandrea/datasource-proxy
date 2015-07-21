package net.ttddyy.dsproxy.proxy;

import net.ttddyy.dsproxy.ExecutionInfoBuilder;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.TimeProvider;
import net.ttddyy.dsproxy.listener.QueryExecutionListener;
import net.ttddyy.dsproxy.transform.QueryTransformer;
import net.ttddyy.dsproxy.transform.TransformInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Proxy Logic implementation for {@link Statement} methods.
 *
 * @author Tadaya Tsuyukubo
 * @since 1.2
 */
public class StatementProxyLogic {

    private static final Set<String> METHODS_TO_INTERCEPT = Collections.unmodifiableSet(
            new HashSet<String>() {
                {
                    addAll(StatementMethodNames.BATCH_PARAM_METHODS);
                    addAll(StatementMethodNames.EXEC_METHODS);
                    addAll(StatementMethodNames.JDBC4_METHODS);
                    addAll(StatementMethodNames.GET_CONNECTION_METHOD);
                    add("getDataSourceName");
                    add("toString");
                    add("getTarget"); // from ProxyJdbcObject
                }
            }
    );

    private final Statement stmt;
    private final ConnectionProxy connectionProxy;
    private final InterceptorHolder interceptorHolder;
    private final String dataSourceName;
    private final List<String> batchQueries = new ArrayList<String>();

    public StatementProxyLogic(Statement stmt, ConnectionProxy connectionProxy) {
        this.stmt = stmt;
        this.connectionProxy = connectionProxy;
        interceptorHolder = connectionProxy.getInterceptorHolder();
        dataSourceName = connectionProxy.getDataSourceName();
    }

    public Object invoke(Method method, Object[] args) throws Throwable {

        final String methodName = method.getName();

        if (!METHODS_TO_INTERCEPT.contains(methodName)) {
            return MethodUtils.proceedExecution(method, stmt, args);
        }

        // special treat for toString method
        if ("toString".equals(methodName)) {
            final StringBuilder sb = new StringBuilder();
            sb.append(stmt.getClass().getSimpleName());
            sb.append(" [");
            sb.append(stmt.toString());
            sb.append("]");
            return sb.toString(); // differentiate toString message.
        } else if ("getDataSourceName".equals(methodName)) {
            return dataSourceName;
        } else if ("getTarget".equals(methodName)) {
            // ProxyJdbcObject interface has method to return original object.
            return stmt;
        }

        if (StatementMethodNames.JDBC4_METHODS.contains(methodName)) {
            final Class<?> clazz = (Class<?>) args[0];
            if ("unwrap".equals(methodName)) {
                return stmt.unwrap(clazz);
            } else if ("isWrapperFor".equals(methodName)) {
                return stmt.isWrapperFor(clazz);
            }
        }

        if (StatementMethodNames.GET_CONNECTION_METHOD.contains(methodName)) {
            return connectionProxy;
        }

        if ("addBatch".equals(methodName) || "clearBatch".equals(methodName)) {
            if ("addBatch".equals(methodName) && ObjectArrayUtils.isFirstArgString(args)) {
                final QueryTransformer queryTransformer = interceptorHolder.getQueryTransformer();
                final String query = (String) args[0];
                final Class<? extends Statement> clazz = Statement.class;
                final int batchCount = batchQueries.size();
                final TransformInfo transformInfo = new TransformInfo(clazz, dataSourceName, query, true, batchCount);
                final String transformedQuery = queryTransformer.transformQuery(transformInfo);
                args[0] = transformedQuery;  // replace to the new query
                batchQueries.add(transformedQuery);
            } else if ("clearBatch".equals(methodName)) {
                batchQueries.clear();
            }

            // proceed execution, no need to call listener
            try {
                return method.invoke(stmt, args);
            } catch (InvocationTargetException ex) {
                throw ex.getTargetException();
            }
        }


        List<QueryInfo> queries;
        boolean isBatchExecute = false;
        int batchSize = 0;

        if (StatementMethodNames.BATCH_EXEC_METHODS.contains(methodName)) {

            queries = new ArrayList<QueryInfo>(batchQueries.size());
            for (String batchQuery : batchQueries) {
                queries.add(new QueryInfo(batchQuery));
            }
            batchSize = batchQueries.size();
            batchQueries.clear();
            isBatchExecute = true;
            queries = Collections.unmodifiableList(queries);
        } else if (StatementMethodNames.QUERY_EXEC_METHODS.contains(methodName) && ObjectArrayUtils.isFirstArgString(args)) {
            final QueryTransformer queryTransformer = interceptorHolder.getQueryTransformer();
            final String query = (String) args[0];
            final TransformInfo transformInfo = new TransformInfo(Statement.class, dataSourceName, query, false, 0);
            final String transformedQuery = queryTransformer.transformQuery(transformInfo);
            args[0] = transformedQuery; // replace to the new query
            queries = Collections.singletonList(new QueryInfo(transformedQuery, null));
        } else {
            queries = Collections.emptyList();
        }

        final QueryExecutionListener listener = interceptorHolder.getListener();
        final ExecutionInfoBuilder execInfoBuilder = ExecutionInfoBuilder.create()
                .dataSourceName(dataSourceName)
                .batch(isBatchExecute)
                .batchSize(batchSize)
                .method(method)
                .methodArgs(args)
                .statement(stmt);
        listener.beforeQuery(execInfoBuilder.build(), queries);

        // Invoke method on original Statement.
        try {
            final TimeProvider timeProvider = connectionProxy.getTimeProvider();
            final long beforeTime = timeProvider.getCurrentTime();
            Object retVal;
            try {
                retVal = method.invoke(stmt, args);
            } finally {
                final long afterTime = timeProvider.getCurrentTime();
                execInfoBuilder.elapsedTime(afterTime - beforeTime, timeProvider.getTimeUnit());
            }

            execInfoBuilder.result(retVal);
            execInfoBuilder.success(true);

            return retVal;
        } catch (InvocationTargetException ex) {
            execInfoBuilder.throwable(ex.getTargetException());
            execInfoBuilder.success(false);
            throw ex.getTargetException();
        } finally {
            listener.afterQuery(execInfoBuilder.build(), queries);
        }
    }

}
