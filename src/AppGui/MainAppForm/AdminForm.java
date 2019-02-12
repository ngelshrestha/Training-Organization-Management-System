package AppGui.MainAppForm;

import AppGui.Admin.AdministrativeLevels.AdministrativeLevel;
import AppGui.Admin.Course.Course;
import AppGui.Admin.CourseType.CourseType;
import AppGui.Admin.Package.CoursePackage;
import AppGui.Admin.TrainingMode.TrainingMode;
import AppGui.Admin.TrainingType.TrainingType;
import AppGui.Admin.User.Requests;
import JDBCConnection.JDBCConnectionPool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Created by angel on 3/10/17.
 */
public class AdminForm {

    public JPanel appForm;
    private JButton courseButton;
    private JButton administrativeLevelButton;
    private JButton trainingTypeButton;
    private JButton trainingModeButton;
    private JButton courseTypeButton;
    private JButton backButton;
    private JButton packageButton;
    private JButton userRequestsButton;

    private JDBCConnectionPool pool;

    private JFrame frame;

    public AdminForm(JDBCConnectionPool _pool) {

        pool = _pool;

        courseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                frame.remove(appForm);
                frame.setContentPane(new Course(pool).appForm);
                frame.revalidate();
            }
        });

        administrativeLevelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                frame.remove(appForm);
                frame.setContentPane(new AdministrativeLevel(pool).appForm);
                frame.revalidate();
            }
        });

        trainingTypeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                frame.remove(appForm);
                frame.setContentPane(new TrainingType(pool).appForm);
                frame.revalidate();
            }
        });
        trainingModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                frame.remove(appForm);
                frame.setContentPane(new TrainingMode(pool).appForm);
                frame.revalidate();
            }
        });
        courseTypeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                frame.remove(appForm);
                frame.setContentPane(new CourseType(pool).appForm);
                frame.revalidate();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                frame.remove(appForm);
                frame.setContentPane(new AppForm(pool).appForm);
                frame.revalidate();
            }
        });
        packageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                frame.remove(appForm);
                frame.setContentPane(new CoursePackage(pool).appForm);
                frame.revalidate();
            }
        });
        userRequestsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                frame.remove(appForm);
                try {
                    frame.setContentPane(new Requests(pool).appForm);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                frame.revalidate();
            }
        });
    }
}
