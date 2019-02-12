package AppGui.User;

import Admin.Hierarchy;
import Admin.HierarchyFactory;
import AppGui.ComboItem;
import AppGui.MainAppForm.UserForm;
import JDBCConnection.JDBCConnectionPool;
import UserFunctions.UserFunctions;
import UserFunctions.UserFunctionsFactory;
import User.UserProperty;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by angel on 3/15/17.
 */
public class Register extends Login{

    public JPanel appForm;
    private JTextField emailId;
    private JTextField phoneNo;
    private JTextField userName;
    private JPasswordField password;
    private JComboBox<Object> level;
    private JButton registerButton;
    private JButton backButton;

    private JDBCConnectionPool pool;
    private Session session;
    private Hierarchy hierarchy;

    private JFrame topFrame;

    public Register(JDBCConnectionPool _pool) throws SQLException {
        super(_pool);

        pool = _pool;
        session = getInstance();

        init();

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    registerButtonListener();
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

    public void init() throws SQLException {
        initLevels();
    }

    public void initLevels() throws SQLException {
        Connection connection = pool.checkOut();

        hierarchy = HierarchyFactory.getHierarchy("Administrative Level");

        hierarchy.setConnection(connection);
        hierarchy.setStatement();

        hierarchy.setProperty();

        for (Hierarchy levelHierarchy:
                hierarchy.getAll()) {
            level.addItem(new ComboItem((String) levelHierarchy.getProperty().getData().get("name"), (int)levelHierarchy.getProperty().getData().get("id")));
        }

        level.setSelectedIndex(-1);
    }

    private void registerButtonListener() throws SQLException {
        UserProperty userProperty = new UserProperty();

        userProperty.setUserName(userName.getText());
        userProperty.setEmail(emailId.getText());
        userProperty.setPassword(String.valueOf(password.getPassword()));
        userProperty.setPhoneNo(Integer.parseInt(phoneNo.getText()));

        hierarchy.setProperty();

        HashMap<String, Object> data = new HashMap<>();

        data.put("id", ((ComboItem)level.getSelectedItem()).getLabel());

        hierarchy.getProperty().setData(data);

        userProperty.setLevel(hierarchy);

        UserFunctions registerUser = UserFunctionsFactory.getUserFunction("Register",pool,userProperty);

        registerUser.steps();
        session.setUserId(registerUser.getUserProperty().getUserId());

        JOptionPane.showMessageDialog(null, "Welcome to TOM");

        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new UserSession(pool).appForm);
        topFrame.revalidate();

    }

    private void backButtonListener()
    {
        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new UserForm(pool).appForm);
        topFrame.revalidate();
    }
}
