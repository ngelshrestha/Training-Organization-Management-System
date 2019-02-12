package Admin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by angel on 3/8/17.
 */
public class TrainingTypeHierarchy extends Hierarchy {

    public TrainingTypeHierarchy(String type) {
        super(type);
    }

    @Override
    public void addToDatabase() throws SQLException {
        String query = "INSERT INTO trainingTypes (trainingTypeTitle) VALUES ('" + this.getProperty().getData().get("name") + "')";

        this.getStatement().executeUpdate(query);

        this.getStatement().close();

        query = "SELECT trainingTypeId FROM trainingTypes WHERE trainingTypeTitle = '" + this.getProperty().getData().get("name") + "'";

        this.setStatement();

        ResultSet set = this.getStatement().executeQuery(query);

        int i = -1;

        while(set.next())
        {
            i = set.getInt("trainingTypeId");
        }

        set.close();

        this.getStatement().close();

        this.setStatement();

        Hierarchy hierarchy = (Hierarchy) this.getProperty().getData().get("courseTypeHierarchy");

        query = "INSERT INTO courseSubType (courseTypeId, trainingTypeId) VALUES(" + hierarchy.getProperty().getData().get("id") + ", " + i + ")";

        this.getStatement().executeUpdate(query);
    }

    @Override
    public void deleteFromDatabase() throws SQLException {

        String query = "DELETE FROM courseSubType WHERE trainingTypeId = " + this.getProperty().getData().get("id");

        this.getStatement().executeUpdate(query);

        this.getStatement().close();

        this.setStatement();

        query = "DELETE FROM trainingTypes WHERE trainingTypeId = " + this.getProperty().getData().get("id");

        this.getStatement().executeUpdate(query);
    }

    @Override
    public void updateInDatabase() throws SQLException {
        String query = "UPDATE trainingTypes SET trainingTypeTitle = '" + this.getProperty().getData().get("name") + "' WHERE trainingTypeId = " + this.getProperty().getData().get("id");

        this.getStatement().executeUpdate(query);

        this.getStatement().close();

        query = "DELETE FROM courseSubType WHERE trainingTypeId = " + this.getProperty().getData().get("id");

        this.setStatement();
        this.getStatement().executeUpdate(query);

        this.getStatement().close();

        Hierarchy hierarchy = (Hierarchy) this.getProperty().getData().get("courseTypeHierarchy");

        this.setStatement();

        query = "INSERT INTO courseSubType (courseTypeId, trainingTypeId) VALUES(" + hierarchy.getProperty().getData().get("id") + ", " + this.getProperty().getData().get("id") + ")";

        this.getStatement().executeUpdate(query);

    }

    @Override
    public List<Hierarchy> getAll() throws SQLException {
        List<Hierarchy> hierarchies = new ArrayList<>();

        String query = "SELECT * FROM trainingTypes";

        ResultSet set = this.getStatement().executeQuery(query);

        while (set.next())
        {
            HashMap<String, Object> data = new HashMap<>();

            Hierarchy hierarchy = HierarchyFactory.getHierarchy("Training Type");

            data.put("name", set.getString("trainingTypeTitle"));
            data.put("id", set.getInt("trainingTypeId"));

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
