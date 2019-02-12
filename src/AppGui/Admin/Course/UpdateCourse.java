package AppGui.Admin.Course;

import Admin.Hierarchy;
import AppGui.ComboItem;
import Course.CourseFunctionFactory;
import Course.CourseFunctions;
import Course.CourseProperty;
import Course.FormGetProperties;
import JDBCConnection.JDBCConnectionPool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by angel on 3/13/17.
 */
public class UpdateCourse {

    protected JPanel appForm;
    private JTextField courseName;
    private JTextField courseCode;
    private JTextField courseHours;
    private JComboBox<Object> level;
    private JComboBox<Object> courseType;
    private JList<Object> trainingModes;
    private JButton editCourseButton;
    private JButton backButton;
    private JComboBox<Object> trainingType;

    private JDBCConnectionPool pool;
    private CourseProperty courseProperty;
    private int courseId;
    private Connection connection;

    private JFrame topFrame;

    public UpdateCourse(JDBCConnectionPool _pool, int _courseId) throws SQLException {
        pool = _pool;
        this.courseId = _courseId;

        init();

        courseType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trainingType.removeAllItems();
                setTrainingType();
            }
        });

        editCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    editCourseButtonListener();
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
        setCourseProperty();
        setCourseDetails();
        setLevel();
        setCourseType();
        setTrainingType();
        setTrainingModes();
    }

    private void setCourseProperty() throws SQLException {
        connection = pool.checkOut();

        courseProperty = new CourseProperty();

        courseProperty.setCourseId(courseId);

        courseProperty = FormGetProperties.getRequiredCourse(connection, courseProperty);

        pool.checkIn(connection);
    }

    private void setCourseDetails()
    {
        courseName.setText(courseProperty.getCourseName());
        courseCode.setText(String.valueOf(courseProperty.getCourseCode()));
        courseHours.setText(String.valueOf(courseProperty.getCourseHours()));
    }

    private void setLevel() throws SQLException {

        connection = pool.checkOut();
        List<Hierarchy> hierarchies = FormGetProperties.getLevels(connection);
        pool.checkIn(connection);

        for (Hierarchy levelHierarchy:
                hierarchies) {
            level.addItem(new ComboItem((String) levelHierarchy.getProperty().getData().get("name"), (Integer) levelHierarchy.getProperty().getData().get("id")));
            if ((int) levelHierarchy.getProperty().getData().get("id") == courseProperty.getLevelId()) {
                level.getModel().setSelectedItem(new ComboItem((String) levelHierarchy.getProperty().getData().get("name"), (Integer) levelHierarchy.getProperty().getData().get("id")));
            }
        }
    }

    private void setCourseType() throws SQLException {
        connection = pool.checkOut();
        List<Hierarchy> hierarchies = FormGetProperties.getCourseTypes(connection);
        pool.checkIn(connection);

        for (Hierarchy courseTypeHierarchy:
             hierarchies) {
            courseType.addItem(new ComboItem((String) courseTypeHierarchy.getProperty().getData().get("name"), (int)courseTypeHierarchy.getProperty().getData().get("id")));
            if (courseProperty.getCourseTypeId() == (int)courseTypeHierarchy.getProperty().getData().get("id"))
            {
                courseType.getModel().setSelectedItem(new ComboItem((String) courseTypeHierarchy.getProperty().getData().get("name"), (int)courseTypeHierarchy.getProperty().getData().get("id")));
            }
        }
    }

    private void setTrainingType()
    {
        try {
            connection = pool.checkOut();

            List<Hierarchy> subTypes = FormGetProperties.getTrainingType(connection, ((ComboItem)courseType.getSelectedItem()).getLabel());

            pool.checkIn(connection);

            for (Hierarchy modeHierarchy:
                    subTypes) {
                trainingType.addItem(new ComboItem((String) modeHierarchy.getProperty().getData().get("name"), (int) modeHierarchy.getProperty().getData().get("id")));
                if (courseProperty.getTrainingTypeId() == (int)modeHierarchy.getProperty().getData().get("id"))
                {
                    trainingType.getModel().setSelectedItem(new ComboItem((String) modeHierarchy.getProperty().getData().get("name"), (int) modeHierarchy.getProperty().getData().get("id")));
                }
            }

        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    private void setTrainingModes() throws SQLException {
        connection = pool.checkOut();

        List<Hierarchy> modes = FormGetProperties.getTrainingModes(connection);

        pool.checkIn(connection);

        trainingModes.setModel(new DefaultListModel<Object>());
        DefaultListModel<Object> model = (DefaultListModel<Object>) trainingModes.getModel();
        int[] i = new int[courseProperty.getModes().size()];
        int j =0;

        for (Hierarchy modeHierarchy:
                modes) {

            model.addElement(new ComboItem((String) modeHierarchy.getProperty().getData().get("name"), (int) modeHierarchy.getProperty().getData().get("id")));

            if (courseProperty.getModes().contains((int)modeHierarchy.getProperty().getData().get("id")))
            {
                i[j] = model.getSize()-1;

                j++;
            }
        }

        trainingModes.setSelectedIndices(i);
    }

    private void editCourseButtonListener() throws SQLException {
        this.courseProperty.setCourseName(courseName.getText());
        this.courseProperty.setCourseCode(Integer.parseInt(courseCode.getText()));
        this.courseProperty.setCourseHours(Integer.parseInt(courseHours.getText()));

        this.courseProperty.setLevelId(((ComboItem)level.getSelectedItem()).getLabel());

        this.courseProperty.setCourseTypeId(((ComboItem)courseType.getSelectedItem()).getLabel());

        this.courseProperty.setTrainingTypeId(((ComboItem)trainingType.getSelectedItem()).getLabel());

        List<Integer> modes = new ArrayList<>();

        for (Object object:
             trainingModes.getSelectedValuesList()) {
            modes.add(((ComboItem)object).getLabel());
        }

        this.courseProperty.setModes(modes);

        connection = pool.checkOut();

        CourseFunctions updateCourseFunc = CourseFunctionFactory.applyFunction("Update", courseProperty, connection);

        updateCourseFunc.steps();

        pool.checkIn(connection);

        JOptionPane.showMessageDialog(null, "Course Updated");

        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new Course(pool).appForm);
        topFrame.revalidate();
    }

    private void backButtonListener()
    {
        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new Course(pool).appForm);
        topFrame.revalidate();
    }
}
