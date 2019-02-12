package Admin;

import JDBCConnection.JDBCConnectionPool;
import Properties.Property;
import Properties.TrainingTypeProperty;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by angel on 3/9/17.
 */
public class HierarchyFactory {

    public static Hierarchy getHierarchy(String hierarchyType)
    {
        Hierarchy hierarchy = null;

        switch (hierarchyType)
        {
            case "Administrative Level":
                hierarchy = new AdministrativeLevelHierarchy("Administrative Level");
                break;

            case "Training Type":
                hierarchy = new TrainingTypeHierarchy("Training Type");
                break;

            case "Training Mode":
                hierarchy = new TrainingModeHierarchy("Training Mode");
                break;

            case "Course Type":
                hierarchy = new CourseTypeHierarchy("Course Type");
                break;

            case "Package":
                hierarchy = new PackageHierarchy("Package");
                break;

            default:
                hierarchy = null;
        }

        return hierarchy;
    }

    public static Hierarchy getCourseTrainingTypeRelation(JDBCConnectionPool pool, Hierarchy hierarchy) throws SQLException {
        Connection connection = pool.checkOut();

        Statement statement = connection.createStatement();

        String query = "SELECT courseType.courseTypeId, courseType.courseType From courseType JOIN courseSubType ON courseType.courseTypeId = courseSubType.courseTypeId WHERE courseSubType.trainingTypeId = " + hierarchy.getProperty().getData().get("id");

        ResultSet set = statement.executeQuery(query);

        HashMap<String, Object> data = new HashMap<>();

        while (set.next())
        {

            data.put("name", set.getString("courseType"));
            data.put("id", set.getInt("courseTypeId"));
        }

        Hierarchy courseTypeHierarchy = HierarchyFactory.getHierarchy("Course Type");

        courseTypeHierarchy.setProperty();
        courseTypeHierarchy.getProperty().setData(data);

        pool.checkIn(connection);
        return courseTypeHierarchy;
    }

}
