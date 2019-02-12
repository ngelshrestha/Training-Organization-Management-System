package AppGui.Admin.Package;

import Admin.Hierarchy;
import Admin.HierarchyFactory;
import AppGui.ComboItem;
import JDBCConnection.JDBCConnectionPool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by angel on 3/14/17.
 */
public class EditOrDeletePackage {

    protected JPanel appForm;
    private JComboBox<Object> level;
    private JComboBox<Object> packageName;
    private JButton editButton;
    private JButton deleteButton;
    private JButton backButton;
    private JLabel packageNameLabel;

    private JDBCConnectionPool pool;
    private Hierarchy levelHierarchy;
    private Hierarchy coursePackage;

    private JFrame topFrame;

    public EditOrDeletePackage(JDBCConnectionPool _pool) throws SQLException {

        pool = _pool;

        init();

        level.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    packageName.removeAllItems();
                    levelListener();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

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
        initLevel();
    }

    private void initLevel() throws SQLException {
        Connection connection = pool.checkOut();

        levelHierarchy = HierarchyFactory.getHierarchy("Administrative Level");

        levelHierarchy.setConnection(connection);
        levelHierarchy.setStatement();

        List<Hierarchy> hierarchies = levelHierarchy.getAll();

        pool.checkIn(connection);

        for (Hierarchy levelHierarchyList:
             hierarchies) {
            level.addItem(new ComboItem((String)levelHierarchyList.getProperty().getData().get("name"), (Integer) levelHierarchyList.getProperty().getData().get("id")));
        }

        level.setSelectedIndex(-1);
        packageName.setVisible(false);
        packageNameLabel.setVisible(false);
    }

    private void levelListener() throws SQLException {
        Connection connection = pool.checkOut();

        coursePackage = HierarchyFactory.getHierarchy("Package");

        coursePackage.setConnection(connection);
        coursePackage.setStatement();

        HashMap<String,Object> data = new HashMap<>();

        data.put("name", ((ComboItem)level.getSelectedItem()).getValue());
        data.put("id",((ComboItem)level.getSelectedItem()).getLabel());

        levelHierarchy.setProperty();
        levelHierarchy.getProperty().setData(data);

        data.clear();

        data.put("level", levelHierarchy);
        data.put("courseRequired", 0);

        coursePackage.setProperty();

        coursePackage.getProperty().setData(data);

        List<Hierarchy> hierarchies = coursePackage.getAll();

        pool.checkIn(connection);

        for (Hierarchy h:
             hierarchies) {
            packageName.addItem(new ComboItem((String)h.getProperty().getData().get("name"), (Integer) h.getProperty().getData().get("id")));
        }

        packageName.setSelectedIndex(-1);

        if (hierarchies.size() > 0)
        {
            packageName.setVisible(true);
            packageNameLabel.setVisible(true);
        } else {
            packageName.setVisible(false);
            packageNameLabel.setVisible(false);
        }
    }

    private void editButtonListener() throws SQLException {

        HashMap<String, Object> data = new HashMap<>();

        data.put("name", ((ComboItem)level.getSelectedItem()).getValue());
        data.put("id", ((ComboItem)level.getSelectedItem()).getLabel());

        levelHierarchy.setProperty();
        levelHierarchy.getProperty().setData(data);

        data.clear();

        data.put("name", ((ComboItem)packageName.getSelectedItem()).getValue());
        data.put("id", ((ComboItem)packageName.getSelectedItem()).getLabel());
        data.put("level", levelHierarchy);
        data.put("courseRequired", 1);

        coursePackage.setProperty();
        coursePackage.getProperty().setData(data);

        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new EditPackage(coursePackage,pool).appForm);
        topFrame.revalidate();
    }

    private void deleteButtonListener() throws SQLException {
        Connection connection = pool.checkOut();

        HashMap<String,Object> data = new HashMap<>();

        data.put("name", ((ComboItem)packageName.getSelectedItem()).getValue());
        data.put("id", ((ComboItem)packageName.getSelectedItem()).getLabel());

        coursePackage.setProperty();
        coursePackage.getProperty().setData(data);

        coursePackage.setConnection(connection);
        coursePackage.setStatement();

        coursePackage.deleteFromDatabase();

        pool.checkIn(connection);

        JOptionPane.showMessageDialog(null, "Package Deleted");

        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new CoursePackage(pool).appForm);
        topFrame.revalidate();

    }

    private void backButtonListener()
    {
        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new CoursePackage(pool).appForm);
        topFrame.revalidate();
    }
}
