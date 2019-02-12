package AppGui.User;

import Admin.Hierarchy;
import AppGui.ComboItem;
import JDBCConnection.JDBCConnectionPool;
import User.FormGetProperties;
import User.UserProperty;
import UserFunctions.UserFunctions;
import UserFunctions.UserFunctionsFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by angel on 3/15/17.
 */
public class EditProfile extends Login{

    protected JPanel appForm;
    private JTextField userName;
    private JTextField emailId;
    private JComboBox<Object> level;
    private JTextField phoneNo;
    private JButton editButton;
    private JButton backButton;
    private JButton changePasswordButton;

    private JDBCConnectionPool pool;
    private UserProperty property;
    private Session session;

    private JFrame topFrame;

    public EditProfile(JDBCConnectionPool _pool) throws SQLException {
        super(_pool);

        pool = _pool;
        session = getInstance();

        init();

        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePasswordButtonListener();
            }
        });

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

    private void init() throws SQLException {
        initDetails();
        initLevels();
    }

    private void initDetails() throws SQLException {
        property = new UserProperty();

        property.setUserId(session.getUserId());

        FormGetProperties.setUser(property);

        Connection connection = pool.checkOut();
        property = FormGetProperties.getUserRequest(connection).get(0);

        pool.checkIn(connection);

        userName.setText(property.getUserName());
        emailId.setText(property.getEmail());
        phoneNo.setText(String.valueOf(property.getPhoneNo()));
    }

    private void initLevels() throws SQLException {
        Connection connection = pool.checkOut();

        Hierarchy hierarchy = property.getLevel();

        hierarchy.setConnection(connection);
        hierarchy.setStatement();

        List<Hierarchy> hierarchies = hierarchy.getAll();

        pool.checkIn(connection);

        for (Hierarchy levels:
             hierarchies) {
            level.addItem(new ComboItem((String) levels.getProperty().getData().get("name"), (int)levels.getProperty().getData().get("id")));
        }
        level.getModel().setSelectedItem(new ComboItem((String)hierarchy.getProperty().getData().get("name"), (Integer) hierarchy.getProperty().getData().get("id")));
    }

    private void changePasswordButtonListener()
    {
        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new ChangePassword(pool).appForm);
        topFrame.revalidate();
    }

    private void editButtonListener() throws SQLException {

        int i = 0;
        HashMap<String, Object> data = new HashMap<>();

        if ((int)property.getLevel().getProperty().getData().get("id") != ((ComboItem)level.getSelectedItem()).getLabel())
        {
            i = JOptionPane.showConfirmDialog(null,"Are you sure? You will lose all your registered courses if you change your level.","Confirm level Change", JOptionPane.YES_NO_OPTION);

            data.put("isChanged", true);

        }

        if (i == JOptionPane.YES_OPTION)
        {
            property.setUserName(userName.getText());
            property.setEmail(emailId.getText());
            property.setPhoneNo(Integer.parseInt(phoneNo.getText()));

            data.put("id", ((ComboItem)level.getSelectedItem()).getLabel());

            property.getLevel().getProperty().setData(data);

            UserFunctions editProfile = UserFunctionsFactory.getUserFunction("Edit Profile", pool, property);

            editProfile.steps();

            JOptionPane.showMessageDialog(null,"Your profile has been successfully edited");

            topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
            topFrame.remove(appForm);
            topFrame.setContentPane(new UserSession(pool).appForm);
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
