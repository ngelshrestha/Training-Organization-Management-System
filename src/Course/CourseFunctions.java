package Course;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by angel on 3/13/17.
 */
public abstract class CourseFunctions {

    protected CourseProperty courseProperty;
    protected Statement statement;
    protected Connection connection;

    public CourseFunctions(CourseProperty property, Connection connection) throws SQLException {
        this.courseProperty = property;
        this.connection = connection;
        this.statement = connection.createStatement();
    }

    public abstract void steps() throws SQLException;

}
