package AppGui.Admin.AdministrativeLevels;

import Admin.Hierarchy;
import Admin.HierarchyFactory;
import JDBCConnection.JDBCConnectionPool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by angel on 3/13/17.
 */
public class AddLevel {

    protected JPanel appForm;
    private JTextField levelName;
    private JButton addButton;
    private JButton backButton;

    private JDBCConnectionPool pool;
    private Connection connection;

    private JFrame topFrame;

    public AddLevel(JDBCConnectionPool _pool) {

        pool = _pool;

        addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                connection = pool.checkOut();

                Hierarchy hierarchy = HierarchyFactory.getHierarchy("Administrative Level");

                hierarchy.setConnection(connection);
                try {
                    hierarchy.setStatement();

                    hierarchy.setProperty();

                    HashMap<String, Object> data = new HashMap<>();

                    data.put("name", levelName.getText());

                    hierarchy.getProperty().setData(data);

                    hierarchy.addToDatabase();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                pool.checkIn(connection);

                JOptionPane.showMessageDialog(null, "Administrative Level Added");

                topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                topFrame.remove(appForm);
                topFrame.setContentPane(new AdministrativeLevel(pool).appForm);
                topFrame.revalidate();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backButtonListener();
            }
        });
    }

    private void backButtonListener()
    {
        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new AdministrativeLevel(pool).appForm);
        topFrame.revalidate();
    }
}
