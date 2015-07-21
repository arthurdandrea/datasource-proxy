package net.ttddyy.dsproxy.proxy;

import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.TestUtils;
import net.ttddyy.dsproxy.listener.QueryExecutionListener;
import net.ttddyy.dsproxy.transform.QueryTransformer;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Tadaya Tsuyukubo
 */
public class PreparedStatementProxyLogicMockTest {

    private static final String DS_NAME = "myDS";

    @Test
    public void testExecuteQuery() throws Throwable {
        final String query = "select * from emp where id = ?";

        PreparedStatement stat = mock(PreparedStatement.class);
        QueryExecutionListener listener = mock(QueryExecutionListener.class);

        PreparedStatementProxyLogic logic = getProxyLogic(stat, query, listener);


        Array array = mock(Array.class);
        InputStream inputStream = mock(InputStream.class);
        BigDecimal bigDecimal = mock(BigDecimal.class);
        InputStream binaryStream = mock(InputStream.class);
        Blob blob = mock(Blob.class);
        boolean booleanValue = true;
        Reader reader = mock(Reader.class);
        Clob clob = mock(Clob.class);
        Date date = mock(Date.class);
        double doubleValue = 10.0d;
        Float floatValue = 20f;
        Integer intvalue = 30;
        Long longValue = 40L;
        Object object = mock(Object.class);
        Ref ref = mock(Ref.class);
        Short shortValue = 50;
        String stringValue = "str";
        Time time = mock(Time.class);
        Timestamp timestamp = mock(Timestamp.class);
        URL url = new URL("http://foo.com");

        Method setArray = PreparedStatement.class.getMethod("setArray", int.class, Array.class);
        Method setAsciiStream = PreparedStatement.class.getMethod("setAsciiStream", int.class, InputStream.class);
        Method setBigDecimal = PreparedStatement.class.getMethod("setBigDecimal", int.class, BigDecimal.class);
        Method setBinaryStream = PreparedStatement.class.getMethod("setBinaryStream", int.class, InputStream.class);
        Method setBlob = PreparedStatement.class.getMethod("setBlob", int.class, Blob.class);
        Method setBoolean = PreparedStatement.class.getMethod("setBoolean", int.class, boolean.class);
        Method setCharacterStream = PreparedStatement.class.getMethod("setCharacterStream", int.class, Reader.class);
        Method setClob = PreparedStatement.class.getMethod("setClob", int.class, Clob.class);
        Method setDate = PreparedStatement.class.getMethod("setDate", int.class, Date.class);
        Method setDouble = PreparedStatement.class.getMethod("setDouble", int.class, double.class);
        Method setFloat = PreparedStatement.class.getMethod("setFloat", int.class, float.class);
        Method setInt = PreparedStatement.class.getMethod("setInt", int.class, int.class);
        Method setLong = PreparedStatement.class.getMethod("setLong", int.class, long.class);
        Method setNull = PreparedStatement.class.getMethod("setNull", int.class, int.class);
        Method setObject = PreparedStatement.class.getMethod("setObject", int.class, Object.class);
        Method setRef = PreparedStatement.class.getMethod("setRef", int.class, Ref.class);
        Method setShort = PreparedStatement.class.getMethod("setShort", int.class, short.class);
        Method setString = PreparedStatement.class.getMethod("setString", int.class, String.class);
        Method setTime = PreparedStatement.class.getMethod("setTime", int.class, Time.class);
        Method setTimestamp = PreparedStatement.class.getMethod("setTimestamp", int.class, Timestamp.class);
        Method setURL = PreparedStatement.class.getMethod("setURL", int.class, URL.class);
        Method executeQuery = PreparedStatement.class.getMethod("executeQuery");

        logic.invoke(setArray, new Object[]{1, array});
        logic.invoke(setAsciiStream, new Object[]{2, inputStream});
        logic.invoke(setBigDecimal, new Object[]{3, bigDecimal});
        logic.invoke(setBinaryStream, new Object[]{4, binaryStream});
        logic.invoke(setBlob, new Object[]{5, blob});
        logic.invoke(setBoolean, new Object[]{6, booleanValue});
        logic.invoke(setCharacterStream, new Object[]{7, reader});
        logic.invoke(setClob, new Object[]{8, clob});
        logic.invoke(setDate, new Object[]{9, date});
        logic.invoke(setDouble, new Object[]{10, doubleValue});
        logic.invoke(setFloat, new Object[]{11, floatValue});
        logic.invoke(setInt, new Object[]{12, intvalue});
        logic.invoke(setLong, new Object[]{13, longValue});
        logic.invoke(setNull, new Object[]{14, Types.VARCHAR});
        logic.invoke(setObject, new Object[]{15, object});
        logic.invoke(setRef, new Object[]{16, ref});
        logic.invoke(setShort, new Object[]{17, shortValue});
        logic.invoke(setString, new Object[]{18, stringValue});
        logic.invoke(setTime, new Object[]{19, time});
        logic.invoke(setTimestamp, new Object[]{20, timestamp});
        logic.invoke(setURL, new Object[]{21, url});

        logic.invoke(executeQuery, null);

        verify(stat).setArray(1, array);
        verify(stat).setAsciiStream(2, inputStream);
        verify(stat).setBigDecimal(3, bigDecimal);
        verify(stat).setBinaryStream(4, binaryStream);
        verify(stat).setBlob(5, blob);
        verify(stat).setBoolean(6, booleanValue);
        verify(stat).setCharacterStream(7, reader);
        verify(stat).setClob(8, clob);
        verify(stat).setDate(9, date);
        verify(stat).setDouble(10, doubleValue);
        verify(stat).setFloat(11, floatValue);
        verify(stat).setInt(12, intvalue);
        verify(stat).setLong(13, longValue);
        verify(stat).setNull(14, Types.VARCHAR);
        verify(stat).setObject(15, object);
        verify(stat).setRef(16, ref);
        verify(stat).setShort(17, shortValue);
        verify(stat).setString(18, stringValue);
        verify(stat).setTime(19, time);
        verify(stat).setTimestamp(20, timestamp);
        verify(stat).setURL(21, url);
        verify(stat).executeQuery();

        Map<String, Object> expectedQueryArgas = new LinkedHashMap<String, Object>();
        expectedQueryArgas.put("1", array);
        expectedQueryArgas.put("2", inputStream);
        expectedQueryArgas.put("3", bigDecimal);
        expectedQueryArgas.put("4", binaryStream);
        expectedQueryArgas.put("5", blob);
        expectedQueryArgas.put("6", booleanValue);
        expectedQueryArgas.put("7", reader);
        expectedQueryArgas.put("8", clob);
        expectedQueryArgas.put("9", date);
        expectedQueryArgas.put("10", doubleValue);
        expectedQueryArgas.put("11", floatValue);
        expectedQueryArgas.put("12", intvalue);
        expectedQueryArgas.put("13", longValue);
        expectedQueryArgas.put("14", "NULL(VARCHAR)");
        expectedQueryArgas.put("15", object);
        expectedQueryArgas.put("16", ref);
        expectedQueryArgas.put("17", shortValue);
        expectedQueryArgas.put("18", stringValue);
        expectedQueryArgas.put("19", time);
        expectedQueryArgas.put("20", timestamp);
        expectedQueryArgas.put("21", url);

        verifyListener(listener, "executeQuery", query, expectedQueryArgas);

    }

