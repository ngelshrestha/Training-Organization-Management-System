package Course;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by angel on 3/13/17.
 */
public class CourseAdd extends CourseFunctions {

    public CourseAdd(CourseProperty property, Connection connection) throws SQLException {
        super(property, connection);
    }

    @Override
    public void steps() throws SQLException {
        insertCourseBasic();
        getInsertedCourseId();
        insertLevel();
        getSubType();
        insertSubType();
        insertModes();
    }

    public void insertCourseBasic() throws SQLException {
        String query = "INSERT INTO courses (courseTitle, courseCode, courseHours) VALUES('" + this.courseProperty.getCourseName() + "'," + this.courseProperty.getCourseCode() + "," + this.courseProperty.getCourseHours() + ")";

        this.statement.executeUpdate(query);
    }

    public void getInsertedCourseId() throws SQLException {
        String query = "SELECT courseId FROM courses WHERE courseTitle = '" + this.courseProperty.getCourseName() + "'";

        ResultSet set = this.statement.executeQuery(query);

        while (set.next())
            this.courseProperty.setCourseId(set.getInt("courseId"));
    }

    public void insertLevel() throws SQLException {
        String query = "INSERT INTO courseLevel (courseId, levelId) VALUES(" + this.courseProperty.getCourseId() + "," + this.courseProperty.getLevelId() + ")";

        this.statement.executeUpdate(query);
    }

    public void getSubType() throws SQLException {
        String query = "SELECT courseSubTypeId FROM courseSubType WHERE courseTypeId = " + this.courseProperty.getCourseTypeId() + " AND trainingTypeId = " + this.courseProperty.getTrainingTypeId();

        ResultSet set = this.statement.executeQuery(query);

        while (set.next())
        {
            this.courseProperty.setCourseSubType(set.getInt("courseSubTypeId"));
        }
    }

    public void insertSubType() throws SQLException {
        String query = "INSERT INTO addCourseSubType (courseSubTypeId, courseId) VALUES (" + this.courseProperty.getCourseSubType() + "," + this.courseProperty.getCourseId() + ")";

        this.statement.executeUpdate(query);
    }

    public void insertModes() throws SQLException {
        List<Integer> list = this.courseProperty.getModes();

        for (int i:
                list) {
            String query = "INSERT INTO availableCourseModes (courseId, modeId) VALUES(" + this.courseProperty.getCourseId() + "," + i + ")";

            this.statement.executeUpdate(query);
        }
    }
}
