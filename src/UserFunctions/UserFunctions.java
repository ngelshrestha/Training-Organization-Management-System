package UserFunctions;

import JDBCConnection.JDBCConnectionPool;
import User.UserProperty;

import java.sql.SQLException;

/**
 * Created by angel on 3/15/17.
 */
public abstract class UserFunctions{

    protected UserProperty userProperty;
    protected JDBCConnectionPool pool;
    protected boolean functionExecuted = true;
    protected String error = null;

    public UserFunctions(JDBCConnectionPool _pool, UserProperty userProperty)
    {
        this.pool = _pool;
        this.userProperty = userProperty;
    }

    public UserProperty getUserProperty()
    {
        return userProperty;
    }

    public boolean isFunctionExecuted() {
        return functionExecuted;
    }

    public String getError() {
        return error;
    }

    public abstract void steps() throws SQLException;

}
