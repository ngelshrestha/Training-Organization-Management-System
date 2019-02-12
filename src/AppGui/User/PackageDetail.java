package AppGui.User;

import Admin.Hierarchy;
import AppGui.MyDialog;
import Course.CourseProperty;
import JDBCConnection.JDBCConnectionPool;
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
 * Created by angel on 3/15/17.
 */
public class PackageDetail extends Login{

    protected JPanel appForm;
    private JTable courseDetail;
    private JButton backButton;
    private JLabel packageName;
    private JButton leaveButton;
    private JScrollPane tableContainer;

    private JDBCConnectionPool pool;
    private Hierarchy packageHierarchy;
    private Session session;

    private JFrame topFrame;

    public PackageDetail(JDBCConnectionPool _pool, Hierarchy _packageHierarchy) throws SQLException {
        super(_pool);

        pool = _pool;
        packageHierarchy = _packageHierarchy;
        session = getInstance();

        init();

        courseDetail.addMouseListener(new MouseAdapter() {
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

        leaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    leaveButtonListener();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    backButtonListener();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

    }

    private void init() throws SQLException {
        initPackageName();
        initPackageDetail();
    }

    private void initPackageName()
    {
        packageName.setText((String) packageHierarchy.getProperty().getData().get("name"));
    }

    private void initPackageDetail() throws SQLException {
        HashMap<String,Object> data = packageHierarchy.getProperty().getData();

        data.put("courseRequired", 1);

        packageHierarchy.getProperty().setData(data);

        Connection connection = pool.checkOut();

        packageHierarchy.setConnection(connection);
        packageHierarchy.setStatement();

        packageHierarchy = packageHierarchy.getAll().get(0);

        pool.checkIn(connection);

        List<CourseProperty> courseProperties = (List<CourseProperty>) packageHierarchy.getProperty().getData().get("courseIds");

        String[] columns = {"Course Name", "Course Code", "Course Hours", "Course Delivery Mode"};

        DefaultTableModel model = new DefaultTableModel(columns,0){
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        courseDetail.setModel(model);

        for (CourseProperty c:
             courseProperties) {
            Object[] object = {c.getCourseName(), c.getCourseCode(), c.getCourseHours(), c.getTrainingMode().getProperty().getData().get("name")};

            model.addRow(object);
        }

        courseDetail.setRowSelectionAllowed(false);
    }

    private void leaveButtonListener() throws SQLException {
        UserProperty userProperty = new UserProperty();

        userProperty.setUserId(session.getUserId());
        userProperty.setCoursePackage(packageHierarchy);

        UserFunctions leaveCourse = UserFunctionsFactory.getUserFunction("Leave Course", pool, userProperty);

        leaveCourse.steps();

        JOptionPane.showMessageDialog(null,"Course Removed");
        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new MyCourse(pool).appForm);
        topFrame.revalidate();
    }

    private void backButtonListener() throws SQLException {
        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new MyCourse(pool).appForm);
        topFrame.revalidate();
    }
}
