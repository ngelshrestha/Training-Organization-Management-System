package AppGui.Admin.AdministrativeLevels;

import AppGui.MainAppForm.AdminForm;
import JDBCConnection.JDBCConnectionPool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Created by angel on 3/13/17.
 */
public class AdministrativeLevel {

    public JPanel appForm;
    private JButton addAdministrativeLevelButton;
    private JButton editOrDeleteAdministrativeLevelButton;
    private JButton backButton;

    private JDBCConnectionPool pool;

    private JFrame topFrame;

    public AdministrativeLevel(JDBCConnectionPool _pool) {

        pool = _pool;

        addAdministrativeLevelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                topFrame.remove(appForm);
                topFrame.setContentPane(new AddLevel(pool).appForm);
                topFrame.revalidate();
            }
        });

        editOrDeleteAdministrativeLevelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                topFrame.remove(appForm);
                try {
                    topFrame.setContentPane(new EditOrDeleteLevel(pool).appForm);
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
