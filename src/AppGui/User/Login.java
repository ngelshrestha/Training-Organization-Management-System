package AppGui.User;

import AppGui.MainAppForm.UserForm;
import JDBCConnection.JDBCConnectionPool;
import UserFunctions.UserFunctions;
import UserFunctions.UserLogin;
import User.UserProperty;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Created by angel on 3/15/17.
 */
public class Login extends Session{

    public JPanel appForm;
    private JTextField email;
    private JPasswordField password;
    private JButton loginButton;
    private JButton backButton;

    private JDBCConnectionPool pool;
    private Session session;

    private JFrame topFrame;

    public Login(JDBCConnectionPool _pool) {

        pool = _pool;
        session = getInstance();

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    validateLogin();
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

    }

    private void validateLogin() throws SQLException {
         UserProperty user = new UserProperty();

         user.setEmail(email.getText());
         user.setPassword(String.valueOf(password.getPassword()));

        UserFunctions functions = new UserLogin(pool,user);
        functions.steps();
        user = functions.getUserProperty();

        if (user.getUserId() != 0)
        {
            startSession(user.getUserId());
        }

        if (session.getUserId() != -1)
        {
            topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
            topFrame.remove(appForm);
            topFrame.setContentPane(new UserSession(pool).appForm);
            topFrame.revalidate();
        } else {
            JOptionPane.showMessageDialog(null,"Invalid Login");
        }


    }

    private void startSession(int userId)
    {
        session.setUserId(userId);
    }

    private void backButtonListener()
    {
        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new UserForm(pool).appForm);
        topFrame.revalidate();
    }
}