    private PreparedStatementProxyLogic getProxyLogic(PreparedStatement ps, String query, QueryExecutionListener listener) {
        InterceptorHolder interceptorHolder = new InterceptorHolder(listener, QueryTransformer.DEFAULT);
        return new PreparedStatementProxyLogic(ps, query, TestUtils.mockConnectionProxy(interceptorHolder, DS_NAME));
    }

    @SuppressWarnings("unchecked")
    private void verifyListener(QueryExecutionListener listener, String methodName, String query, Map<String, Object> expectedQueryArgs) {
        ArgumentCaptor<ExecutionInfo> executionInfoCaptor = ArgumentCaptor.forClass(ExecutionInfo.class);
        ArgumentCaptor<List> queryInfoListCaptor = ArgumentCaptor.forClass(List.class);


        verify(listener).afterQuery(executionInfoCaptor.capture(), queryInfoListCaptor.capture());

        ExecutionInfo execInfo = executionInfoCaptor.getValue();
        assertThat(execInfo.getMethod(), is(notNullValue()));
        assertThat(execInfo.getMethod().getName(), is(methodName));

        assertThat(execInfo.getMethodArgs(), is(nullValue()));
        assertThat(execInfo.getDataSourceName(), is(DS_NAME));
        assertThat(execInfo.getThrowable(), is(nullValue()));
        assertThat(execInfo.isBatch(), is(false));
        assertThat(execInfo.getBatchSize(), is(0));

        List<QueryInfo> queryInfoList = queryInfoListCaptor.getValue();
        assertThat(queryInfoList.size(), is(1));
        QueryInfo queryInfo = queryInfoList.get(0);
        assertThat(queryInfo.getQuery(), is(query));

        List<Map<String, Object>> queryArgsList = queryInfo.getQueryArgsList();
        assertThat("non-batch query size is always 1", queryArgsList, hasSize(1));

        Map<String, Object> queryArgs = queryArgsList.get(0);
        assertThat(queryArgs, aMapWithSize(expectedQueryArgs.size()));
        for (Map.Entry<String, Object> entry : expectedQueryArgs.entrySet()) {
            assertThat(queryArgs, hasEntry(entry.getKey(), entry.getValue()));
        }
    }

