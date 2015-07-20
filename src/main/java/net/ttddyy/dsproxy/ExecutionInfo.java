package net.ttddyy.dsproxy;

import java.lang.reflect.Method;
import java.sql.Statement;

/**
 * Contains query execution information.
 *
 * @author Tadaya Tsuyukubo
 */
public class ExecutionInfo {
    private final String dataSourceName;
    private final Method method;
    private final Object[] methodArgs;
    private final Object result;
    private final long elapsedTime;
    private final Throwable throwable;
    private final StatementType statementType;
    private final boolean isSuccess;
    private final boolean isBatch;
    private final int batchSize;
    private final Statement statement;

    public ExecutionInfo(String dataSourceName, Statement statement, boolean isBatch, int batchSize, Method method, Object[] methodArgs, long elapsedTime, Object result, Throwable throwable, boolean isSuccess) {
        this(dataSourceName, statement, isBatch, batchSize, method, methodArgs, elapsedTime, result, throwable, isSuccess, StatementType.valueOf(statement));
    }

    public ExecutionInfo(String dataSourceName, Statement statement, boolean isBatch, int batchSize, Method method, Object[] methodArgs, long elapsedTime, Object result, Throwable throwable, boolean isSuccess, StatementType statementType) {
        this.dataSourceName = dataSourceName;
        this.statement = statement;
        this.isBatch = isBatch;
        this.batchSize = batchSize;
        this.method = method;
        this.methodArgs = methodArgs;
        this.result = result;
        this.elapsedTime = elapsedTime;
        this.throwable = throwable;
        this.isSuccess = isSuccess;
        this.statementType = statementType;
    }


    public Method getMethod() {
        return method;
    }

    public Object[] getMethodArgs() {
        return methodArgs;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public Object getResult() {
        return result;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public StatementType getStatementType() {
        return statementType;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public boolean isBatch() {
        return isBatch;
    }

    public int getBatchSize() {
        return batchSize;
    }

    /**
     * Returns {@link java.sql.Statement}, {@link java.sql.PreparedStatement}, or {@link java.sql.CallableStatement}
     * used by the execution.
     *
     * @return statement/prepared/callable object
     * @since 1.3.1
     */
    public Statement getStatement() {
        return statement;
    }
}
