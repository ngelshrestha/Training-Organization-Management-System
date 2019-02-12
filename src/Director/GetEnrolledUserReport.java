package Director;

import Admin.Hierarchy;
import Admin.HierarchyFactory;
import JDBCConnection.JDBCConnectionPool;
import Properties.Property;
import Properties.PropertyFactory;
import User.UserProperty;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by angel on 3/15/17.
 */
public class GetEnrolledUserReport extends Report {

    public GetEnrolledUserReport(String type, JDBCConnectionPool pool) {
        super(type, pool);
    }

    @Override
    public void getReportData() throws SQLException {
        Connection connection = pool.checkOut();

        Statement statement = connection.createStatement();

        String query = "SELECT (SELECT users.userName FROM users WHERE users.userId = userPurchase.userId) AS userName, (SELECT packages.packageName FROM packages WHERE packages.packageId = userPurchase.packageId) AS packageName, (SELECT purchaseStatus.purchaseStatusType FROM purchaseStatus WHERE purchaseStatus.purchaseStatusId = userPurchase.purchaseStatusId) AS purchaseStatus, userPurchase.date FROM userPurchase";

        ResultSet set = statement.executeQuery(query);

        list = new ArrayList<>();
        HashMap<String, Object> data = new HashMap<>();

        while (set.next()) {
            Hierarchy packageHierarchy = HierarchyFactory.getHierarchy("Package");

            data.put("name", set.getString("purchaseStatus"));

            Property purchaseProperty = PropertyFactory.getProperty("Purchase Status");

            purchaseProperty.setData(data);

            data.clear();

            data.put("name", set.getString("packageName"));
            data.put("userPurchaseStatus", purchaseProperty);

            packageHierarchy.setProperty();
            packageHierarchy.getProperty().setData(data);

            UserProperty property = new UserProperty();

            property.setUserName(set.getString("userName"));

            data.clear();

            data.put("package", packageHierarchy);
            data.put("user", property);
            data.put("date", set.getDate("date"));

            list.add(data);
        }

        set.close();
        statement.close();
        pool.checkIn(connection);
    }
}
