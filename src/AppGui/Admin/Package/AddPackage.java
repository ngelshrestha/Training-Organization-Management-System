package AppGui.Admin.Package;

import Admin.Hierarchy;
import Admin.HierarchyFactory;
import AppGui.ComboItem;
import AppGui.MyDialog;
import Course.CourseProperty;
import Course.FormGetProperties;
import JDBCConnection.JDBCConnectionPool;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by angel on 3/14/17.
 */
public class AddPackage {

    protected JPanel appForm;
    private JTextField packageName;
    private JComboBox<Object> level;
    private JTable coursesList;
    private JButton addButton;
    private JButton backButton;
    private JLabel courseListLabel;
    private JScrollPane tableContainer;

    private JDBCConnectionPool pool;
    private Hierarchy levelHierarchy;

    private JFrame topFrame;

    public AddPackage(JDBCConnectionPool _pool) throws SQLException {

        pool = _pool;

        init();

        level.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    coursesList.removeAll();
                    levelSelectListener();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        coursesList.addMouseListener(new MouseAdapter() {
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

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addButtonListener();
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

    private void init() throws SQLException {
        initLevel();
    }

    private void initLevel() throws SQLException {
        Connection connection = pool.checkOut();

        levelHierarchy = HierarchyFactory.getHierarchy("Administrative Level");
        levelHierarchy.setConnection(connection);
        levelHierarchy.setStatement();

        List<Hierarchy> hierarchies = levelHierarchy.getAll();

        pool.checkIn(connection);

        for (Hierarchy levelHierarchy:
             hierarchies) {
            level.addItem(new ComboItem((String) levelHierarchy.getProperty().getData().get("name"),(int)levelHierarchy.getProperty().getData().get("id")));
        }

        level.setSelectedIndex(-1);
        coursesList.setVisible(false);
        courseListLabel.setVisible(false);
        tableContainer.setVisible(false);
    }

//    private void levelSelectListener() throws SQLException {
//        Connection connection = pool.checkOut();
//        List<CourseProperty> courseProperties = FormGetProperties.getCourses(connection,((ComboItem)level.getSelectedItem()).getLabel());
//
//        pool.checkIn(connection);
//
//        coursesList.setModel(new DefaultListModel<Object>());
//        DefaultListModel<Object> model = (DefaultListModel<Object>) coursesList.getModel();
//
//        for (CourseProperty course:
//             courseProperties) {
//            for (Hierarchy hierarchy:
//                 course.getTrainingModeHierarchy()) {
//                model.addElement(new ComboItem(course.getCourseName(),course.getCourseId()));
//            }
//        }
//
//        if (model.size() > 0)
//        {
//            coursesList.setVisible(true);
//            courseListLabel.setVisible(true);
//        } else {
//            coursesList.setVisible(false);
//            courseListLabel.setVisible(false);
//        }
//    }

    private void levelSelectListener() throws SQLException {
        Connection connection = pool.checkOut();
        List<CourseProperty> courseProperties = FormGetProperties.getCourses(connection,((ComboItem)level.getSelectedItem()).getLabel());

        pool.checkIn(connection);

        String[] columns = {"Course Name", "Course Code", "Course Hours", "Training Mode Available"};

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };

        coursesList.setModel(tableModel);


        for (CourseProperty course:
             courseProperties) {
            List<Hierarchy> trainingMode = course.getTrainingModeHierarchy();
//            System.out.println(trainingMode.size());
            for (Hierarchy hierarchy:
                 trainingMode) {
//                System.out.println(course.getCourseName() + " : " + hierarchy.getProperty().getData().get("name"));
                Object[] objects = {course.getCourseName(), course.getCourseCode(), course.getCourseHours(), new ComboItem((String)hierarchy.getProperty().getData().get("name"), (Integer) hierarchy.getProperty().getData().get("courseModeId"))};
                tableModel.addRow(objects);
            }

        }

        coursesList.setRowSelectionAllowed(true);
        coursesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        if (courseProperties.size() > 0)
        {
            coursesList.setVisible(true);
            courseListLabel.setVisible(true);
            tableContainer.setVisible(true);
        } else {
            coursesList.setVisible(false);
            courseListLabel.setVisible(false);
            tableContainer.setVisible(false);
        }

    }

    private void addButtonListener() throws SQLException {
        Connection connection = pool.checkOut();
        Hierarchy packageHierarchy = HierarchyFactory.getHierarchy("Package");

        packageHierarchy.setConnection(connection);
        packageHierarchy.setStatement();

        packageHierarchy.setProperty();

        HashMap<String, Object> data = new HashMap<>();

        data.put("name", ((ComboItem)level.getSelectedItem()).getValue());
        data.put("id", ((ComboItem)level.getSelectedItem()).getLabel());

        levelHierarchy.setProperty();
        levelHierarchy.getProperty().setData(data);

        data.clear();

        List<CourseProperty> courseProperties = new ArrayList<>();

        int[] rows = coursesList.getSelectedRows();

        for (int i = 0; i<rows.length; i++) {
            CourseProperty courseProperty = new CourseProperty();

//            System.out.println(coursesList.getModel().getValueAt(rows[i],0));

            courseProperty.setCourseModeId(((ComboItem)coursesList.getModel().getValueAt(rows[i],3)).getLabel());

            courseProperties.add(courseProperty);
        }

        data.put("name", packageName.getText());
        data.put("level",levelHierarchy);
        data.put("courseIds",courseProperties);

        packageHierarchy.getProperty().setData(data);

        packageHierarchy.addToDatabase();

        pool.checkIn(connection);

        JOptionPane.showMessageDialog(null, "Package Added");

        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new CoursePackage(pool).appForm);
        topFrame.revalidate();
    }

    private void backButtonListener()
    {
        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new CoursePackage(pool).appForm);
        topFrame.revalidate();
    }
}
