package AppGui.Admin.CourseType;

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
public class EditOrDeleteCourseType {

    protected JPanel appForm;
    private JComboBox<Object> courseType;
    private JButton editButton;
    private JButton deleteButton;
    private JButton backButton;

    private JDBCConnectionPool pool;
    private Hierarchy hierarchy;

    private JFrame topFrame;

    public EditOrDeleteCourseType(JDBCConnectionPool _pool) throws SQLException {

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
        initCourseType();
    }

    private void initCourseType() throws SQLException {
        Connection connection = pool.checkOut();

        hierarchy = HierarchyFactory.getHierarchy("Course Type");

        hierarchy.setConnection(connection);
        hierarchy.setStatement();

        List<Hierarchy> hierarchies = hierarchy.getAll();

        for (Hierarchy courseTypeHierarchy:
             hierarchies) {
            courseType.addItem(new ComboItem((String)courseTypeHierarchy.getProperty().getData().get("name"), (Integer) courseTypeHierarchy.getProperty().getData().get("id")));
        }

        courseType.setSelectedIndex(-1);

        pool.checkIn(connection);
    }

    private void editButtonListener()
    {
        setHierarchyData();

        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new EditCourseType(hierarchy,pool).appForm);
        topFrame.revalidate();
    }

    private void deleteButtonListener() throws SQLException {
        Connection connection = pool.checkOut();

        setHierarchyData();

        hierarchy.setConnection(connection);
        hierarchy.setStatement();

        hierarchy.deleteFromDatabase();

        pool.checkIn(connection);

        JOptionPane.showMessageDialog(null, "Course Type Deleted");

        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new CourseType(pool).appForm);
        topFrame.revalidate();
    }

    private void setHierarchyData()
    {
        HashMap<String , Object> data = new HashMap<>();

        data.put("name", ((ComboItem)courseType.getSelectedItem()).getValue());
        data.put("id", ((ComboItem)courseType.getSelectedItem()).getLabel());

        hierarchy.setProperty();
        hierarchy.getProperty().setData(data);
    }

    private void backButtonListener()
    {
        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new CourseType(pool).appForm);
        topFrame.revalidate();
    }
}
