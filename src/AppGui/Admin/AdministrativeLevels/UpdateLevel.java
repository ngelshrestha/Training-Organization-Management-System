package AppGui.Admin.AdministrativeLevels;

import Admin.Hierarchy;
import JDBCConnection.JDBCConnectionPool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by angel on 3/13/17.
 */
public class UpdateLevel {

    protected JPanel appForm;
    private JTextField levelName;
    private JButton updateButton;
    private JButton backButton;

    private JDBCConnectionPool pool;
    private Hierarchy hierarchy;

    private JFrame topFrame;

    public UpdateLevel(JDBCConnectionPool _pool, Hierarchy _hierarchy)
    {
        pool = _pool;
        hierarchy = _hierarchy;

        init();


        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    updateButtonListener();
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
        initLevel();
    }

    private void initLevel()
    {
        levelName.setText((String) hierarchy.getProperty().getData().get("name"));
    }

    private void updateButtonListener() throws SQLException {
        HashMap<String, Object> data = new HashMap<>();

        data.put("name", levelName.getText());

        hierarchy.getProperty().setData(data);

        hierarchy.updateInDatabase();

        JOptionPane.showMessageDialog(null, "Administrative Level Updated");

        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new AdministrativeLevel(pool).appForm);
        topFrame.revalidate();
    }

    private void backButtonListener()
    {
        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new AdministrativeLevel(pool).appForm);
        topFrame.revalidate();
    }
}
