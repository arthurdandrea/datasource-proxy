package net.ttddyy.dsproxy;

import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

/**
 * @author Tadaya Tsuyukubo
 * @since 1.3
 */
public class ExecutionInfoBuilder {
    private String dataSourceName;
    private Method method;
    private Object[] methodArgs;
    private Object result;
    private long elapsedTime;
    private TimeUnit elapsedTimeUnit;
    private Throwable throwable;
    private StatementType statementType;
    private boolean success;
    private boolean batch;
    private int batchSize;
    private Statement statement;
    private boolean statementTypeSet;

    public static ExecutionInfoBuilder create() {
        return new ExecutionInfoBuilder();
    }

    public ExecutionInfoBuilder dataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
        return this;
    }

    public ExecutionInfoBuilder method(Method method) {
        this.method = method;
        return this;
    }

    public ExecutionInfoBuilder methodArgs(Object[] methodArgs) {
        this.methodArgs = methodArgs;
        return this;
    }

    public ExecutionInfoBuilder result(Object result) {
        this.result = result;
        return this;
    }

    public ExecutionInfoBuilder elapsedTime(long elapsedTime, TimeUnit timeUnit) {
        this.elapsedTime = elapsedTime;
        elapsedTimeUnit = timeUnit;
        return this;
    }

    public ExecutionInfoBuilder throwable(Throwable throwable) {
        this.throwable = throwable;
        return this;
    }

    public ExecutionInfoBuilder statement(Statement statement) {
        this.statement = statement;
        return this;
    }

    public ExecutionInfoBuilder statementType(StatementType statementType) {
        this.statementType = statementType;
        statementTypeSet = true;
        return this;
    }

    public ExecutionInfoBuilder success(boolean success) {
        this.success = success;
        return this;
    }

    public ExecutionInfoBuilder batch(boolean batch) {
        this.batch = batch;
        return this;
    }

    public ExecutionInfoBuilder batchSize(int batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    public ExecutionInfo build() {
        StatementType statementType = statementTypeSet ? this.statementType : StatementType.valueOf(statement);
        return new ExecutionInfo(dataSourceName, statement, batch, batchSize, method, methodArgs, elapsedTime, elapsedTimeUnit, result, throwable, success, statementType);
    }
}
