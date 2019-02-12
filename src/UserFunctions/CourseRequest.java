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
public class CourseRequest extends UserFunctions {

    private int purchaseType;

    public CourseRequest(JDBCConnectionPool _pool, UserProperty userProperty) {
        super(_pool, userProperty);
    }

    @Override
    public void steps() throws SQLException {
        getPurchaseStatus();
        insertUserRequest();
    }

    private void getPurchaseStatus() throws SQLException {
        Connection connection = pool.checkOut();
        String query = "SELECT purchaseStatusId FROM purchaseStatus WHERE purchaseStatusType = 'Requested'";

        Statement statement = connection.createStatement();

        ResultSet set = statement.executeQuery(query);

        while (set.next())
        {
            purchaseType = set.getInt("purchaseStatusId");
        }

        set.close();
        statement.close();
        pool.checkIn(connection);
    }

    private void insertUserRequest() throws SQLException {
        Connection connection = pool.checkOut();

        Statement statement = connection.createStatement();

        String query = "INSERT INTO userPurchase (userId, packageId, purchaseStatusId) VALUES(" + this.userProperty.getUserId() + "," + this.userProperty.getCoursePackage().getProperty().getData().get("id") + "," + this.purchaseType + ")";

        statement.executeUpdate(query);

        statement.close();

        pool.checkIn(connection);
    }
}
