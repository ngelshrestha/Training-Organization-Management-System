package AppGui.Admin.TrainingType;

import Admin.Hierarchy;
import Admin.HierarchyFactory;
import AppGui.ComboItem;
import JDBCConnection.JDBCConnectionPool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by angel on 3/13/17.
 */
public class AddTrainingType {

    protected JPanel appForm;
    private JTextField trainingTypeName;
    private JButton addButton;
    private JButton backButton;
    private JComboBox<Object> courseType;
    private JLabel courseTypeLabel;

    private JDBCConnectionPool pool;

    private JFrame topFrame;

    public AddTrainingType(JDBCConnectionPool _pool) throws SQLException {

        pool = _pool;

        init();

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection connection = pool.checkOut();

                Hierarchy hierarchy = HierarchyFactory.getHierarchy("Training Type");

                hierarchy.setConnection(connection);
                try {
                    hierarchy.setStatement();

                    hierarchy.setProperty();

                    HashMap<String, Object> data = new HashMap<>();

                    Hierarchy courseTypeHierarchy = HierarchyFactory.getHierarchy("Course Type");

                    data.put("name", ((ComboItem)courseType.getSelectedItem()).getValue());
                    data.put("id", ((ComboItem)courseType.getSelectedItem()).getLabel());

                    courseTypeHierarchy.setProperty();
                    courseTypeHierarchy.getProperty().setData(data);

                    data.clear();

                    data.put("name", trainingTypeName.getText());
                    data.put("courseTypeHierarchy", courseTypeHierarchy);

                    hierarchy.getProperty().setData(data);

                    hierarchy.addToDatabase();

                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                pool.checkIn(connection);

                JOptionPane.showMessageDialog(null, "Training Type Added");

                topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                topFrame.remove(appForm);
                topFrame.setContentPane(new TrainingType(pool).appForm);
                topFrame.revalidate();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
                topFrame.remove(appForm);
                topFrame.setContentPane(new TrainingType(pool).appForm);
                topFrame.revalidate();
            }
        });
    }

    private void init() throws SQLException {
        initCourseType();
    }

    private void initCourseType() throws SQLException {
        Connection connection = pool.checkOut();
        Hierarchy hierarchy = HierarchyFactory.getHierarchy("Course Type");

        hierarchy.setConnection(connection);
        hierarchy.setStatement();

        List<Hierarchy> hierarchies = hierarchy.getAll();

        pool.checkIn(connection);

        for (Hierarchy courseTypeHierarchy:
             hierarchies) {
            courseType.addItem(new ComboItem((String) courseTypeHierarchy.getProperty().getData().get("name"), (int) courseTypeHierarchy.getProperty().getData().get("id")));
        }

        courseType.setSelectedIndex(-1);
    }
}
