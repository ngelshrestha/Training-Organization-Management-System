package AppGui.MainAppForm;

import AppGui.User.*;
import JDBCConnection.JDBCConnectionPool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Created by angel on 3/14/17.
 */
public class UserForm {

    public JPanel appForm;
    private JButton loginButton;
    private JButton registerButton;
    private JButton backButton;
    private JButton requestCourseButton;


    private JDBCConnectionPool pool;

    private JFrame topFrame;

    public UserForm(JDBCConnectionPool _pool)
    {
        pool = _pool;

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                topFrame.remove(appForm);
                topFrame.setContentPane(new Login(pool).appForm);
                topFrame.revalidate();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                topFrame.remove(appForm);
                try {
                    topFrame.setContentPane(new Register(pool).appForm);
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
