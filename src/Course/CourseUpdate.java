package Course;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by angel on 3/13/17.
 */
public class CourseUpdate extends CourseFunctions {

    public CourseUpdate(CourseProperty property, Connection connection) throws SQLException {
        super(property, connection);
    }

    @Override
    public void steps() throws SQLException {
        updateDetails();
        updateLevel();
        updateCourseSubType();
        updateModes();
    }

    public void updateDetails() throws SQLException {
        String query = "UPDATE courses SET courseTitle = '" + this.courseProperty.getCourseName() + "', courseCode = " + this.courseProperty.getCourseCode() + ", courseHours = " + this.courseProperty.getCourseHours() + " WHERE courseId = " + this.courseProperty.getCourseId();

        this.statement.executeUpdate(query);
    }

    public void updateLevel() throws SQLException {
        String query = "DELETE FROM courseLevel WHERE courseId = " + this.courseProperty.getCourseId();

        this.statement.executeUpdate(query);

        query = "INSERT INTO courseLevel (courseId, levelId) VALUES(" + this.courseProperty.getCourseId() + ", " + this.courseProperty.getLevelId() + ")";

        this.statement.executeUpdate(query);
    }

    public void updateCourseSubType() throws SQLException {
        String query = "DELETE FROM addCourseSubType WHERE courseId = " + this.courseProperty.getCourseId();

        this.statement.executeUpdate(query);

        statement.close();

        String subQuery = "SELECT courseSubTypeId FROM courseSubType WHERE courseTypeId = " + this.courseProperty.getCourseTypeId() + " AND trainingTypeId = " + this.courseProperty.getTrainingTypeId();

        this.statement = this.connection.createStatement();

        ResultSet set = this.statement.executeQuery(subQuery);
        int i = -1;

        while (set.next())
        {
            i = set.getInt("courseSubTypeId");
        }

        set.close();
        this.statement.close();

        this.statement = this.connection.createStatement();

        query = "INSERT INTO addCourseSubType (courseSubTypeId, courseId) VALUES(" + i + "," + this.courseProperty.getCourseId() + ")";

        this.statement.executeUpdate(query);
    }

    public void updateModes() throws SQLException {
        String query = "DELETE FROM availableCourseModes WHERE courseId = " + this.courseProperty.getCourseId();

        this.statement.executeUpdate(query);

        for (int i:
             this.courseProperty.getModes()) {
            query = "INSERT INTO availableCourseModes (courseId, modeId) VALUES(" + this.courseProperty.getCourseId() + ", " + i + ")";

            this.statement.executeUpdate(query);
        }
    }
}
