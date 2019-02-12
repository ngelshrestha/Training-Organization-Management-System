package AppGui.Admin.TrainingType;

import Admin.Hierarchy;
import Admin.HierarchyFactory;
import AppGui.ComboItem;
import JDBCConnection.JDBCConnectionPool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by angel on 3/14/17.
 */
public class EditTrainingType {

    protected JPanel appForm;
    private JTextField trainingType;
    private JComboBox<Object> courseType;
    private JButton editButton;
    private JButton backButton;

    private JDBCConnectionPool pool;
    private Hierarchy trainingTypeHierarchy;
    private Hierarchy courseTypeHierarchy;

    private JFrame topFrame;

    public EditTrainingType(Hierarchy _trainingTypeHierarchy, JDBCConnectionPool _pool) throws SQLException {

        pool = _pool;
        trainingTypeHierarchy = _trainingTypeHierarchy;

        init();

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    editButtonListener();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backButtonListener();
            }
        });
    }

    private void init() throws SQLException {
        initTrainingType();
        getCourseType();
        initCourseType();
    }

    private void initTrainingType()
    {
        trainingType.setText((String) trainingTypeHierarchy.getProperty().getData().get("name"));
    }

    private void getCourseType() throws SQLException {

        courseTypeHierarchy = HierarchyFactory.getCourseTrainingTypeRelation(pool,trainingTypeHierarchy);
    }

    private void initCourseType() throws SQLException {
        Connection connection = pool.checkOut();

        courseTypeHierarchy.setConnection(connection);
        courseTypeHierarchy.setStatement();

        List<Hierarchy> hierarchies = courseTypeHierarchy.getAll();

        for (Hierarchy courseTypeHierarchyList:
             hierarchies) {
            courseType.addItem(new ComboItem((String) courseTypeHierarchyList.getProperty().getData().get("name"), (int)courseTypeHierarchyList.getProperty().getData().get("id")));
        }

        courseType.getModel().setSelectedItem(new ComboItem((String) courseTypeHierarchy.getProperty().getData().get("name"), (int)courseTypeHierarchy.getProperty().getData().get("id")));

        pool.checkIn(connection);
    }

    private void editButtonListener() throws SQLException {
        HashMap<String, Object> data = new HashMap<>();

        data.put("name", ((ComboItem)courseType.getSelectedItem()).getValue());
        data.put("id", ((ComboItem)courseType.getSelectedItem()).getLabel());

        courseTypeHierarchy.getProperty().setData(data);

        data.clear();
        data.put("name", trainingType.getText());
        data.put("id", trainingTypeHierarchy.getProperty().getData().get("id"));

        data.put("courseTypeHierarchy", courseTypeHierarchy);

        trainingTypeHierarchy.getProperty().setData(data);

        trainingTypeHierarchy.updateInDatabase();

        JOptionPane.showMessageDialog(null, "Training Type Updated");

        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new TrainingType(pool).appForm);
        topFrame.revalidate();
    }

    private void backButtonListener()
    {
        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new TrainingType(pool).appForm);
        topFrame.revalidate();
    }
}
