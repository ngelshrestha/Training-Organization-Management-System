package UserFunctions;

import JDBCConnection.JDBCConnectionPool;
import User.UserProperty;

import java.sql.SQLException;

/**
 * Created by angel on 3/15/17.
 */
public class UserFunctionsFactory {

    public static UserFunctions getUserFunction(String type, JDBCConnectionPool pool, UserProperty user) throws SQLException {
        UserFunctions userFunctions = null;

        switch (type)
        {
            case "Login":
                userFunctions = new UserLogin(pool,user);
                break;

            case "Request Course":
                userFunctions = new CourseRequest(pool,user);
                break;

            case "Leave Course":
                userFunctions = new LeaveCourse(pool,user);
                break;

            case "Give Feedback":
                userFunctions = new GiveFeedback(pool,user);
                break;

            case "Register":
                userFunctions = new UserRegister(pool,user);
                break;

            case "User Accept":
                userFunctions = new UserAccept(pool, user);
                break;

            case "User Ignore":
                userFunctions = new UserIgnore(pool, user);
                break;

            case "Change Password":
                userFunctions = new ChangePassword(pool,user);
                break;

            case "Edit Profile":
                userFunctions = new EditUserProfile(pool,user);
                break;

            default:
                userFunctions = null;
                break;
        }

        return userFunctions;
    }

}
