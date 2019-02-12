package Admin;

import Properties.AdministrativeLevelProperty;
import Properties.Property;
import Properties.PropertyFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by angel on 3/8/17.
 */
public class AdministrativeLevelHierarchy extends Hierarchy {

    public AdministrativeLevelHierarchy(String type) {
        super(type);
    }

    @Override
    public void addToDatabase() throws SQLException {
        String query = "INSERT INTO administrativeLevels (levelName) VALUES('" + this.getProperty().getData().get("name") + "')";

        this.getStatement().executeUpdate(query);
    }

    @Override
    public void deleteFromDatabase() throws SQLException {
        String query = "DELETE FROM administrativeLevels WHERE levelId = " + this.getProperty().getData().get("id");

        this.getStatement().executeUpdate(query);
    }

    @Override
    public void updateInDatabase() throws SQLException {
        String query = "UPDATE administrativeLevels SET levelName = '" + this.getProperty().getData().get("name") + "' WHERE levelId = " + this.getProperty().getData().get("id");

        this.getStatement().executeUpdate(query);
    }

    @Override
    public List<Hierarchy> getAll() throws SQLException {

        List<Hierarchy> hierarchies = new ArrayList<>();

        String query = "SELECT * FROM administrativeLevels";

        ResultSet set = this.getStatement().executeQuery(query);

        while (set.next())
        {
            HashMap<String, Object> data = new HashMap<>();

            Hierarchy hierarchy = HierarchyFactory.getHierarchy("Administrative Level");

            data.put("name", set.getString("levelName"));
            data.put("id", set.getInt("levelId"));

            hierarchy.setProperty();
            hierarchy.getProperty().setData(data);

            hierarchies.add(hierarchy);
        }

        return hierarchies;
    }

    @Override
    public void checkForDelete() throws SQLException {

    }
}
