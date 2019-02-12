package Admin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by angel on 3/8/17.
 */
public class TrainingModeHierarchy extends Hierarchy {

    public TrainingModeHierarchy(String type) {
        super(type);
    }

    @Override
    public void addToDatabase() throws SQLException {
        String query = "INSERT INTO trainingModes (modeName) VALUES('" + this.getProperty().getData().get("name") + "')";

        this.getStatement().executeUpdate(query);
    }

    @Override
    public void deleteFromDatabase() throws SQLException {
        String query = "DELETE FROM trainingModes WHERE modeId = " + this.getProperty().getData().get("id");

        this.getStatement().executeUpdate(query);
    }

    @Override
    public void updateInDatabase() throws SQLException {
        String query = "UPDATE trainingModes SET modeName = '" + this.getProperty().getData().get("name") + "' WHERE modeId = " + this.getProperty().getData().get("id");

        this.getStatement().executeUpdate(query);
    }


    @Override
    public List<Hierarchy> getAll() throws SQLException {
        List<Hierarchy> hierarchies = new ArrayList<>();

        String query = "SELECT * FROM trainingModes";

        ResultSet set = this.getStatement().executeQuery(query);

        while (set.next())
        {
            HashMap<String, Object> data = new HashMap<>();

            Hierarchy hierarchy = HierarchyFactory.getHierarchy("Training Mode");

            data.put("name", set.getString("modeName"));
            data.put("id", set.getInt("modeId"));

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
