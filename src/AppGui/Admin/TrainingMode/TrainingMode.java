package AppGui.Admin.TrainingMode;

import AppGui.MainAppForm.AdminForm;
import JDBCConnection.JDBCConnectionPool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Created by angel on 3/14/17.
 */
public class TrainingMode {

    public JPanel appForm;
    private JButton addTrainingModeButton;
    private JButton editDeleteTrainingModeButton;
    private JButton backButton;

    private JDBCConnectionPool pool;

    private JFrame topFrame;

    public TrainingMode(JDBCConnectionPool _pool) {

        pool = _pool;

        addTrainingModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                topFrame.remove(appForm);
                topFrame.setContentPane(new AddTrainingMode(pool).appForm);
                topFrame.revalidate();
            }
        });

        editDeleteTrainingModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                topFrame.remove(appForm);
                try {
                    topFrame.setContentPane(new EditOrDeleteTrainingMode(pool).appForm);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                topFrame.revalidate();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                topFrame.remove(appForm);
                topFrame.setContentPane(new AdminForm(pool).appForm);
                topFrame.revalidate();
            }
        });
    }
}
