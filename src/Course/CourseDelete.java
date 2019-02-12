package Course;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by angel on 3/13/17.
 */
public class CourseDelete extends CourseFunctions
{

    public CourseDelete(CourseProperty property, Connection connection) throws SQLException {
        super(property, connection);
    }

    public void steps() throws SQLException {
        deleteCourseLevel();
        deleteCourseSubType();
        deleteCourseModes();
        deleteCourse();
    }

    public void deleteCourseLevel() throws SQLException {
        String query = "DELETE FROM courseLevel WHERE courseId = " + this.courseProperty.getCourseId();

        this.statement.executeUpdate(query);
    }

    public void deleteCourseSubType() throws SQLException {
        String query = "DELETE FROM addCourseSubType WHERE courseId = " + this.courseProperty.getCourseId();

        this.statement.executeUpdate(query);
    }

    public void deleteCourseModes() throws SQLException {
        String query = "DELETE FROM availableCourseModes WHERE courseId = " + this.courseProperty.getCourseId();

        this.statement.executeUpdate(query);
    }

    public void deleteCourse() throws SQLException {
        String query = "DELETE FROM courses WHERE courseId = " + this.courseProperty.getCourseId();

        this.statement.executeUpdate(query);
    }

}
