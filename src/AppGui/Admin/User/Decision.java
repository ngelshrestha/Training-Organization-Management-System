package AppGui.Admin.User;

import AppGui.MainAppForm.AdminForm;
import JDBCConnection.JDBCConnectionPool;
import User.FormGetProperties;
import UserFunctions.UserFunctions;
import UserFunctions.UserFunctionsFactory;
import User.UserProperty;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by angel on 3/16/17.
 */
public class Decision {

    protected JPanel appForm;
    private JButton acceptButton;
    private JButton ignoreButton;
    private JButton backButton;
    private JTextPane details;
    private JScrollPane detailsContainer;

    private JDBCConnectionPool pool;
    private UserProperty property;

    private JFrame topFrame;

    public Decision(JDBCConnectionPool _pool , UserProperty _property) throws SQLException {


        pool = _pool;
        property = _property;

        init();

        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    acceptButtonListener();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        ignoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ignoreButtonListener();
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
        initUserDetails();
    }

    private void initUserDetails() throws SQLException {
        Connection connection = pool.checkOut();

        FormGetProperties.setUser(property);

        property = FormGetProperties.getUserRequest(connection).get(0);

        details.setText("User Name : " + property.getUserName() + "\n");
        details.setText(details.getText() + "Email Id : " + property.getEmail() + "\n");
        details.setText(details.getText() + "Phone no : " + property.getPhoneNo() + "\n");
        details.setText(details.getText() + "Administrative Level : " + property.getLevel().getProperty().getData().get("name") + "\n");
        details.setText(details.getText() + "Package Request : " + property.getCoursePackage().getProperty().getData().get("name") + "\n");
        details.setText(details.getText() + "Requested Date : " + property.getDate() + "\n");

        details.setEditable(false);
    }

    private void acceptButtonListener() throws SQLException {
        UserFunctions acceptUser = UserFunctionsFactory.getUserFunction("User Accept", pool, property);

        acceptUser.steps();

        FormGetProperties.removeUser();

        JOptionPane.showMessageDialog(null,"Purchase Accepted");

        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new Requests(pool).appForm);
        topFrame.revalidate();
    }

    private void ignoreButtonListener() throws SQLException {

        UserFunctions ignoreUser = UserFunctionsFactory.getUserFunction("User Ignore", pool, property);

        ignoreUser.steps();

        FormGetProperties.removeUser();

        JOptionPane.showMessageDialog(null,"Purchase Cancelled");

        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new Requests(pool).appForm);
        topFrame.revalidate();
    }

    private void backButtonListener()
    {
        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new AdminForm(pool).appForm);
        topFrame.revalidate();
    }
}
