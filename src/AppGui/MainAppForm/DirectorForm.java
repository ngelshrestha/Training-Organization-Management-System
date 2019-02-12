package AppGui.MainAppForm;

import AppGui.Director.EnrolledUsersReportForm;
import AppGui.Director.FeedbackReportForm;
import AppGui.User.Feedback;
import JDBCConnection.JDBCConnectionPool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Created by angel on 3/15/17.
 */
public class DirectorForm {

    public JPanel appForm;
    private JButton feedbackReportButton;
    private JButton enrolledUsersReportButton;
    private JButton backButton;

    private JDBCConnectionPool pool;

    private JFrame topFrame;

    public DirectorForm(JDBCConnectionPool _pool) {

        pool = _pool;

        feedbackReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                topFrame.remove(appForm);
                try {
                    topFrame.setContentPane(new FeedbackReportForm(pool).appForm);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                topFrame.revalidate();
            }
        });

        enrolledUsersReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                topFrame.remove(appForm);
                try {
                    topFrame.setContentPane(new EnrolledUsersReportForm(pool).appForm);
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
                topFrame.setContentPane(new AppForm(pool).appForm);
                topFrame.revalidate();
            }
        });
    }
}
