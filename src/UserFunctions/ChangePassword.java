package UserFunctions;

import JDBCConnection.JDBCConnectionPool;
import User.UserProperty;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by angel on 3/17/17.
 */
public class ChangePassword extends UserFunctions {

    private boolean oldPasswordMatch = false;
    private boolean confirmPasswordMatch = false;

    public ChangePassword(JDBCConnectionPool _pool, UserProperty userProperty) {
        super(_pool, userProperty);
    }

    @Override
    public void steps() throws SQLException {
        checkConfirmPasswordMatch();
        if (confirmPasswordMatch)
        {
            checkOldPasswordMatch();

            if (oldPasswordMatch)
            {
                changePassword();
            } else {
                this.functionExecuted = false;
                this.error = "Old password do not match";
            }

        } else {
            this.functionExecuted = false;
            this.error = "New Password and Confirm Password do not match";
        }
    }

    private void checkConfirmPasswordMatch()
    {
        if (userProperty.getPasswordProperty().getPassword().equals(userProperty.getPasswordProperty().getConfirmPassword()))
        {
            this.confirmPasswordMatch = true;
        }
    }

    private void checkOldPasswordMatch() throws SQLException {
        Connection connection = pool.checkOut();

        Statement statement = connection.createStatement();

        String query = "SELECT password FROM users WHERE userId = " + userProperty.getUserId();

        ResultSet set = statement.executeQuery(query);

        while (set.next())
        {
            if (set.getString("password").equals(userProperty.getPasswordProperty().getOldPassword()))
            {
                this.oldPasswordMatch = true;
            }
        }

        set.close();
        statement.close();

        pool.checkIn(connection);
    }

    private void changePassword() throws SQLException {
        Connection connection = pool.checkOut();

        Statement statement = connection.createStatement();

        String query = "UPDATE users SET password = '" + userProperty.getPasswordProperty().getPassword() + "' WHERE userId = " + userProperty.getUserId();

        statement.executeUpdate(query);

        statement.close();

        pool.checkIn(connection);
    }
}
