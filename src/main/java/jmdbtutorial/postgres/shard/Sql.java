package jmdbtutorial.postgres.shard;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class Sql {
    public static void printRows(List<String> rows) {
        for (String row : rows) {
            System.out.println(row);
        }
    }

    public static List<String> readRows(ResultSet rs) throws SQLException {
        List<String> rows = new ArrayList<>();

        ResultSetMetaData metaData = rs.getMetaData();
        int cols = metaData.getColumnCount();
        StringBuffer sb = new StringBuffer();
        for (int i=1;i<=cols; i++) {
            sb.append(metaData.getColumnLabel(i));
            sb.append("\t\t\t");
        }
        rows.add(sb.toString());

        rows.add("----------------------------------------");


        while (rs.next()) {
            sb = new StringBuffer();
            for (int i=1;i<=cols;i++) {
                sb.append(rs.getObject(i));
                sb.append("\t");
            }
            rows.add(sb.toString());
        }
        return rows;
    }
}
