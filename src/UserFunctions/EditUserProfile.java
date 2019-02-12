package UserFunctions;

import JDBCConnection.JDBCConnectionPool;
import User.UserProperty;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by angel on 3/17/17.
 */
public class EditUserProfile extends UserFunctions {

    public EditUserProfile(JDBCConnectionPool _pool, UserProperty userProperty) {
        super(_pool, userProperty);
    }

    @Override
    public void steps() throws SQLException {
        changeUserDetails();
    }

    private void changeUserDetails() throws SQLException {
        Connection connection = pool.checkOut();

        Statement statement = connection.createStatement();

        String query = "UPDATE users SET userName = '" + userProperty.getUserName() + "', emailId = '" + userProperty.getEmail() + "', phoneNo = " + userProperty.getPhoneNo() + ", levelId = " + userProperty.getLevel().getProperty().getData().get("id") + " WHERE userId = " + userProperty.getUserId();

        statement.executeUpdate(query);

        statement.close();

        statement = connection.createStatement();

        if ((boolean)userProperty.getLevel().getProperty().getData().get("isChanged"))
        {
            query = "DELETE FROM userPurchase WHERE userId = " + userProperty.getUserId();

            statement.executeUpdate(query);
        }

        statement.close();

        pool.checkIn(connection);
    }
}
