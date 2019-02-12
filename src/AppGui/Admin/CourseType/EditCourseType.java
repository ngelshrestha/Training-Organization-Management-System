package AppGui.Admin.CourseType;

import Admin.Hierarchy;
import JDBCConnection.JDBCConnectionPool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by angel on 3/14/17.
 */
public class EditCourseType {

    protected JPanel appForm;
    private JTextField courseType;
    private JButton editButton;
    private JButton backButton;

    private JDBCConnectionPool pool;
    private Hierarchy hierarchy;

    private JFrame topFrame;

    public EditCourseType(Hierarchy _hierarchy, JDBCConnectionPool _pool) {

        pool = _pool;
        hierarchy = _hierarchy;

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

    private void init()
    {
        initCourseType();
    }

    private void initCourseType()
    {
        courseType.setText((String) hierarchy.getProperty().getData().get("name"));
    }

    private void editButtonListener() throws SQLException {
        Connection connection = pool.checkOut();

        HashMap<String, Object> data = new HashMap<>();

        data.put("name", courseType.getText());

        hierarchy.getProperty().setData(data);

        hierarchy.setConnection(connection);
        hierarchy.setStatement();

        hierarchy.updateInDatabase();

        pool.checkIn(connection);

        JOptionPane.showMessageDialog(null, "Course Type Updated");

        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new CourseType(pool).appForm);
        topFrame.revalidate();
    }

    private void backButtonListener()
    {
        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new CourseType(pool).appForm);
        topFrame.revalidate();
    }
}
