package Course;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by angel on 3/13/17.
 */
public class CourseGet extends CourseFunctions {

    public CourseGet(CourseProperty property, Connection connection) throws SQLException {
        super(property, connection);
    }

    @Override
    public void steps() throws SQLException {
        getCourseDetails();
        getCourseLevel();
        getCourseModes();
        getCourseSubType();
        getCourseType();
        getTrainingType();
    }

    public void getCourseDetails() throws SQLException {
        String query = "SELECT * FROM courses WHERE courseId = " + this.courseProperty.getCourseId();

        ResultSet set = this.statement.executeQuery(query);

        while (set.next())
        {
            this.courseProperty.setCourseName(set.getString("courseTitle"));
            this.courseProperty.setCourseCode(set.getInt("courseCode"));
            this.courseProperty.setCourseHours(set.getInt("courseHours"));
        }

        set.close();
    }

    public void getCourseLevel() throws SQLException {
        String query = "SELECT levelId FROM courseLevel WHERE courseId = " + this.courseProperty.getCourseId();

        ResultSet set = this.statement.executeQuery(query);

        while (set.next())
        {
            this.courseProperty.setLevelId(set.getInt("levelId"));
        }

        set.close();
    }

    public void getCourseSubType() throws SQLException {
        String query = "SELECT courseSubTypeId FROM addCourseSubType WHERE courseId = " + this.courseProperty.getCourseId();

        ResultSet set = this.statement.executeQuery(query);

        while (set.next())
        {
            this.courseProperty.setCourseSubType(set.getInt("courseSubTypeId"));
        }

        set.close();
    }

    public void getCourseModes() throws SQLException {
        String query = "SELECT modeId FROM availableCourseModes WHERE courseId = " + this.courseProperty.getCourseId();

        ResultSet set = this.statement.executeQuery(query);

        List<Integer> modes = new ArrayList<>();

        while (set.next())
        {
            modes.add(set.getInt("modeId"));
        }

        set.close();

        this.courseProperty.setModes(modes);
    }

    public void getCourseType() throws SQLException {
        String query = "SELECT courseTypeId FROM courseSubType WHERE courseSubTypeId = " + this.courseProperty.getCourseSubType();

        ResultSet set = this.statement.executeQuery(query);

        while (set.next())
        {
            this.courseProperty.setCourseTypeId(set.getInt("courseTypeId"));
        }
    }

    public void getTrainingType() throws SQLException {
        String query = "SELECT trainingTypeId FROM courseSubType WHERE courseSubTypeId = " + this.courseProperty.getCourseSubType();

        ResultSet set = this.statement.executeQuery(query);

        while (set.next())
        {
            this.courseProperty.setTrainingTypeId(set.getInt("trainingTypeId"));
        }
    }
}
