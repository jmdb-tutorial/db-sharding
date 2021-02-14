package jmdbtutorial.postgres.shard;

import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class Basic_Postgres_Connection {


    @Test
    public void can_connect() throws SQLException {

        String url = "jdbc:postgresql://localhost/";
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "");


        try (Connection conn = DriverManager.getConnection(url, props)) {
            if (tableExists(conn, "CUSTOMER")) {
                executeSql(conn, "DROP TABLE CUSTOMER;");
            }
            executeSql(conn, "CREATE TABLE CUSTOMER (id VARCHAR, name VARCHAR);");

            executeSql(conn, "INSERT INTO CUSTOMER VALUES ('000000001', 'foo bar');");


            ResultSet rs = executeQuery(conn, "SELECT * FROM CUSTOMER ;");

            List<String> rows = readRows(rs);
            assertThat(rows.size(), is(1));
            printRows(rows);
        }


    }

    private static boolean tableExists(Connection conn, String tableName
    ) throws SQLException {
        ResultSet rs = conn.prepareStatement(format("SELECT to_regclass('%s');", tableName)).executeQuery();
        rs.next();
        return rs.getString(1) != null;
    }

    private void printRows(List<String> rows) {
        System.out.println("id \t\t\t name");
        System.out.println("-------------------");
        for (String row : rows) {
            System.out.println(row);
        }
    }

    private static List<String> readRows(ResultSet rs) throws SQLException {
        List<String> rows = new ArrayList<>();
        while (rs.next()) {
            String id = rs.getString("id");
            String name = rs.getString("name");
            rows.add(format("%s \t %s", id, name));
        }
        return rows;
    }

    private static void executeSql(Connection conn, String sql) throws SQLException {
        PreparedStatement st = conn.prepareStatement(sql);
        st.execute();
    }


    private static ResultSet executeQuery(Connection conn, String sql) throws SQLException {
        PreparedStatement st = conn.prepareStatement(sql);
        return st.executeQuery();
    }
}
