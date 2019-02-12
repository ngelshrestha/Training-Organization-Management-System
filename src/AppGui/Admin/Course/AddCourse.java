package AppGui.Admin.Course;

import Admin.Hierarchy;
import AppGui.ComboItem;
import AppGui.FormValidation.MyNumericVerifier;
import Course.*;
import JDBCConnection.JDBCConnectionPool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by angel on 3/9/17.
 */
public class AddCourse{

    public JPanel appForm;
    private JTextField courseName;
    private JTextField courseCode;
    private JTextField courseHours;
    private JComboBox<Object> level;
    private JComboBox<Object> courseType;
    private JComboBox<Object> trainingType;
    private JButton addCourseButton;
    private JLabel trainingTypeLabel;
    private JList<Object> trainingModes;
    private JButton backButton;
    private JTextPane validationLabel;
    private JScrollPane validationContainer;
    private static JDBCConnectionPool pool;
    private static Connection connection;
    private JFrame topFrame;

    public AddCourse(JDBCConnectionPool _pool) throws SQLException {

        pool = _pool;

        init();

        courseType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                trainingType.removeAllItems();

                trainingTypeListener();
            }
        });

        addCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateForm())
                    addCourseListener();

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
        initCourseType();
        initTrainingModes();

    }

    private void initLevel() throws SQLException {
        trainingType.setVisible(false);
        trainingTypeLabel.setVisible(false);

        connection = pool.checkOut();
        List<Hierarchy> hierarchies = FormGetProperties.getLevels(connection);
        pool.checkIn(connection);

        for (Hierarchy levelHierarchy:
                hierarchies) {
            level.addItem(new ComboItem((String) levelHierarchy.getProperty().getData().get("name"), (Integer) levelHierarchy.getProperty().getData().get("id")));
        }

        level.setSelectedIndex(-1);
    }

    private void initCourseType() throws SQLException {
        connection = pool.checkOut();
        List<Hierarchy> hierarchies = FormGetProperties.getCourseTypes(connection);
        pool.checkIn(connection);

        for (Hierarchy courseTypeHierarchy:
                hierarchies) {
            courseType.addItem(new ComboItem((String)courseTypeHierarchy.getProperty().getData().get("name"), (Integer) courseTypeHierarchy.getProperty().getData().get("id")));
        }

        courseType.setSelectedIndex(-1);
    }

    private void initTrainingModes() throws SQLException {

        connection = pool.checkOut();

        List<Hierarchy> modes = FormGetProperties.getTrainingModes(connection);

        pool.checkIn(connection);

        trainingModes.setModel(new DefaultListModel<Object>());
        DefaultListModel<Object> model = (DefaultListModel<Object>) trainingModes.getModel();

        for (Hierarchy modeHierarchy:
                modes) {
            model.addElement(new ComboItem((String) modeHierarchy.getProperty().getData().get("name"), (int) modeHierarchy.getProperty().getData().get("id")));
        }
    }

    private void trainingTypeListener()
    {
        try {
            connection = pool.checkOut();

            List<Hierarchy> subTypes = FormGetProperties.getTrainingType(connection, ((ComboItem)courseType.getSelectedItem()).getLabel());

            pool.checkIn(connection);

            for (Hierarchy modeHierarchy:
                    subTypes) {
                trainingType.addItem(new ComboItem((String) modeHierarchy.getProperty().getData().get("name"), (int) modeHierarchy.getProperty().getData().get("id")));
            }

            trainingType.setSelectedIndex(-1);

            if (subTypes.size() > 0)
            {
                trainingType.setVisible(true);
                trainingTypeLabel.setVisible(true);
            } else {
                trainingType.setVisible(false);
                trainingTypeLabel.setVisible(false);
            }

        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    private void addCourseListener()
    {
        CourseProperty courseProperty = new CourseProperty();

        courseProperty.setCourseName(courseName.getText());
        courseProperty.setCourseCode(Integer.parseInt(courseCode.getText()));
        courseProperty.setCourseHours(Integer.parseInt(courseHours.getText()));

        ComboItem item = (ComboItem) level.getSelectedItem();
        courseProperty.setLevelId(item.getLabel());

        item = (ComboItem) courseType.getSelectedItem();
        courseProperty.setCourseTypeId(item.getLabel());

        item = (ComboItem) trainingType.getSelectedItem();
        courseProperty.setTrainingTypeId(item.getLabel());

        List<Integer> list = new ArrayList<>();

        for (Object object:
                trainingModes.getSelectedValuesList()) {
            ComboItem modeItem = (ComboItem) object;
            list.add(modeItem.getLabel());
        }

        courseProperty.setModes(list);

        try {

            connection = pool.checkOut();

            CourseFunctions courseAdd = CourseFunctionFactory.applyFunction("Add", courseProperty, connection);
            courseAdd.steps();

            pool.checkIn(connection);

            JOptionPane.showMessageDialog(null, "Course Added");

            topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
            topFrame.remove(appForm);
            topFrame.setContentPane(new Course(pool).appForm);
            topFrame.revalidate();

        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    private void backButtonListener()
    {
        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new Course(pool).appForm);
        topFrame.revalidate();
    }

//    private boolean validateForm()
//    {
//        boolean isValid = true;
//
//        String errors = "";
//
//        courseCode.setInputVerifier(new MyNumericVerifier(validationLabel));
//        courseCode.setName("Course Code");
//        if (!courseCode.getInputVerifier().shouldYieldFocus(courseCode))
//        {
//            isValid = false;
//            errors += validationLabel.getText();
//        }
//
//        courseHours.setInputVerifier(new MyNumericVerifier(validationLabel));
//        courseHours.setName("Course Hours");
//        if (!courseHours.getInputVerifier().shouldYieldFocus(courseHours))
//        {
//            isValid = false;
//            errors += validationLabel.getText();
//        }
//
//        validationLabel.setText(errors);
//        validationLabel.setEditable(false);
//        validationLabel.setVisible(true);
//
//        return isValid;
//    }

    private boolean validateForm()
    {
        boolean isValid = false;
        String errors = "";

        courseCode.setInputVerifier(new MyNumericVerifier());
        if (!courseCode.getInputVerifier().verify(courseCode))
        {
            errors += "\nCourse Code" + courseCode.getInputVerifier().toString();
        }

        courseHours.setInputVerifier(new MyNumericVerifier());
        if (!courseHours.getInputVerifier().verify(courseHours))
        {
            errors += "\nCourse Hours" + courseHours.getInputVerifier().toString();
        }

        System.out.println(errors);

        if (errors.equals(""))
            isValid = true;

        return isValid;
    }

}