    @Test
    public void testBatch() throws Throwable {
        final String query = "update emp set name = ? where id = ?";

        PreparedStatement stat = mock(PreparedStatement.class);
        when(stat.executeBatch()).thenReturn(new int[]{1, 1, 1});

        QueryExecutionListener listener = mock(QueryExecutionListener.class);

        PreparedStatementProxyLogic logic = getProxyLogic(stat, query, listener);

        Method setString = PreparedStatement.class.getMethod("setString", int.class, String.class);
        Method setInt = PreparedStatement.class.getMethod("setInt", int.class, int.class);
        Method addBatch = PreparedStatement.class.getMethod("addBatch");
        Method executeBatch = PreparedStatement.class.getMethod("executeBatch");

        logic.invoke(setString, new Object[]{1, "foo"});
        logic.invoke(setInt, new Object[]{2, 10});
        logic.invoke(addBatch, null);

        logic.invoke(setString, new Object[]{1, "bar"});
        logic.invoke(setInt, new Object[]{2, 20});
        logic.invoke(addBatch, null);

        logic.invoke(setString, new Object[]{1, "baz"});
        logic.invoke(setInt, new Object[]{2, 30});
        logic.invoke(addBatch, null);

        Object result = logic.invoke(executeBatch, null);

        assertThat(result, is(instanceOf(int[].class)));
        assertThat(((int[]) result).length, is(3));
        assertThat(((int[]) result)[0], is(1));
        assertThat(((int[]) result)[1], is(1));
        assertThat(((int[]) result)[2], is(1));

        verify(stat).setString(1, "foo");
        verify(stat).setInt(2, 10);
        verify(stat).setString(1, "bar");
        verify(stat).setInt(2, 20);
        verify(stat).setString(1, "baz");
        verify(stat).setInt(2, 30);
        verify(stat, times(3)).addBatch();

        Map<String, Object> expectedArgs1 = new LinkedHashMap<String, Object>();
        expectedArgs1.put("1", "foo");
        expectedArgs1.put("2", 10);

        Map<String, Object> expectedArgs2 = new LinkedHashMap<String, Object>();
        expectedArgs2.put("1", "bar");
        expectedArgs2.put("2", 20);

        Map<String, Object> expectedArgs3 = new LinkedHashMap<String, Object>();
        expectedArgs3.put("1", "baz");
        expectedArgs3.put("2", 30);

        MockTestUtils.verifyListenerForBatch(listener, DS_NAME, query, expectedArgs1, expectedArgs2, expectedArgs3);

    }

