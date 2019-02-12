package AppGui.User;

import Admin.Hierarchy;
import Admin.HierarchyFactory;
import AppGui.ComboItem;
import AppGui.MyDialog;
import Course.CourseProperty;
import JDBCConnection.JDBCConnectionPool;
import User.FormGetProperties;
import UserFunctions.UserFunctions;
import UserFunctions.UserFunctionsFactory;
import User.UserProperty;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by angel on 3/14/17.
 */
public class RequestCourse extends Login{

    public JPanel appForm;
    private JComboBox<Object> packageList;
    private JButton requestButton;
    private JButton backButton;
    private JTable courses;
    private JLabel packageListLabel;
    private JLabel coursesLabel;
    private JScrollPane tableContainer;

    private JDBCConnectionPool pool;
    private Session session;
    private UserProperty property;
    private Hierarchy packageHierarchy;

    private JFrame topFrame;

    public RequestCourse(JDBCConnectionPool _pool) throws SQLException {
        super(_pool);

        pool = _pool;
        session = getInstance();
        init();

        packageList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                courses.removeAll();
                try {
                    packageListListener();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        courses.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2)
                {
                    JTable target = (JTable) e.getSource();

                    String output = MyDialog.getOutput(target);

                    JOptionPane.showMessageDialog(null,output);
                }
            }
        });

        requestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    requestButtonListener();
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
        initPackageList();
    }

    private void initPackageList() throws SQLException {
        Connection connection = pool.checkOut();

        property = new UserProperty();

        property.setUserId(session.getUserId());

        property = FormGetProperties.getUserLevel(connection, property);

        packageHierarchy = HierarchyFactory.getHierarchy("Package");

        packageHierarchy.setConnection(connection);
        packageHierarchy.setStatement();

        packageHierarchy.setProperty();

        HashMap<String, Object> data = new HashMap<>();

        data.put("courseRequired", 0);
        data.put("level", property.getLevel());

        packageHierarchy.getProperty().setData(data);

        List<Hierarchy> hierarchies = packageHierarchy.getAll();

        pool.checkIn(connection);

        for (Hierarchy h:
             hierarchies) {
            packageList.addItem(new ComboItem((String) h.getProperty().getData().get("name"), (int)h.getProperty().getData().get("id")));
        }

        packageList.setSelectedIndex(-1);

        courses.setVisible(false);
        coursesLabel.setVisible(false);
        tableContainer.setVisible(false);
    }

    private void packageListListener() throws SQLException {
        HashMap<String, Object> data = new HashMap<>();

        data.put("courseRequired", 1);
        data.put("id", ((ComboItem)packageList.getSelectedItem()).getLabel());

        packageHierarchy.getProperty().setData(data);

        packageHierarchy = packageHierarchy.getAll().get(0);

        List<CourseProperty> courseProperties = (List<CourseProperty>) packageHierarchy.getProperty().getData().get("courseIds");

        String[] columns = {"Course Name", "Course Code", "Course Hours"};

        DefaultTableModel model = new DefaultTableModel(columns,0){
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };

        courses.setModel(model);

        for (CourseProperty course:
             courseProperties) {
            Object[] row = {course.getCourseName(),course.getCourseCode(),course.getCourseHours()};
            model.addRow(row);
        }

        courses.setRowSelectionAllowed(false);

        if (courseProperties.size() > 0)
        {
            tableContainer.setVisible(true);
            courses.setVisible(true);
            coursesLabel.setVisible(true);
        } else {
            tableContainer.setVisible(false);
            courses.setVisible(false);
            coursesLabel.setVisible(false);
        }
    }

    private void backButtonListener()
    {
        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new UserSession(pool).appForm);
        topFrame.revalidate();
    }

    private void requestButtonListener() throws SQLException {
        property.setCoursePackage(packageHierarchy);

        UserFunctions function = UserFunctionsFactory.getUserFunction("Request Course", pool, property);

        function.steps();
        FormGetProperties.removeUser();

        JOptionPane.showMessageDialog(null,"Course Request Successful");

        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new UserSession(pool).appForm);
        topFrame.revalidate();
    }
}
