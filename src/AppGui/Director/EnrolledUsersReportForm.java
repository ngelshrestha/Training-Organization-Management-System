package AppGui.Director;

import AppGui.MainAppForm.DirectorForm;
import AppGui.MyDialog;
import Director.Report;
import Director.ReportFactory;
import Director.ReportProperty;
import JDBCConnection.JDBCConnectionPool;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by angel on 3/15/17.
 */
public class EnrolledUsersReportForm {

    public JPanel appForm;
    private JTable enrolledUsersRequests;
    private JButton backButton;
    private JScrollPane tableContainer;
    private JTable enrolledStudents;
    private JScrollPane enrolledTableContainer;

    private JDBCConnectionPool pool;
    List<ReportProperty> reportProperties;
    String[] columns = {"User Name", "Package Name","Date"};

    private JFrame topFrame;

    public EnrolledUsersReportForm(JDBCConnectionPool _pool) throws SQLException {

        pool = _pool;

        init();

        enrolledUsersRequests.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2)
                {
                    JTable target = (JTable) e.getSource();

                    String output = MyDialog.getOutput(target);

                    JOptionPane.showMessageDialog(null,output);
                }
            }
        });

        enrolledStudents.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2)
                {
                    JTable target = (JTable) e.getSource();

                    String output = MyDialog.getOutput(target);

                    JOptionPane.showMessageDialog(null,output);
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
        initList();
        initEnrolledUsersList();
        initUserRequestList();
    }

    private void initList() throws SQLException {
        Report report = ReportFactory.getReport("User", pool);

        reportProperties = report.steps();
    }

    private void initEnrolledUsersList() throws SQLException {
        DefaultTableModel model = new DefaultTableModel(columns,0){
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };

        enrolledStudents.setModel(model);

        for (ReportProperty property:
             reportProperties) {
            if (property.getReportData().get("purchaseStatus").equals("Accepted"))
            {
                Object[] object = {property.getReportData().get("userName"), property.getReportData().get("packageName"), property.getReportData().get("date")};
                model.addRow(object);
            }
        }

        enrolledStudents.setFillsViewportHeight(true);
    }

    private void initUserRequestList()
    {
        DefaultTableModel model = new DefaultTableModel(columns,0){
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };

        enrolledUsersRequests.setModel(model);

        for (ReportProperty property:
                reportProperties) {
            if (property.getReportData().get("purchaseStatus").equals("Requested"))
            {
                Object[] object = {property.getReportData().get("userName"), property.getReportData().get("packageName"), property.getReportData().get("date")};
                model.addRow(object);
            }
        }

        enrolledUsersRequests.setFillsViewportHeight(true);
    }

    private void backButtonListener()
    {
        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new DirectorForm(pool).appForm);
        topFrame.revalidate();
    }
}