    @Test
    public void testBatchWithClearBatch() throws Throwable {
        final String query = "update emp set name = ? where id = ?";

        PreparedStatement stat = mock(PreparedStatement.class);
        when(stat.executeBatch()).thenReturn(new int[]{1});

        QueryExecutionListener listener = mock(QueryExecutionListener.class);

        PreparedStatementProxyLogic logic = getProxyLogic(stat, query, listener);

        Method setString = PreparedStatement.class.getMethod("setString", int.class, String.class);
        Method setInt = PreparedStatement.class.getMethod("setInt", int.class, int.class);
        Method addBatch = PreparedStatement.class.getMethod("addBatch");
        Method clearBatch = PreparedStatement.class.getMethod("clearBatch");
        Method executeBatch = PreparedStatement.class.getMethod("executeBatch");

        logic.invoke(setString, new Object[]{1, "foo"});
        logic.invoke(setInt, new Object[]{2, 10});
        logic.invoke(addBatch, null);

        logic.invoke(clearBatch, null);

        logic.invoke(setString, new Object[]{1, "FOO"});
        logic.invoke(setInt, new Object[]{2, 20});
        logic.invoke(addBatch, null);

        Object result = logic.invoke(executeBatch, null);

        assertThat(result, is(instanceOf(int[].class)));

        assertThat(((int[]) result).length, is(1));
        assertThat(((int[]) result)[0], is(1));

        verify(stat).setString(1, "foo");
        verify(stat).setInt(2, 10);
        verify(stat).clearBatch();
        verify(stat).setString(1, "FOO");
        verify(stat).setInt(2, 20);
        verify(stat, times(2)).addBatch();

        Map<String, Object> expectedArgs = new LinkedHashMap<String, Object>();
        expectedArgs.put("1", "FOO");
        expectedArgs.put("2", 20);

        MockTestUtils.verifyListenerForBatch(listener, DS_NAME, query, expectedArgs);

    }

    @Test
    public void testBatchWithClearParameters() throws Throwable {
        final String query = "update emp set name = ? where id = ?";

        PreparedStatement stat = mock(PreparedStatement.class);
        when(stat.executeBatch()).thenReturn(new int[]{1});

        QueryExecutionListener listener = mock(QueryExecutionListener.class);

        PreparedStatementProxyLogic logic = getProxyLogic(stat, query, listener);

        Method setString = PreparedStatement.class.getMethod("setString", int.class, String.class);
        Method setInt = PreparedStatement.class.getMethod("setInt", int.class, int.class);
        Method addBatch = PreparedStatement.class.getMethod("addBatch");
        Method clearParametes = PreparedStatement.class.getMethod("clearParameters");
        Method executeBatch = PreparedStatement.class.getMethod("executeBatch");

        logic.invoke(setString, new Object[]{1, "foo"});
        logic.invoke(clearParametes, null);
        logic.invoke(setString, new Object[]{1, "FOO"});
        logic.invoke(setInt, new Object[]{2, 10});
        logic.invoke(addBatch, null);

        Object result = logic.invoke(executeBatch, null);

        assertThat(result, is(instanceOf(int[].class)));

        assertThat(((int[]) result).length, is(1));
        assertThat(((int[]) result)[0], is(1));

        verify(stat).setString(1, "foo");
        verify(stat).clearParameters();
        verify(stat).setString(1, "FOO");
        verify(stat).setInt(2, 10);
        verify(stat).addBatch();

        Map<String, Object> expectedArgs = new LinkedHashMap<String, Object>();
        expectedArgs.put("1", "FOO");
        expectedArgs.put("2", 10);

        MockTestUtils.verifyListenerForBatch(listener, DS_NAME, query, expectedArgs);

    }

