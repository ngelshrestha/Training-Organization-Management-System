package AppGui.Admin.Package;

import AppGui.MainAppForm.AdminForm;
import JDBCConnection.JDBCConnectionPool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Created by angel on 3/14/17.
 */
public class CoursePackage {

    public JPanel appForm;
    private JButton addPackageButton;
    private JButton editDeletePackageButton;
    private JButton backButton;

    private JDBCConnectionPool pool;

    private JFrame topFrame;

    public CoursePackage(JDBCConnectionPool _pool)
    {
        pool = _pool;

        addPackageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                topFrame.remove(appForm);
                try {
                    topFrame.setContentPane(new AddPackage(pool).appForm);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                topFrame.revalidate();
            }
        });

        editDeletePackageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                topFrame.remove(appForm);
                try {
                    topFrame.setContentPane(new EditOrDeletePackage(pool).appForm);
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
