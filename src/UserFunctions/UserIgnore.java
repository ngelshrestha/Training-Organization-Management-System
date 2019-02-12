package UserFunctions;

import JDBCConnection.JDBCConnectionPool;
import User.UserProperty;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by angel on 3/16/17.
 */
public class UserIgnore extends UserFunctions {

    public UserIgnore(JDBCConnectionPool _pool, UserProperty userProperty) {
        super(_pool, userProperty);
    }

    @Override
    public void steps() throws SQLException {
        removePurchase();
    }

    private void removePurchase() throws SQLException {
        Connection connection = pool.checkOut();

        Statement statement = connection.createStatement();

        String query = "DELETE FROM userPurchase WHERE userId = " + userProperty.getUserId() + " AND packageId = " + userProperty.getCoursePackage().getProperty().getData().get("id");

        statement.executeUpdate(query);

        statement.close();

        pool.checkIn(connection);
    }
}
