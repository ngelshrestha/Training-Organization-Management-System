package AppGui.User;

import Admin.Hierarchy;
import Admin.HierarchyFactory;
import AppGui.ComboItem;
import AppGui.MyDialog;
import JDBCConnection.JDBCConnectionPool;
import Properties.Property;
import Properties.PropertyFactory;
import User.FormGetProperties;
import User.UserProperty;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by angel on 3/14/17.
 */
public class MyCourse extends Login{

    public JPanel appForm;
    private JTable packagesList;
    private JButton backButton;
    private JScrollPane tableContainer;

    private JDBCConnectionPool pool;
    private Session session;

    private JFrame topFrame;

    public MyCourse(JDBCConnectionPool _pool) throws SQLException {
        super(_pool);
        pool = _pool;
        session = getInstance();

        init();

        packagesList.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                JTable target = (JTable) e.getSource();

                if (e.getClickCount() == 2)
                {

                    int column = 0;
                    int row = target.getSelectedRow();

                    try {
                        packageListListener((ComboItem) target.getModel().getValueAt(row,column), (ComboItem) target.getModel().getValueAt(row,1));
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
        initCourses();
    }

    private void initCourses() throws SQLException {
        Connection connection = pool.checkOut();

        UserProperty property = new UserProperty();

        property.setUserId(session.getUserId());

        List<Hierarchy> packages = FormGetProperties.getPackages(connection, property);

        pool.checkIn(connection);

        String[] columns = {"Package Name", "Status"};

        DefaultTableModel model = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };

        packagesList.setModel(model);

        for (Hierarchy h:
             packages) {
            Property purchaseStatus = (Property) h.getProperty().getData().get("userPurchaseStatus");
            Object[] object = {new ComboItem((String) h.getProperty().getData().get("name"), (int)h.getProperty().getData().get("id")), new ComboItem((String) purchaseStatus.getData().get("name"),(int)purchaseStatus.getData().get("id"))};
            model.addRow(object);
        }
    }

    private void packageListListener(ComboItem item, ComboItem purchaseStatus) throws SQLException {
        Hierarchy hierarchy = HierarchyFactory.getHierarchy("Package");

        hierarchy.setProperty();

        HashMap<String , Object> data = new HashMap<>();

        Property property = PropertyFactory.getProperty("Purchase Status");

        data.put("id", purchaseStatus.getLabel());
        data.put("name", purchaseStatus.getValue());

        property.setData(data);

        data.clear();

        data.put("id", item.getLabel());
        data.put("name", item.getValue());
        data.put("userPurchaseStatus", property);

        hierarchy.getProperty().setData(data);

        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new PackageDetail(pool,hierarchy).appForm);
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
