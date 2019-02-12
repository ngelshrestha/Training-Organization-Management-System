package AppGui.User;

import JDBCConnection.JDBCConnectionPool;
import User.PasswordProperty;
import User.UserProperty;
import UserFunctions.UserFunctions;
import UserFunctions.UserFunctionsFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Created by angel on 3/17/17.
 */
public class ChangePassword extends Login{

    protected JPanel appForm;
    private JPasswordField oldPassword;
    private JPasswordField newPassword;
    private JPasswordField confirmPassword;
    private JButton changeButton;
    private JButton backButton;

    private JDBCConnectionPool pool;
    private Session session;

    private JFrame topFrame;

    public ChangePassword(JDBCConnectionPool _pool) {
        super(_pool);

        pool = _pool;
        session = getInstance();

        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    changeButtonListener();
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

    private void changeButtonListener() throws SQLException {
        UserProperty property = new UserProperty();

        PasswordProperty passwordProperty = new PasswordProperty();

        passwordProperty.setOldPassword(String.valueOf(oldPassword.getPassword()));
        passwordProperty.setPassword(String.valueOf(newPassword.getPassword()));
        passwordProperty.setConfirmPassword(String.valueOf(confirmPassword.getPassword()));

        property.setUserId(session.getUserId());
        property.setPasswordProperty(passwordProperty);

        UserFunctions changePassword  = UserFunctionsFactory.getUserFunction("Change Password", pool, property);

        changePassword.steps();

        if (!changePassword.isFunctionExecuted())
        {
            JOptionPane.showMessageDialog(null, changePassword.getError());
        } else {
            JOptionPane.showMessageDialog(null, "Password Changed Successfully");

            topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
            topFrame.remove(appForm);
            topFrame.setContentPane(new EditProfile(pool).appForm);
            topFrame.revalidate();
        }
    }

    private void backButtonListener()
    {
        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new UserSession(pool).appForm);
        topFrame.revalidate();
    }
}
