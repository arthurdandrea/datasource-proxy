package net.ttddyy.dsproxy.transform;

import net.ttddyy.dsproxy.TestUtils;
import net.ttddyy.dsproxy.listener.QueryExecutionListener;
import net.ttddyy.dsproxy.proxy.InterceptorHolder;
import net.ttddyy.dsproxy.proxy.jdk.JdkJdbcProxyFactory;
import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;

/**
 * @author Tadaya Tsuyukubo
 */
public class CallableStatementParameterTransformTest {

    private DataSource rawDatasource;
    private static List<String> batchValues = new ArrayList<String>();

    // hsqldb stored procedure.
    public static void foo(String input1, String input2, Integer input3, String[] output1, String[] output2, Integer[] output3) {
        output1[0] = "FOO:" + input1 + "+" + input2 + "+" + input3;
        output2[0] = String.valueOf(input3);
        output3[0] = output1[0].length();
    }

    public static void bar(String input1, String input2) {
        batchValues.add("BAR:" + input1 + "+" + input2);
    }

    @Before
    public void setup() throws Exception {
        JDBCDataSource rawDataSource = new JDBCDataSource();
        rawDataSource.setDatabase("jdbc:hsqldb:mem:aname");
        rawDataSource.setUser("sa");
        this.rawDatasource = rawDataSource;

        String procFoo = "CREATE PROCEDURE foo(IN in1 VARCHAR(50), IN in2 VARCHAR(50), IN in3 INT, OUT out1 VARCHAR(200), OUT out2 VARCHAR(200), OUT out3 INT) LANGUAGE JAVA NOT DETERMINISTIC NO SQL EXTERNAL NAME 'CLASSPATH:net.ttddyy.dsproxy.transform.CallableStatementParameterTransformTest.foo'";
        String procBar = "CREATE PROCEDURE bar(IN in1 VARCHAR(50), IN in2 VARCHAR(50)) LANGUAGE JAVA NOT DETERMINISTIC NO SQL EXTERNAL NAME 'CLASSPATH:net.ttddyy.dsproxy.transform.CallableStatementParameterTransformTest.bar'";

        Statement stmt = rawDataSource.getConnection().createStatement();
        stmt.addBatch(procFoo);
        stmt.addBatch(procBar);
        stmt.executeBatch();

        // for procedure bar
        batchValues.clear();
    }

    @After
    public void teardown() throws Exception {
        TestUtils.shutdown(rawDatasource);
    }

    private Connection getProxyConnection(ParameterTransformer paramTransformer) throws Exception {
        QueryExecutionListener queryListener = mock(QueryExecutionListener.class);
        QueryTransformer queryTransformer = mock(QueryTransformer.class);
        when(queryTransformer.transformQuery(isA(TransformInfo.class))).thenAnswer(new Answer<String>() {
            public String answer(InvocationOnMock invocation) throws Throwable {
                return ((TransformInfo) invocation.getArguments()[0]).getQuery();  // return input query as is
            }
        });
        InterceptorHolder interceptorHolder = new InterceptorHolder(queryListener, queryTransformer, paramTransformer);

        return new JdkJdbcProxyFactory().createConnection(rawDatasource.getConnection(), interceptorHolder);
    }

