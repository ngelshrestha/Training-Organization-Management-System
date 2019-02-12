package Admin;

import Properties.Property;
import Properties.PropertyFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by angel on 3/8/17.
 */
public abstract class Hierarchy {

    private Property property;
    private Connection connection;
    private Statement statement;
    private String type;

    public Hierarchy(String type)
    {
        this.type = type;
    }

    public void setStatement() throws SQLException {
        this.statement = this.connection.createStatement();
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setProperty()
    {
        this.property = PropertyFactory.getProperty(this.type);
    }

    public Statement getStatement() {
        return statement;
    }

    public Connection getConnection() {
        return connection;
    }

    public Property getProperty()
    {
        return this.property;
    }

    public abstract void addToDatabase() throws SQLException;
    public abstract void deleteFromDatabase() throws SQLException;
    public abstract void updateInDatabase() throws SQLException;
    public abstract List<Hierarchy> getAll() throws SQLException;
    public abstract void checkForDelete() throws SQLException;

}
