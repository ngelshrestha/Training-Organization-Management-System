package JDBCConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by angel on 3/3/17.
 */
public class Main {

    public static void main(String args[]) throws SQLException {
        JDBCConnectionPool pool = new JDBCConnectionPool("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/designPatterns?verifyServerCertificate=false&useSSL=true", "root", "root");

        Connection connection = pool.checkOut();

        Statement statement = connection.createStatement();

        String query = "SELECT title FROM test WHERE id = 1";
        ResultSet rs = statement.executeQuery(query);

        while (rs.next())
        {
            String test = rs.getString("title");
            System.out.println(test);
        }

        statement.close();

        pool.checkIn(connection);
    }

}
