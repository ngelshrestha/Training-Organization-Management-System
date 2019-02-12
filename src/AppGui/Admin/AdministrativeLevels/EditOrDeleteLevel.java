package AppGui.Admin.AdministrativeLevels;

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

/**
 * Created by angel on 3/13/17.
 */
public class EditOrDeleteLevel {

    protected JPanel appForm;
    private JComboBox<Object> levelName;
    private JButton editButton;
    private JButton backButton;
    private JButton deleteButton;

    private JDBCConnectionPool pool;
    private Connection connection;

    private JFrame topFrame;

    public EditOrDeleteLevel(JDBCConnectionPool _pool) throws SQLException {

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
        initLevels();
    }

    private void initLevels() throws SQLException {
        connection = pool.checkOut();

        Hierarchy hierarchy = HierarchyFactory.getHierarchy("Administrative Level");

        hierarchy.setConnection(connection);
        hierarchy.setStatement();

        hierarchy.setProperty();

        for (Hierarchy level:
             hierarchy.getAll()) {
            levelName.addItem(new ComboItem((String) level.getProperty().getData().get("name"), (int)level.getProperty().getData().get("id")));
        }

        levelName.setSelectedIndex(-1);
    }

    public void editButtonListener() throws SQLException {
        Hierarchy hierarchy = HierarchyFactory.getHierarchy("Administrative Level");
        hierarchy.setConnection(connection);
        hierarchy.setStatement();

        hierarchy.setProperty();

        HashMap<String, Object> data = new HashMap<>();

        data.put("name", ((ComboItem) levelName.getSelectedItem()).getValue());
        data.put("id", ((ComboItem)levelName.getSelectedItem()).getLabel());

        hierarchy.getProperty().setData(data);

        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new UpdateLevel(pool, hierarchy).appForm);
        topFrame.revalidate();
    }

    public void deleteButtonListener() throws SQLException {
        Hierarchy hierarchy = HierarchyFactory.getHierarchy("Administrative Level");

        hierarchy.setConnection(connection);
        hierarchy.setStatement();

        hierarchy.setProperty();

        HashMap<String, Object> data = new HashMap<>();

        data.put("name", ((ComboItem) levelName.getSelectedItem()).getValue());
        data.put("id", ((ComboItem)levelName.getSelectedItem()).getLabel());

        hierarchy.getProperty().setData(data);

        hierarchy.deleteFromDatabase();

        JOptionPane.showMessageDialog(null, "Administrative Level Deleted");

        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new AdministrativeLevel(pool).appForm);
        topFrame.revalidate();
    }

    private void backButtonListener()
    {
        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new AdministrativeLevel(pool).appForm);
        topFrame.revalidate();
    }
}
