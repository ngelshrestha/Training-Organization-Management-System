package UserFunctions;

import JDBCConnection.JDBCConnectionPool;
import User.UserProperty;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by angel on 3/15/17.
 */
public class GiveFeedback extends UserFunctions {

    public GiveFeedback(JDBCConnectionPool _pool, UserProperty userProperty) {
        super(_pool, userProperty);
    }

    @Override
    public void steps() throws SQLException {
        insertFeedback();
    }

    public void insertFeedback() throws SQLException {
        Connection connection = pool.checkOut();

        Statement statement = connection.createStatement();

        String query = "INSERT INTO feedback (message, userId, packageId) VALUES('" + userProperty.getMessage() + "'," + userProperty.getUserId() + "," + userProperty.getCoursePackage().getProperty().getData().get("id") + ")";

        statement.executeUpdate(query);

        statement.close();

        pool.checkIn(connection);
    }
}