    @SuppressWarnings("unchecked")
    @Test
    public void testClearParameters() throws Throwable {
        final String query = "update emp set name = ? where id = ?";

        PreparedStatement stat = mock(PreparedStatement.class);

        QueryExecutionListener listener = mock(QueryExecutionListener.class);

        PreparedStatementProxyLogic logic = getProxyLogic(stat, query, listener);

        Method setString = PreparedStatement.class.getMethod("setString", int.class, String.class);
        Method setInt = PreparedStatement.class.getMethod("setInt", int.class, int.class);
        Method addBatch = PreparedStatement.class.getMethod("addBatch");
        Method clearParametes = PreparedStatement.class.getMethod("clearParameters");
        Method executeBatch = PreparedStatement.class.getMethod("executeBatch");

        logic.invoke(setString, new Object[]{1, "foo"});
        logic.invoke(setInt, new Object[]{2, 10});
        logic.invoke(clearParametes, null);
        logic.invoke(addBatch, null);

        logic.invoke(executeBatch, null);

        verify(stat).setString(1, "foo");
        verify(stat).setInt(2, 10);
        verify(stat).clearParameters();
        verify(stat).addBatch();

        ArgumentCaptor<List> queryInfoListCaptor = ArgumentCaptor.forClass(List.class);

        verify(listener).afterQuery(any(ExecutionInfo.class), queryInfoListCaptor.capture());

        List<QueryInfo> queryInfoList = queryInfoListCaptor.getValue();
        assertThat(queryInfoList.size(), is(1));

        assertThat(queryInfoList.get(0).getQueryArgsList(), hasSize(1));
        assertThat("Args should be empty", queryInfoList.get(0).getQueryArgsList().get(0), anEmptyMap());


    }


    @Test
    public void testGetTarget() throws Throwable {
        PreparedStatement stmt = mock(PreparedStatement.class);
        PreparedStatementProxyLogic logic = getProxyLogic(stmt, null, null);

        Method method = ProxyJdbcObject.class.getMethod("getTarget");
        Object result = logic.invoke(method, null);

        assertThat(result, is(instanceOf(PreparedStatement.class)));
        assertThat((PreparedStatement) result, is(sameInstance(stmt)));
    }

    @Test
    public void testUnwrap() throws Throwable {
        PreparedStatement mock = mock(PreparedStatement.class);
        when(mock.unwrap(String.class)).thenReturn("called");

        PreparedStatementProxyLogic logic = getProxyLogic(mock, null, null);

        Method method = PreparedStatement.class.getMethod("unwrap", Class.class);
        Object result = logic.invoke(method, new Object[]{String.class});

        verify(mock).unwrap(String.class);
        assertThat(result, is(instanceOf(String.class)));
        assertThat((String) result, is("called"));
    }

    @Test
    public void testIsWrapperFor() throws Throwable {
        PreparedStatement mock = mock(PreparedStatement.class);
        when(mock.isWrapperFor(String.class)).thenReturn(true);

        PreparedStatementProxyLogic logic = getProxyLogic(mock, null, null);

        Method method = PreparedStatement.class.getMethod("isWrapperFor", Class.class);
        Object result = logic.invoke(method, new Object[]{String.class});

        verify(mock).isWrapperFor(String.class);
        assertThat(result, is(instanceOf(boolean.class)));
        assertThat((Boolean) result, is(true));
    }

    @Test
    public void testGetConnection() throws Throwable {
        PreparedStatement stat = mock(PreparedStatement.class);

        ConnectionProxy connectionProxy = TestUtils.mockConnectionProxy(new InterceptorHolder(null, QueryTransformer.DEFAULT), DS_NAME);
        PreparedStatementProxyLogic logic = new PreparedStatementProxyLogic(stat, null, connectionProxy);

        Method method = PreparedStatement.class.getMethod("getConnection");
        Object result = logic.invoke(method, null);

        assertThat(result, is(sameInstance((Object) connectionProxy)));
        verify(stat, never()).getConnection();
    }

}
