package Admin;

import Course.CourseProperty;
import org.omg.PortableServer.THREAD_POLICY_ID;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by angel on 3/14/17.
 */
public class PackageHierarchy extends Hierarchy {

    public PackageHierarchy(String type) {
        super(type);
    }

    @Override
    public void addToDatabase() throws SQLException {
        Hierarchy hierarchy = (Hierarchy) this.getProperty().getData().get("level");

        String query = "INSERT INTO packages (packageName, levelId) VALUES('" + this.getProperty().getData().get("name") + "'," + hierarchy.getProperty().getData().get("id") + ")";

        this.getStatement().executeUpdate(query);

        this.getStatement().close();

        this.setStatement();

        query = "SELECT packageId FROM packages WHERE packageName = '" + this.getProperty().getData().get("name") + "' AND levelId = " + hierarchy.getProperty().getData().get("id");

        ResultSet set = this.getStatement().executeQuery(query);

        int i = -1;

        while (set.next())
        {
            i = set.getInt("packageId");
        }

        set.close();
        this.getStatement().close();

        List<CourseProperty> courseProperties = (List<CourseProperty>) this.getProperty().getData().get("courseIds");

        for (CourseProperty course:
             courseProperties) {

            this.setStatement();

            query = "INSERT INTO coursesInPackage (courseModeId, packageId) VALUES(" + course.getCourseModeId() + ", " + i + ")";

            this.getStatement().executeUpdate(query);

            this.getStatement().close();
        }

    }

    @Override
    public void deleteFromDatabase() throws SQLException {
        String query = "DELETE FROM coursesInPackage WHERE packageId = " + this.getProperty().getData().get("id");

        this.getStatement().executeUpdate(query);

        this.getStatement().close();

        this.setStatement();

        query = "DELETE FROM packages WHERE packageId = " + this.getProperty().getData().get("id");

        this.getStatement().executeUpdate(query);
    }

    @Override
    public void updateInDatabase() throws SQLException {

        Hierarchy level = (Hierarchy) this.getProperty().getData().get("level");

        String query = "UPDATE packages SET packageName = '" + this.getProperty().getData().get("name") + "', levelId = " + level.getProperty().getData().get("id") + " WHERE packageId = " + this.getProperty().getData().get("id");

        this.getStatement().executeUpdate(query);

        this.getStatement().close();

        this.setStatement();

        query = "DELETE FROM coursesInPackage WHERE packageId = " + this.getProperty().getData().get("id");

        this.getStatement().executeUpdate(query);

        List<CourseProperty> courseProperties = (List<CourseProperty>) this.getProperty().getData().get("courseIds");

        for (CourseProperty c:
             courseProperties) {
            this.getStatement().close();

            this.setStatement();

            query = "INSERT INTO coursesInPackage (courseModeId, packageId) VALUES(" + c.getCourseModeId() + ", " + this.getProperty().getData().get("id") + ")";

            this.getStatement().executeUpdate(query);
        }
    }

    @Override
    public List<Hierarchy> getAll() throws SQLException {

        List<Hierarchy> hierarchies = new ArrayList<>();

        HashMap<String,Object> data = new HashMap<>();

        if ((int)this.getProperty().getData().get("courseRequired")==0)
        {

            Hierarchy hierarchy = (Hierarchy) this.getProperty().getData().get("level");

            String query = "SELECT packages.packageId, packages.packageName FROM packages WHERE levelId = " + hierarchy.getProperty().getData().get("id");

            ResultSet set = this.getStatement().executeQuery(query);

            while (set.next())
            {
                Hierarchy coursePackage = HierarchyFactory.getHierarchy("Package");

                coursePackage.setProperty();

                data.clear();

                data.put("name", set.getString("packageName"));
                data.put("id", set.getInt("packageId"));

                coursePackage.getProperty().setData(data);

                hierarchies.add(coursePackage);
            }
        } else {
            Hierarchy level = (Hierarchy) this.getProperty().getData().get("level");

            String query = "SELECT trainingModes.modeId, trainingModes.modeName, courses.courseId, courses.courseTitle, courses.courseCode, courses.courseHours, availableCourseModes.courseModeId FROM trainingModes, courses, availableCourseModes JOIN coursesInPackage ON coursesInPackage.courseModeId = availableCourseModes.courseModeId WHERE coursesInPackage.packageId = " + this.getProperty().getData().get("id") + " AND courses.courseId = availableCourseModes.courseId AND trainingModes.modeId = availableCourseModes.modeId";

            Statement statement1 = this.getConnection().createStatement();

            ResultSet set1 = statement1.executeQuery(query);

            List<CourseProperty> courseProperties = new ArrayList<>();

            while (set1.next())
            {
                CourseProperty courseProperty = new CourseProperty();

                courseProperty.setCourseId(set1.getInt("courseId"));
                courseProperty.setCourseName(set1.getString("courseTitle"));
                courseProperty.setCourseCode(set1.getInt("courseCode"));
                courseProperty.setCourseHours(set1.getInt("courseHours"));

                Hierarchy trainingMode = HierarchyFactory.getHierarchy("Training Mode");

                data.clear();
                data.put("id", set1.getInt("modeId"));
                data.put("name", set1.getString("modeName"));

                trainingMode.setProperty();
                trainingMode.getProperty().setData(data);

                courseProperty.setCourseModeId(set1.getInt("courseModeId"));
                courseProperty.setTrainingMode(trainingMode);

                courseProperties.add(courseProperty);
            }

            data.clear();
            data.put("id", this.getProperty().getData().get("id"));
            data.put("name", this.getProperty().getData().get("name"));
            data.put("courseIds",courseProperties);
            data.put("level", level);

            this.getProperty().setData(data);

            hierarchies.add(this);
        }

        return hierarchies;
    }

    @Override
    public void checkForDelete() throws SQLException {

    }
}
