package AppGui.User;

import Admin.Hierarchy;
import Admin.HierarchyFactory;
import AppGui.ComboItem;
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
import java.util.HashMap;
import java.util.List;

/**
 * Created by angel on 3/14/17.
 */
public class Feedback extends Login{

    public JPanel appForm;
    private JTextArea message;
    private JButton submitButton;
    private JButton backButton;
    private JComboBox<Object> packageList;

    private JDBCConnectionPool pool;
    private Session session;

    private JFrame topFrame;

    public Feedback(JDBCConnectionPool _pool) throws SQLException {
        super(_pool);

        pool = _pool;
        session = getInstance();

        init();

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    submitButtonListener();
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
        initPackageList();
    }

    private void initPackageList() throws SQLException {
        Connection connection = pool.checkOut();

        UserProperty property = new UserProperty();

        property.setUserId(session.getUserId());

        List<Hierarchy> packages = FormGetProperties.getPackages(connection, property);

        pool.checkIn(connection);

        for (Hierarchy h:
             packages) {
            packageList.addItem(new ComboItem((String) h.getProperty().getData().get("name"), (int)h.getProperty().getData().get("id")));
        }

        packageList.setSelectedIndex(-1);
    }

    private void submitButtonListener() throws SQLException {
        UserProperty userProperty = new UserProperty();

        Hierarchy packageHierarchy = HierarchyFactory.getHierarchy("Package");

        HashMap<String, Object> data = new HashMap<>();

        data.put("id", ((ComboItem)packageList.getSelectedItem()).getLabel());

        packageHierarchy.setProperty();
        packageHierarchy.getProperty().setData(data);

        userProperty.setUserId(session.getUserId());
        userProperty.setCoursePackage(packageHierarchy);
        userProperty.setMessage(message.getText());

        UserFunctions giveFeedback = UserFunctionsFactory.getUserFunction("Give Feedback", pool, userProperty);

        giveFeedback.steps();

        JOptionPane.showMessageDialog(null,"Feedback Submitted. Thank you for your time!!");

        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new UserSession(pool).appForm);
        topFrame.revalidate();
    }

    private void backButtonListener()
    {
        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new UserSession(pool).appForm);
        topFrame.revalidate();
    }
}
