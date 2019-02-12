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
public class UserRegister extends UserFunctions {

    public UserRegister(JDBCConnectionPool _pool, UserProperty userProperty) {
        super(_pool, userProperty);
    }

    @Override
    public void steps() throws SQLException {
        insertUser();
        getUserId();
    }

    public void insertUser() throws SQLException {
        Connection connection = pool.checkOut();

        Statement statement = connection.createStatement();

        System.out.println(userProperty.getPassword());

        String query = "INSERT INTO users (userName, emailId, password, phoneNo, levelId) VALUES('" + userProperty.getUserName() + "', '" + userProperty.getEmail() + "', '" + userProperty.getPassword() + "'," + userProperty.getPhoneNo() + ", " + userProperty.getLevel().getProperty().getData().get("id") + ")";

        statement.executeUpdate(query);

        statement.close();

        pool.checkIn(connection);
    }

    public void getUserId() throws SQLException {
        Connection connection = pool.checkOut();

        Statement statement = connection.createStatement();

        String query = "SELECT userId FROM users WHERE emailId = '" + userProperty.getEmail() + "'";

        ResultSet set = statement.executeQuery(query);

        while (set.next())
        {
            userProperty.setUserId(set.getInt("userId"));
        }

        set.close();
        statement.close();

        pool.checkIn(connection);
    }
}
