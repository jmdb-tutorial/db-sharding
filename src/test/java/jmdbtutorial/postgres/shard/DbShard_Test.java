package jmdbtutorial.postgres.shard;

import org.junit.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

import static java.lang.Thread.currentThread;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.readAllBytes;

public class DbShard_Test {

    private static Connection conn;

    @BeforeClass
    public static void connect_to_db() throws SQLException {
        String url = "jdbc:postgresql://localhost/?user=postgres";
        conn = DriverManager.getConnection(url);
    }

    @AfterClass
    public static void close_connection() throws SQLException {
        conn.close();
    }

    @Test
    public void execute_sql_file() throws URISyntaxException, IOException, SQLException {
        URL url = currentThread().getContextClassLoader().getResource("test_sql_statements.sql");

        String fileContent = new String(readAllBytes(Paths.get(url.toURI())), UTF_8);

        String[] sqlStatmenets = fileContent.split(";");

        for (String sql : sqlStatmenets) {
            Statement st = conn.createStatement();

            System.out.println("\nExecuting: \n" + sql);
            if (sql.trim().startsWith("SELECT")) {
                ResultSet rs = st.executeQuery(sql);
                Sql.printRows(Sql.readRows(rs));
            } else {
                st.execute(sql);
            }

        }

    }
}
