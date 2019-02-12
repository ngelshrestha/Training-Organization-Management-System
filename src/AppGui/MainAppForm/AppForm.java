package AppGui.MainAppForm;

import JDBCConnection.JDBCConnectionPool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by angel on 3/14/17.
 */
public class AppForm {

    public JPanel appForm;
    private JButton adminButton;
    private JButton userButton;
    private JButton exitButton;
    private JButton directorButton;

    private JDBCConnectionPool pool;

    private JFrame frame;

    public AppForm(JDBCConnectionPool _pool) {

        pool = _pool;

        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                frame.remove(appForm);
                frame.setContentPane(new AdminForm(pool).appForm);
                frame.revalidate();
            }
        });

        userButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                frame.remove(appForm);
                frame.setContentPane(new UserForm(pool).appForm);
                frame.revalidate();
            }
        });

        directorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                frame.remove(appForm);
                frame.setContentPane(new DirectorForm(pool).appForm);
                frame.revalidate();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}
