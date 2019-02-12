package AppGui.Admin.TrainingMode;

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
 * Created by angel on 3/14/17.
 */
public class AddTrainingMode {

    protected JPanel appForm;
    private JTextField trainingMode;
    private JButton addButton;
    private JButton backButton;

    private JDBCConnectionPool pool;

    private JFrame topFrame;

    public AddTrainingMode(JDBCConnectionPool _pool)
    {
        pool = _pool;

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection connection = pool.checkOut();

                Hierarchy hierarchy = HierarchyFactory.getHierarchy("Training Mode");
                hierarchy.setConnection(connection);
                try {
                    hierarchy.setStatement();

                    hierarchy.setProperty();

                    HashMap<String, Object> data = new HashMap<>();

                    data.put("name", trainingMode.getText());

                    hierarchy.getProperty().setData(data);

                    hierarchy.addToDatabase();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                pool.checkIn(connection);

                JOptionPane.showMessageDialog(null, "Training Mode Added");

                topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                topFrame.remove(appForm);
                topFrame.setContentPane(new TrainingMode(pool).appForm);
                topFrame.revalidate();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                topFrame.remove(appForm);
                topFrame.setContentPane(new TrainingMode(pool).appForm);
                topFrame.revalidate();
            }
        });
    }
}
