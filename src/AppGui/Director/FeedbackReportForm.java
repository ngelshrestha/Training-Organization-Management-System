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
public class FeedbackReportForm {

    public JPanel appForm;
    private JTable feedback;
    private JButton backButton;
    private JScrollPane tableContainer;

    private JDBCConnectionPool pool;

    private JFrame topFrame;

    public FeedbackReportForm(JDBCConnectionPool _pool) throws SQLException {

        pool = _pool;

        init();

        feedback.addMouseListener(new MouseAdapter() {
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
        initFeedbackList();
    }

    private void initFeedbackList() throws SQLException {
        Report report = ReportFactory.getReport("Feedback",pool);

        List<ReportProperty> reports = report.steps();

        String[] columns = {"User Name", "Package Name", "Message", "Date"};
        DefaultTableModel model = new DefaultTableModel(columns,0){
            @Override
            public boolean isCellEditable(int rows, int columns)
            {
                return false;
            }
        };

        feedback.setModel(model);

        for (ReportProperty singleReport:
             reports) {
            Object[] objects = {singleReport.getReportData().get("userName"), singleReport.getReportData().get("packageName"), singleReport.getReportData().get("message"), singleReport.getReportData().get("date")};
            model.addRow(objects);
        }
    }

    public void backButtonListener()
    {
        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new DirectorForm(pool).appForm);
        topFrame.revalidate();
    }
}
