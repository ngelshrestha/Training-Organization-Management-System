package UserFunctions;

import JDBCConnection.JDBCConnectionPool;
import User.UserProperty;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by angel on 3/15/17.
 */
public class LeaveCourse extends UserFunctions {

    public LeaveCourse(JDBCConnectionPool _pool, UserProperty userProperty) {
        super(_pool, userProperty);
    }

    @Override
    public void steps() throws SQLException {
        deletePackageRequest();
    }

    public void deletePackageRequest() throws SQLException {
        Connection connection = pool.checkOut();

        String query = "DELETE FROM userPurchase WHERE userId = " + this.userProperty.getUserId() + " AND packageId = " + this.userProperty.getCoursePackage().getProperty().getData().get("id");

        Statement statement = connection.createStatement();

        statement.executeUpdate(query);

        statement.close();

        pool.checkIn(connection);
    }
}
