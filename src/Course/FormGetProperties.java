package Course;

import Admin.Hierarchy;
import Admin.HierarchyFactory;

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
public class FormGetProperties {

    // add FormGetProperties, delete FormGetProperties, getCourseInfo, updateCourse

    // FormGetProperties Name, course COde, course hours, level, type, subtype, training modes

    public static List<Hierarchy> getLevels(Connection connection) throws SQLException
    {
        Hierarchy hierarchy = HierarchyFactory.getHierarchy("Administrative Level");

        hierarchy.setConnection(connection);
        hierarchy.setStatement();

        return hierarchy.getAll();
    }

    public static List<Hierarchy> getCourseTypes(Connection connection) throws SQLException {
        Hierarchy hierarchy = HierarchyFactory.getHierarchy("Course Type");

        hierarchy.setConnection(connection);
        hierarchy.setStatement();

        return hierarchy.getAll();
    }

    public static List<Hierarchy> getTrainingType(Connection connection, int typeId) throws SQLException {
        List<Hierarchy> hierarchies = new ArrayList<>();

        Statement statement = connection.createStatement();
        String query = "SELECT courseSubType.courseSubTypeId, trainingTypes.trainingTypeId, trainingTypes.trainingTypeTitle FROM courseSubType JOIN trainingTypes ON courseSubType.trainingTypeId = trainingTypes.trainingTypeId WHERE courseSubType.courseTypeId = " + typeId;

        ResultSet set = statement.executeQuery(query);

        while (set.next())
        {
            HashMap<String, Object> data = new HashMap<>();

            data.put("id", set.getInt("trainingTypeId"));
            data.put("name", set.getString("trainingTypeTitle"));
            data.put("courseSubTypeId", set.getInt("courseSubTypeId"));

            Hierarchy hierarchy = HierarchyFactory.getHierarchy("Training Type");

            hierarchy.setProperty();
            hierarchy.getProperty().setData(data);

            hierarchies.add(hierarchy);
        }

        return hierarchies;
    }

    public static List<Hierarchy> getTrainingModes(Connection connection) throws SQLException {
        Hierarchy hierarchy = HierarchyFactory.getHierarchy("Training Mode");

        hierarchy.setConnection(connection);
        hierarchy.setStatement();

        return hierarchy.getAll();
    }

    public static List<CourseProperty> getCourses(Connection connection, int levelId, int courseSubTypeId) throws SQLException {

        List<CourseProperty> courseProperties = new ArrayList<CourseProperty>();
        String query = "SELECT courseLevel.courseId AS courseId FROM addCourseSubType JOIN courseLevel ON addCourseSubType.courseId = courseLevel.courseId WHERE courseLevel.levelId = " + levelId + " AND addCourseSubType.courseSubTypeId = " + courseSubTypeId;

        Statement statement = connection.createStatement();

        ResultSet set = statement.executeQuery(query);

        while (set.next())
        {
            query = "SELECT courseTitle, courseId FROM courses WHERE courseId = " + set.getInt("courseId");

            Statement statement1 = connection.createStatement();

            ResultSet set1 = statement1.executeQuery(query);

            while (set1.next())
            {

                CourseProperty courseProperty = new CourseProperty();

                courseProperty.setCourseName(set1.getString("courseTitle"));
                courseProperty.setCourseId(set1.getInt("courseId"));

                courseProperties.add(courseProperty);
            }

            set1.close();
            statement1.close();
        }

        set.close();
        statement.close();

        return courseProperties;
    }

    public static List<CourseProperty> getCourses(Connection connection, int levelId) throws SQLException {
        List<CourseProperty> courseProperties = new ArrayList<>();

        Statement statement = connection.createStatement();

        String query = "SELECT courses.courseId, courses.courseTitle, courses.courseCode, courses.courseHours FROM courses JOIN courseLevel ON courses.courseId = courseLevel.courseId WHERE courseLevel.levelId = " + levelId;

        ResultSet set = statement.executeQuery(query);

        HashMap<String, Object> data = new HashMap<>();

        while (set.next())
        {
            List<Hierarchy> trainingMode = new ArrayList<>();

            Statement statement2 = connection.createStatement();

            query = "SELECT trainingModes.modeId, trainingModes.modeName, availableCourseModes.courseModeId FROM trainingModes JOIN availableCourseModes ON trainingModes.modeId = availableCourseModes.modeId WHERE availableCourseModes.courseId = " + set.getInt("courseId");

            ResultSet set2 = statement2.executeQuery(query);

            while (set2.next())
            {

                Hierarchy hierarchy = HierarchyFactory.getHierarchy("Training Mode");

                data.clear();
                data.put("id", set2.getInt("modeId"));
                data.put("name", set2.getString("modeName"));
                data.put("courseModeId", set2.getInt("courseModeId"));

                hierarchy.setProperty();
                hierarchy.getProperty().setData(data);

                trainingMode.add(hierarchy);
            }

            set2.close();
            statement2.close();

            CourseProperty courseProperty = new CourseProperty();

            courseProperty.setCourseId(set.getInt("courseId"));
            courseProperty.setCourseName(set.getString("courseTitle"));
            courseProperty.setCourseCode(set.getInt("courseCode"));
            courseProperty.setCourseHours(set.getInt("courseHours"));
            courseProperty.setTrainingModeHierarchy(trainingMode);

            courseProperties.add(courseProperty);
        }

        return courseProperties;
    }

    public static CourseProperty getRequiredCourse(Connection connection, CourseProperty courseProperty) throws SQLException {
        CourseFunctions getCourse = CourseFunctionFactory.applyFunction("Get", courseProperty, connection);

        getCourse.steps();

        return getCourse.courseProperty;
    }
}
