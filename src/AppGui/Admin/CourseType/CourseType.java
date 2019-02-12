package AppGui.Admin.CourseType;

import AppGui.MainAppForm.AdminForm;
import JDBCConnection.JDBCConnectionPool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Created by angel on 3/14/17.
 */
public class CourseType {

    public JPanel appForm;
    private JButton addCourseTypeButton;
    private JButton editDeleteCourseTypeButton;
    private JButton backButton;

    private JDBCConnectionPool pool;

    private JFrame topFrame;

    public CourseType(JDBCConnectionPool _pool) {

        pool = _pool;

        addCourseTypeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                topFrame.remove(appForm);
                topFrame.setContentPane(new AddCourseType(pool).appForm);
                topFrame.revalidate();
            }
        });

        editDeleteCourseTypeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                topFrame.remove(appForm);
                try {
                    topFrame.setContentPane(new EditOrDeleteCourseType(pool).appForm);
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
