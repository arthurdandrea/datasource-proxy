package net.ttddyy.dsproxy;

import net.ttddyy.dsproxy.proxy.ConnectionProxy;
import net.ttddyy.dsproxy.proxy.InterceptorHolder;
import org.hsqldb.jdbc.JDBCDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Tadaya Tsuyukubo
 */
public class TestUtils {

    public static DataSource getDataSourceWithData() throws Exception {
        JDBCDataSource dataSource = new JDBCDataSource();
        dataSource.setDatabase("jdbc:hsqldb:mem:aname");
        dataSource.setUser("sa");

        executeQuery(dataSource,
                "create table emp ( id integer primary key, name varchar(10) );",
                "insert into emp ( id, name )values (1, 'foo');",
                "insert into emp ( id, name )values (2, 'bar');"
        );

        return dataSource;
    }

    private static void executeQuery(DataSource dataSource, String... queries) throws Exception {
        Connection conn = dataSource.getConnection();
        Statement stmt = conn.createStatement();
        for (String query : queries) {
            stmt.execute(query);
        }
        conn.close();
    }

    public static void shutdown(DataSource dataSource) throws Exception {
        executeQuery(dataSource, "shutdown;");
    }

    public static int countTable(DataSource dataSource, String tableName) throws Exception {
        Connection conn = dataSource.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select count(*) from " + tableName);
        rs.next();
        conn.close();
        return rs.getInt(1);
    }

    public static ConnectionProxy mockConnectionProxy(String dataSourceName) {
        return mockConnectionProxy(new InterceptorHolder(), dataSourceName);
    }

    public static ConnectionProxy mockConnectionProxy(InterceptorHolder interceptorHolder, String dataSourceName) {
        ConnectionProxy mock = mock(ConnectionProxy.class);
        when(mock.getInterceptorHolder()).thenReturn(interceptorHolder);
        when(mock.getDataSourceName()).thenReturn(dataSourceName);
        when(mock.getTimeProvider()).thenReturn(CurrentTimeProvider.INSTANCE);
        return mock;
    }
}
