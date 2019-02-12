package AppGui.Admin.Course;

import AppGui.MainAppForm.AdminForm;
import JDBCConnection.JDBCConnectionPool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Created by angel on 3/10/17.
 */
public class Course {

    public JPanel appForm;
    private JButton addCourseButton;
    private JButton editOrDeleteCourseButton;
    private JButton backButton;
    private JFrame topFrame;
    private JDBCConnectionPool pool;

    public Course(JDBCConnectionPool _pool) {

        pool = _pool;

        addCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                    topFrame.remove(appForm);
                    topFrame.setContentPane(new AddCourse(pool).appForm);
                    topFrame.revalidate();

                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        editOrDeleteCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                topFrame.remove(appForm);
                try {
                    topFrame.setContentPane(new EditOrDeleteCourse(pool).appForm);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                topFrame.revalidate();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                topFrame.remove(appForm);
                topFrame.setContentPane(new AdminForm(pool).appForm);
                topFrame.revalidate();
            }
        });
    }

}
