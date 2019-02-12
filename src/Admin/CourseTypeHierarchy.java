package Admin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by angel on 3/13/17.
 */
public class CourseTypeHierarchy extends Hierarchy {

    public CourseTypeHierarchy(String type) {
        super(type);
    }

    @Override
    public void addToDatabase() throws SQLException {
        String query = "INSERT INTO courseType (courseType) VALUES('" + this.getProperty().getData().get("name") + "')";

        this.getStatement().executeUpdate(query);
    }

    @Override
    public void deleteFromDatabase() throws SQLException {
        String query = "DELETE FROM courseType WHERE courseTypeId = " + this.getProperty().getData().get("id");

        this.getStatement().executeUpdate(query);
    }

    @Override
    public void updateInDatabase() throws SQLException {
        String query = "UPDATE courseType SET courseType = '" + this.getProperty().getData().get("name") + "' WHERE courseTypeId = " + this.getProperty().getData().get("id");

        this.getStatement().executeUpdate(query);
    }

    @Override
    public List<Hierarchy> getAll() throws SQLException {
        List<Hierarchy> hierarchies = new ArrayList<>();

        String query = "SELECT * FROM courseType";

        ResultSet set = this.getStatement().executeQuery(query);

        while (set.next())
        {
            HashMap<String, Object> data = new HashMap<>();

            Hierarchy hierarchy = HierarchyFactory.getHierarchy("Course Type");

            data.put("name", set.getString("courseType"));
            data.put("id", set.getInt("courseTypeId"));

            hierarchy.setProperty();
            hierarchy.getProperty().setData(data);

            hierarchies.add(hierarchy);
        }

        set.close();

        return hierarchies;
    }

    @Override
    public void checkForDelete() throws SQLException {

    }
}
