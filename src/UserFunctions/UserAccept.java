package UserFunctions;

import JDBCConnection.JDBCConnectionPool;
import User.UserProperty;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by angel on 3/16/17.
 */
public class UserAccept extends UserFunctions {

    public UserAccept(JDBCConnectionPool _pool, UserProperty userProperty) {
        super(_pool, userProperty);
    }

    @Override
    public void steps() throws SQLException {
        changePurchaseStatus();
    }

    private void changePurchaseStatus() throws SQLException {
        Connection connection = pool.checkOut();

        Statement statement = connection.createStatement();

        String query = "UPDATE userPurchase SET userPurchase.purchaseStatusId = (SELECT purchaseStatus.purchaseStatusId FROM purchaseStatus WHERE purchaseStatus.purchaseStatusType = 'Accepted') WHERE userId = " + userProperty.getUserId() + "  AND packageId = " + userProperty.getCoursePackage().getProperty().getData().get("id");

        statement.executeUpdate(query);

        statement.close();

        pool.checkIn(connection);
    }
}
