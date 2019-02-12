package AppGui.Admin.TrainingMode;

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
public class EditOrDeleteTrainingMode {

    protected JPanel appForm;
    private JComboBox<Object> trainingMode;
    private JButton editButton;
    private JButton deleteButton;
    private JButton backButton;

    private JDBCConnectionPool pool;
    private Hierarchy trainingModeHierarchy;

    private JFrame topFrame;

    public EditOrDeleteTrainingMode(JDBCConnectionPool _pool) throws SQLException {
        pool = _pool;

        init();

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                  editButtonListener();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    deleteButtonListener();
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
        initTrainingModes();
    }

    private void initTrainingModes() throws SQLException {
        Connection connection = pool.checkOut();

        trainingModeHierarchy = HierarchyFactory.getHierarchy("Training Mode");

        trainingModeHierarchy.setConnection(connection);
        trainingModeHierarchy.setStatement();

        List<Hierarchy> hierarchies = trainingModeHierarchy.getAll();

        for (Hierarchy hierarchy:
             hierarchies) {
            trainingMode.addItem(new ComboItem((String) hierarchy.getProperty().getData().get("name"), (int)hierarchy.getProperty().getData().get("id")));
        }

        trainingMode.setSelectedIndex(-1);

        pool.checkIn(connection);
    }

    public void editButtonListener()
    {
        setHierarchyData();

        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new EditTrainingMode(trainingModeHierarchy,pool).appForm);
        topFrame.revalidate();
    }

    public void deleteButtonListener() throws SQLException {
        HashMap<String, Object> data = new HashMap<>();

        data.put("name", ((ComboItem)trainingMode.getSelectedItem()).getValue());
        data.put("id", ((ComboItem)trainingMode.getSelectedItem()).getLabel());

        trainingModeHierarchy.setProperty();
        trainingModeHierarchy.getProperty().setData(data);

        trainingModeHierarchy.deleteFromDatabase();

        JOptionPane.showMessageDialog(null, "Training Mode Deleted");

        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new TrainingMode(pool).appForm);
        topFrame.revalidate();
    }

    private void setHierarchyData()
    {
        HashMap<String, Object> data = new HashMap<>();

        data.put("name", ((ComboItem)trainingMode.getSelectedItem()).getValue());
        data.put("id", ((ComboItem)trainingMode.getSelectedItem()).getLabel());

        trainingModeHierarchy.setProperty();
        trainingModeHierarchy.getProperty().setData(data);
    }

    private void backButtonListener()
    {
        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new TrainingMode(pool).appForm);
        topFrame.revalidate();
    }
}
