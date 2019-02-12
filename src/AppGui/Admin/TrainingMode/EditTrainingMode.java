package AppGui.Admin.TrainingMode;

import Admin.Hierarchy;
import JDBCConnection.JDBCConnectionPool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by angel on 3/14/17.
 */
public class EditTrainingMode {

    protected JPanel appForm;
    private JTextField trainingMode;
    private JButton editButton;
    private JButton backButton;

    private JDBCConnectionPool pool;
    private Hierarchy hierarchy;

    private JFrame topFrame;

    public EditTrainingMode(Hierarchy _hierarchy, JDBCConnectionPool _pool)
    {
        pool = _pool;
        hierarchy = _hierarchy;

        init();

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

    private void init()
    {
        initTrainingMode();
    }

    private void initTrainingMode() {
        trainingMode.setText((String) hierarchy.getProperty().getData().get("name"));
    }

    private void editButtonListener() throws SQLException {
        HashMap<String, Object> data = new HashMap<>();

        data.put("name", trainingMode.getText());
        data.put("id", hierarchy.getProperty().getData().get("id"));

        hierarchy.getProperty().setData(data);

        hierarchy.updateInDatabase();

        JOptionPane.showMessageDialog(null, "Training Mode Updated");

        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new TrainingMode(pool).appForm);
        topFrame.revalidate();
    }

    private void backButtonListener()
    {
        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new TrainingMode(pool).appForm);
        topFrame.revalidate();
    }
}
