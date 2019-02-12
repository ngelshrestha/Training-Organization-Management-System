package AppGui.Admin.User;

import Admin.Hierarchy;
import Admin.HierarchyFactory;
import AppGui.ComboItem;
import AppGui.MainAppForm.AdminForm;
import JDBCConnection.JDBCConnectionPool;
import User.FormGetProperties;
import User.UserProperty;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by angel on 3/15/17.
 */
public class Requests {

    public JPanel appForm;
    private JButton detailsButton;
    private JTable users;
    private JScrollPane usersContainer;
    private JButton backButton;

    private JDBCConnectionPool pool;

    private JFrame topFrame;

    public Requests(JDBCConnectionPool _pool) throws SQLException {

        pool = _pool;

        init();

        users.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2)
            {
                JTable target = (JTable) e.getSource();
                try {
                    userListOnClick(target);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
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
        initUserRequests();
    }

    private void initUserRequests() throws SQLException {
        Connection connection = pool.checkOut();

        List<UserProperty> usersList = FormGetProperties.getUserRequest(connection);

        String [] columns = {"User", "Package", "Requested Date"};

        DefaultTableModel model = new DefaultTableModel(columns,0){
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };

        users.setModel(model);

        for (UserProperty user:
             usersList) {
            Hierarchy hierarchy = user.getCoursePackage();
            Object[] objects = {new ComboItem(user.getUserName(),user.getUserId()), new ComboItem((String)hierarchy.getProperty().getData().get("name"), (Integer) hierarchy.getProperty().getData().get("id")), user.getDate()};
            model.addRow(objects);
        }

        pool.checkIn(connection);
    }

    private void userListOnClick(JTable target) throws SQLException{
        UserProperty user = new UserProperty();

        int row = target.getSelectedRow();

        user.setUserId(((ComboItem)target.getModel().getValueAt(row,0)).getLabel());
        user.setUserName(((ComboItem)target.getModel().getValueAt(row,0)).getValue());

        HashMap<String ,Object> data = new HashMap<>();

        data.put("id", ((ComboItem)target.getModel().getValueAt(row,1)).getLabel());
        data.put("name", ((ComboItem)target.getModel().getValueAt(row, 1)).getValue());

        Hierarchy packageHierarchy = HierarchyFactory.getHierarchy("Package");

        packageHierarchy.setProperty();
        packageHierarchy.getProperty().setData(data);

        user.setCoursePackage(packageHierarchy);

        user.setDate((Date) target.getModel().getValueAt(row,2));

        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new Decision(pool, user).appForm);
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
