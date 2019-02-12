package Course;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by angel on 3/13/17.
 */
public class CourseFunctionFactory {

    public static CourseFunctions applyFunction(String functionType, CourseProperty property, Connection connection) throws SQLException {
        CourseFunctions courseFunction = null;

        switch (functionType)
        {
            case "Add":
                courseFunction = new CourseAdd(property, connection);
                break;

            case "Delete":
                courseFunction = new CourseDelete(property, connection);
                break;

            case "Get":
                courseFunction = new CourseGet(property, connection);
                break;

            case "Update":
                courseFunction = new CourseUpdate(property, connection);
                break;

            default:
                courseFunction = null;
        }

        return courseFunction;
    }

}
