package User;

import Admin.Hierarchy;
import Admin.HierarchyFactory;
import Admin.PackageHierarchy;
import JDBCConnection.JDBCConnectionPool;
import Properties.Property;
import Properties.PropertyFactory;

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
public class FormGetProperties {

    private static UserProperty user = null;

    public static UserProperty getUserLevel(Connection connection, UserProperty userProperty) throws SQLException {
        Statement statement = connection.createStatement();

        String query = "SELECT administrativeLevels.levelId, administrativeLevels.levelName FROM administrativeLevels JOIN users ON administrativeLevels.levelId = users.levelId WHERE users.userId = " + userProperty.getUserId();

        ResultSet set = statement.executeQuery(query);

        while (set.next())
        {
            Hierarchy hierarchy = HierarchyFactory.getHierarchy("Administrative Level");

            HashMap<String, Object> data = new HashMap<>();

            data.put("id", set.getInt("levelId"));
            data.put("name", set.getString("levelName"));

            hierarchy.setProperty();
            hierarchy.getProperty().setData(data);

            userProperty.setLevel(hierarchy);
        }

        set.close();
        statement.close();

        return userProperty;
    }

    public static List<Hierarchy> getPackages(Connection connection, UserProperty userProperty) throws SQLException {
        Statement statement = connection.createStatement();

        String query = "SELECT packages.packageId, packages.packageName, userPurchase.purchaseStatusId,(SELECT purchaseStatus.purchaseStatusType FROM purchaseStatus WHERE purchaseStatus.purchaseStatusId = userPurchase.purchaseStatusId) AS purchaseStatus FROM packages JOIN userPurchase ON packages.packageId = userPurchase.packageId WHERE userPurchase.userId = " + userProperty.getUserId();

        ResultSet set = statement.executeQuery(query);

        List<Hierarchy> packages = new ArrayList<>();

        HashMap<String, Object> data = new HashMap<>();

        while (set.next())
        {
            Hierarchy hierarchy = HierarchyFactory.getHierarchy("Package");

            data.clear();

            data.put("id", set.getInt("purchaseStatusId"));
            data.put("name", set.getString("purchaseStatus"));

            Property property = PropertyFactory.getProperty("Purchase Status");

            property.setData(data);

            data.clear();

            data.put("id", set.getInt("packageId"));
            data.put("name", set.getString("packageName"));
            data.put("userPurchaseStatus", property);

            hierarchy.setProperty();
            hierarchy.getProperty().setData(data);

            packages.add(hierarchy);
        }

        return packages;
    }

    public static void setUser(UserProperty _user)
    {
        user = _user;
    }

    public static void removeUser()
    {
        user = null;
    }

    public static List<UserProperty> getUserRequest(Connection connection) throws SQLException {
        List<UserProperty> users = new ArrayList<>();

        Statement statement = connection.createStatement();

        if (user == null)
        {
            String query = "SELECT users.userId, users.userName, packages.packageId, packages.packageName, userPurchase.date FROM  users, packages JOIN userPurchase ON packages.packageId = userPurchase.packageId WHERE users.userId = userPurchase.userId AND packages.packageId = userPurchase.packageId AND userPurchase.purchaseStatusId = (SELECT purchaseStatus.purchaseStatusId FROM purchaseStatus WHERE purchaseStatus.purchaseStatusType = 'Requested')";

            ResultSet set = statement.executeQuery(query);

            while (set.next())
            {
                HashMap<String , Object> data = new HashMap<>();

                data.put("id", set.getInt("packageId"));
                data.put("name", set.getString("packageName"));

                Hierarchy packageHierarchy = HierarchyFactory.getHierarchy("Package");

                packageHierarchy.setProperty();
                packageHierarchy.getProperty().setData(data);

                data.clear();

                UserProperty property = new UserProperty();

                property.setUserId(set.getInt("userId"));
                property.setUserName(set.getString("userName"));
                property.setDate(set.getDate("date"));
                property.setCoursePackage(packageHierarchy);

                users.add(property);
            }

            set.close();

        } else {
            String query = "SELECT users.userName, users.emailId, users.phoneNo, (SELECT administrativeLevels.levelName FROM administrativeLevels WHERE administrativeLevels.levelId = users.levelId) AS `level`, users.levelId FROM users WHERE userId = " + user.getUserId();

            ResultSet set = statement.executeQuery(query);

            HashMap<String, Object> data = new HashMap<>();

            while (set.next())
            {
                user.setUserName(set.getString("userName"));
                user.setEmail(set.getString("emailId"));
                user.setPhoneNo(set.getInt("phoneNo"));

                Hierarchy hierarchy = HierarchyFactory.getHierarchy("Administrative Level");

                data.clear();
                data.put("id", set.getInt("levelId"));
                data.put("name", set.getString("level"));

                hierarchy.setProperty();
                hierarchy.getProperty().setData(data);

                user.setLevel(hierarchy);

                users.add(user);
            }
        }

        statement.close();

        return users;

    }


}
