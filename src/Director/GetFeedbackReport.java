package Director;

import Admin.Hierarchy;
import Admin.HierarchyFactory;
import JDBCConnection.JDBCConnectionPool;
import Properties.Property;
import User.UserProperty;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by angel on 3/15/17.
 */
public class GetFeedbackReport extends Report {

    public GetFeedbackReport(String type, JDBCConnectionPool pool) {
        super(type,pool);
    }

    @Override
    public void getReportData() throws SQLException {
        Connection connection = pool.checkOut();

        Statement statement = connection.createStatement();

        String query = "SELECT (SELECT users.userName FROM users WHERE users.userId = feedback.userId) AS userName, (SELECT packages.packageName FROM packages WHERE packages.packageId = feedback.packageId) AS packageName, message, `date` FROM feedback";

        ResultSet set = statement.executeQuery(query);

        list = new ArrayList<>();

        while (set.next())
        {
            HashMap<String , Object> data = new HashMap<>();

            Hierarchy hierarchy = HierarchyFactory.getHierarchy("Package");

            data.put("name", set.getString("packageName"));

            hierarchy.setProperty();
            hierarchy.getProperty().setData(data);

            data.clear();

            UserProperty property = new UserProperty();

            property.setUserName(set.getString("userName"));
            property.setMessage(set.getString("message"));

            data.put("package", hierarchy);
            data.put("user", property);
            data.put("date", set.getDate("date"));

            list.add(data);
        }

        set.close();
        statement.close();

        pool.checkIn(connection);
    }
}
