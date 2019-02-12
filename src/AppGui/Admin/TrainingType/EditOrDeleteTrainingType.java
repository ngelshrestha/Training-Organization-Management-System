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
 * Created by angel on 3/13/17.
 */
public class EditOrDeleteTrainingType {

    protected JPanel appForm;
    private JComboBox<Object> trainingTypeName;
    private JButton editButton;
    private JButton deleteButton;
    private JButton backButton;

    private JDBCConnectionPool pool;
    private Hierarchy hierarchy;

    private JFrame topFrame;

    public EditOrDeleteTrainingType(JDBCConnectionPool _pool) throws SQLException {

        pool = _pool;

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
        initTrainingType();
    }

    private void initTrainingType() throws SQLException {
        Connection connection = pool.checkOut();
        hierarchy = HierarchyFactory.getHierarchy("Training Type");

        hierarchy.setConnection(connection);
        hierarchy.setStatement();

        List<Hierarchy> hierarchies = hierarchy.getAll();

        for (Hierarchy trainingTypeHierarchy:
             hierarchies) {
            trainingTypeName.addItem(new ComboItem((String)trainingTypeHierarchy.getProperty().getData().get("name"), (Integer) trainingTypeHierarchy.getProperty().getData().get("id")));
        }

        trainingTypeName.setSelectedIndex(-1);
        pool.checkIn(connection);
    }

    private void deleteButtonListener() throws SQLException {
        Connection connection = pool.checkOut();

        hierarchy.setConnection(connection);
        hierarchy.setStatement();

        setTrainingTypeHierarchy();

        hierarchy.deleteFromDatabase();

        pool.checkIn(connection);

        JOptionPane.showMessageDialog(null, "Training Type Deleted");

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

    private void editButtonListener() throws SQLException {

        Connection connection = pool.checkOut();

        hierarchy.setConnection(connection);
        hierarchy.setStatement();

        setTrainingTypeHierarchy();

        pool.checkIn(connection);

        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new EditTrainingType(hierarchy,pool).appForm);
        topFrame.revalidate();
    }

    private void setTrainingTypeHierarchy()
    {
        hierarchy.setProperty();

        HashMap<String, Object> data = new HashMap<>();

        data.put("name", ((ComboItem)trainingTypeName.getSelectedItem()).getValue());
        data.put("id", ((ComboItem)trainingTypeName.getSelectedItem()).getLabel());

        hierarchy.getProperty().setData(data);
    }
}
