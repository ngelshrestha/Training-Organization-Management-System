package AppGui.Admin.CourseType;

import Admin.Hierarchy;
import Admin.HierarchyFactory;
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
public class AddCourseType {

    protected JPanel appForm;
    private JTextField courseType;
    private JButton addButton;
    private JButton backButton;

    private JDBCConnectionPool pool;

    private JFrame topFrame;

    public AddCourseType(JDBCConnectionPool _pool) {

        pool = _pool;

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Connection connection = pool.checkOut();

                Hierarchy hierarchy = HierarchyFactory.getHierarchy("Course Type");

                hierarchy.setConnection(connection);
                try {
                    hierarchy.setStatement();
                    hierarchy.setProperty();

                    HashMap<String, Object> data = new HashMap<>();

                    data.put("name", courseType.getText());

                    hierarchy.getProperty().setData(data);

                    hierarchy.addToDatabase();

                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                pool.checkIn(connection);

                JOptionPane.showMessageDialog(null, "Course Type Added");

                topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                topFrame.remove(appForm);
                topFrame.setContentPane(new CourseType(pool).appForm);
                topFrame.revalidate();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                topFrame.remove(appForm);
                topFrame.setContentPane(new CourseType(pool).appForm);
                topFrame.revalidate();
            }
        });
    }
}
