package UserFunctions;

import JDBCConnection.JDBCConnectionPool;
import User.UserProperty;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by angel on 3/15/17.
 */
public class UserLogin extends UserFunctions {

    public UserLogin(JDBCConnectionPool pool, UserProperty user) throws SQLException {
        super(pool,user);
    }

    @Override
    public void steps() throws SQLException {
        checkLogin();
    }

    private void checkLogin() throws SQLException {
        Connection connection = pool.checkOut();

        String query = "SELECT userId FROM users WHERE emailID = '" + this.userProperty.getEmail() + "' AND password = '" + this.userProperty.getPassword() + "'";

        Statement statement = connection.createStatement();

        ResultSet set = statement.executeQuery(query);

        while (set.next())
        {
            this.userProperty.setUserId(set.getInt("userId"));
        }

        pool.checkIn(connection);
    }
}
