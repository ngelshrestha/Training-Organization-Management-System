package AppGui.Admin.Package;

import Admin.Hierarchy;
import Admin.HierarchyFactory;
import AppGui.ComboItem;
import Course.CourseProperty;
import Course.FormGetProperties;
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
public class EditPackage {

    protected JPanel appForm;
    private JComboBox<Object> level;
    private JTextField packageNameLabel;
    private JList<Object> courses;
    private JButton editButton;
    private JButton backButton;

    private JDBCConnectionPool pool;
    private Hierarchy coursePackage;
    private Hierarchy levelHierarchy;

    private JFrame topFrame;

    public EditPackage(Hierarchy _hierarchy,JDBCConnectionPool _pool) throws SQLException {

        pool = _pool;
        coursePackage = _hierarchy;

        init();

        level.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    courses.removeAll();
                    initCourseList();
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

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backButtonListener();
            }
        });
    }

    private void init() throws SQLException {
        initLevel();
        initPackageName();
        initCourseList();
    }

    private void initLevel() throws SQLException {
        Connection connection = pool.checkOut();

        levelHierarchy = (Hierarchy) coursePackage.getProperty().getData().get("level");
        levelHierarchy.setConnection(connection);
        levelHierarchy.setStatement();

        List<Hierarchy> hierarchies = levelHierarchy.getAll();

        pool.checkIn(connection);

        for (Hierarchy h:
             hierarchies) {
            level.addItem(new ComboItem((String)h.getProperty().getData().get("name"), (Integer) h.getProperty().getData().get("id")));
        }

        System.out.println(levelHierarchy.getProperty().getData().get("id"));

        level.getModel().setSelectedItem(new ComboItem((String)levelHierarchy.getProperty().getData().get("name"), (Integer) levelHierarchy.getProperty().getData().get("id")));
    }

    private void initPackageName()
    {
        packageNameLabel.setText((String) coursePackage.getProperty().getData().get("name"));
    }

    private void initCourseList() throws SQLException {
        Connection connection = pool.checkOut();

        coursePackage.setConnection(connection);
        coursePackage.setStatement();

        Hierarchy hierarchy = coursePackage.getAll().get(0);

        List<CourseProperty> coursePropertiesSelected = (List<CourseProperty>) hierarchy.getProperty().getData().get("courseIds");

        List<Integer> ids = new ArrayList<>();

        for (CourseProperty c:
             coursePropertiesSelected) {
            ids.add(c.getCourseId());
        }

        List<CourseProperty> courseProperties = FormGetProperties.getCourses(connection,((ComboItem)level.getSelectedItem()).getLabel());

        int i[] = new int[ids.size()];
        int j = 0;

        courses.setModel(new DefaultListModel<Object>());
        DefaultListModel<Object> model = (DefaultListModel<Object>) courses.getModel();

        for (CourseProperty course:
                courseProperties) {
            model.addElement(new ComboItem(course.getCourseName(),course.getCourseId()));

            if (ids.contains((course.getCourseId())))
            {
                i[j] = model.size()-1;
                j++;
            }
        }

        courses.setSelectedIndices(i);

        pool.checkIn(connection);
    }

    private void backButtonListener()
    {
        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new CoursePackage(pool).appForm);
        topFrame.revalidate();
    }

    private void editButtonListener() throws SQLException {
        Connection connection = pool.checkOut();

        HashMap<String, Object> data = new HashMap<>();

        data.put("id", ((ComboItem)level.getSelectedItem()).getLabel());

        levelHierarchy.setProperty();
        levelHierarchy.getProperty().setData(data);

        data.clear();

        List<CourseProperty> courseProperties = new ArrayList<>();

        for (Object object:
             courses.getSelectedValuesList()) {
            CourseProperty property = new CourseProperty();

            property.setCourseId(((ComboItem)object).getLabel());
            courseProperties.add(property);
        }

        data.put("id", coursePackage.getProperty().getData().get("id"));
        data.put("name", packageNameLabel.getText());
        data.put("level", levelHierarchy);
        data.put("courseIds", courseProperties);

        coursePackage.setProperty();
        coursePackage.getProperty().setData(data);

        coursePackage.updateInDatabase();

        pool.checkIn(connection);

        JOptionPane.showMessageDialog(null, "Package Updated");

        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new CoursePackage(pool).appForm);
        topFrame.revalidate();
    }
}