    @Test
    public void testReplaceParam() throws Exception {

        ParameterTransformer paramTransformer = mock(ParameterTransformer.class);
        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ParameterReplacer replacer = (ParameterReplacer) invocation.getArguments()[0];
                replacer.setString("@p1", "first_REPLACED");  // replace first parameter by name
                replacer.setString(2, "second_REPLACED");  // replace second parameter by index
                replacer.registerOutParameter("@p4", Types.VARCHAR);
                return null;
            }
        }).when(paramTransformer).transformParameters(isA(ParameterReplacer.class), isA(TransformInfo.class));

        Connection conn = getProxyConnection(paramTransformer);

        CallableStatement cs = conn.prepareCall("{call foo(?,?,?,?,?,?)}");
        cs.setString("@p1", "first");
        cs.setString(2, "second");
        cs.setInt(3, 100);
        cs.registerOutParameter("@p4", Types.VARCHAR);
        cs.registerOutParameter(5, Types.VARCHAR);
        cs.registerOutParameter(6, Types.INTEGER);
        cs.execute();

        verify(paramTransformer, only()).transformParameters(isA(ParameterReplacer.class), isA(TransformInfo.class));

        String out1 = cs.getString("@p4");
        assertThat(out1, is("FOO:first_REPLACED+second_REPLACED+100"));

        String out2 = cs.getString(5);
        assertThat(out2, is("100"));

        int out3 = cs.getInt(6);
        assertThat(out3, is("FOO:first_REPLACED+second_REPLACED+100".length()));

    }

    @Test
    public void testClearAndReplaceParam() throws Exception {

        ParameterTransformer paramTransformer = mock(ParameterTransformer.class);
        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ParameterReplacer replacer = (ParameterReplacer) invocation.getArguments()[0];
                replacer.clearParameters();
                replacer.setString("@p1", "first_REPLACED");
                replacer.setString(2, "second_REPLACED");
                replacer.setInt(3, 200);
                replacer.registerOutParameter("@p4", Types.VARCHAR);
                replacer.registerOutParameter(5, Types.VARCHAR);
                replacer.registerOutParameter(6, Types.INTEGER);
                return null;
            }
        }).when(paramTransformer).transformParameters(isA(ParameterReplacer.class), isA(TransformInfo.class));

        Connection conn = getProxyConnection(paramTransformer);

        CallableStatement cs = conn.prepareCall("{call foo(?,?,?,?,?,?)}");
        cs.setString("@p1", "first");
        cs.setString(2, "second");
        cs.setInt(3, 100);
        cs.registerOutParameter("@p4", Types.VARCHAR);
        cs.registerOutParameter(5, Types.VARCHAR);
        cs.registerOutParameter(6, Types.INTEGER);
        cs.execute();

        verify(paramTransformer, only()).transformParameters(isA(ParameterReplacer.class), isA(TransformInfo.class));

        String out1 = cs.getString("@p4");
        assertThat(out1, is("FOO:first_REPLACED+second_REPLACED+200"));

        String out2 = cs.getString(5);
        assertThat(out2, is("200"));

        int out3 = cs.getInt(6);
        assertThat(out3, is("FOO:first_REPLACED+second_REPLACED+200".length()));

    }

    @Test
    public void testBatchReplaceParam() throws Exception {

        ParameterTransformer paramTransformer = mock(ParameterTransformer.class);
        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ParameterReplacer replacer = (ParameterReplacer) invocation.getArguments()[0];
                replacer.setString("@p2", "second-1_REPLACED");  // first batch
                return null;
            }
        }).doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ParameterReplacer replacer = (ParameterReplacer) invocation.getArguments()[0];
                replacer.setString("@p2", "second-2_REPLACED");  // second batch
                return null;
            }
        }).when(paramTransformer).transformParameters(isA(ParameterReplacer.class), isA(TransformInfo.class));

        Connection conn = getProxyConnection(paramTransformer);

        CallableStatement cs = conn.prepareCall("{call bar(?,?)}");
        cs.setString(1, "first-1");
        cs.setString("@p2", "second-1");
        cs.addBatch();
        cs.setString(1, "first-2");
        cs.setString("@p2", "second-2");
        cs.addBatch();
        cs.executeBatch();

        verify(paramTransformer, times(2)).transformParameters(isA(ParameterReplacer.class), isA(TransformInfo.class));

        assertThat(batchValues, hasSize(2));
        assertThat(batchValues.get(0), is("BAR:first-1+second-1_REPLACED"));
        assertThat(batchValues.get(1), is("BAR:first-2+second-2_REPLACED"));
    }

    @Test
    public void testBatchClearAndReplaceParam() throws Exception {

        ParameterTransformer paramTransformer = mock(ParameterTransformer.class);
        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                // first batch. call clearParameters().
                ParameterReplacer replacer = (ParameterReplacer) invocation.getArguments()[0];
                replacer.clearParameters();
                replacer.setString(1, "first-1_REPLACED");
                replacer.setString("@p2", "second-1_REPLACED");
                return null;
            }
        }).doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                // second batch. don't call clearParameters().
                ParameterReplacer replacer = (ParameterReplacer) invocation.getArguments()[0];
                replacer.setString("@p2", "second-2_REPLACED");
                return null;
            }
        }).when(paramTransformer).transformParameters(isA(ParameterReplacer.class), isA(TransformInfo.class));

        Connection conn = getProxyConnection(paramTransformer);

        CallableStatement cs = conn.prepareCall("{call bar(?,?)}");
        cs.setString(1, "first-1");
        cs.setString("@p2", "second-1");
        cs.addBatch();
        cs.setString(1, "first-2");
        cs.setString("@p2", "second-2");
        cs.addBatch();
        cs.executeBatch();

        verify(paramTransformer, times(2)).transformParameters(isA(ParameterReplacer.class), isA(TransformInfo.class));

        assertThat(batchValues, hasSize(2));
        assertThat(batchValues.get(0), is("BAR:first-1_REPLACED+second-1_REPLACED"));
        assertThat(batchValues.get(1), is("BAR:first-2+second-2_REPLACED"));
    }

}
