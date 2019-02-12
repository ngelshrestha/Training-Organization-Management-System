package AppGui.Admin.Course;

import Admin.Hierarchy;
import AppGui.ComboItem;
import Course.*;
import JDBCConnection.JDBCConnectionPool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by angel on 3/13/17.
 */
public class EditOrDeleteCourse {

    protected JPanel appForm;
    private JComboBox<Object> level;
    private JComboBox courseType;
    private JLabel trainingTypeLabel;
    private JComboBox<Object> trainingType;
    private JComboBox<Object> course;
    private JLabel courseLabel;
    private JButton deleteButton;
    private JButton backButton;
    private JButton editButton;
    private JDBCConnectionPool pool;
    private Connection connection;
    private JFrame topFrame;

    public EditOrDeleteCourse(JDBCConnectionPool _pool) throws SQLException {

        pool = _pool;

        init();

        level.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (courseType.getSelectedIndex() != -1)
                {
                    course.removeAllItems();

                    int levelId = ((ComboItem) level.getSelectedItem()).getLabel();
                    int courseSubTypeId = ((ComboItem) trainingType.getSelectedItem()).getLabel();

                    try {
                        setCourseTrainingTypeButtonListener(levelId, courseSubTypeId);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        courseType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trainingType.removeAllItems();
                courseTypeListener();

                if (trainingType.getSelectedIndex() == -1)
                {
                    course.setVisible(false);
                    courseLabel.setVisible(false);
                }
            }
        });

        trainingType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (trainingType.getSelectedIndex() != -1)
                {
                    course.removeAllItems();

                    int levelId = ((ComboItem)level.getSelectedItem()).getLabel();

                    try {
                        int courseSubTypeId = ((ComboItem) trainingType.getSelectedItem()).getLabel();

                        setCourseTrainingTypeButtonListener(levelId, courseSubTypeId);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    deleteButtonListener();
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

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    editButtonListener();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    private void init() throws SQLException {
        initLevel();
        initCourseType();
    }

    private void initLevel() throws SQLException {
        trainingType.setVisible(false);
        trainingTypeLabel.setVisible(false);
        courseLabel.setVisible(false);
        course.setVisible(false);

        connection = pool.checkOut();
        List<Hierarchy> hierarchies = FormGetProperties.getLevels(connection);
        pool.checkIn(connection);

        for (Hierarchy levelHierarchy:
                hierarchies) {
            level.addItem(new ComboItem((String) levelHierarchy.getProperty().getData().get("name"), (Integer) levelHierarchy.getProperty().getData().get("id")));
        }

        level.setSelectedIndex(-1);
        courseType.setSelectedIndex(-1);
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

    private void courseTypeListener()
    {
        try {
            connection = pool.checkOut();

            List<Hierarchy> subTypes = FormGetProperties.getTrainingType(connection, ((ComboItem)courseType.getSelectedItem()).getLabel());

            pool.checkIn(connection);

            for (Hierarchy modeHierarchy:
                    subTypes) {
                trainingType.addItem(new ComboItem((String) modeHierarchy.getProperty().getData().get("name"), (int) modeHierarchy.getProperty().getData().get("courseSubTypeId")));
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

    private void backButtonListener()
    {
        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new Course(pool).appForm);
        topFrame.revalidate();
    }

    private void setCourseTrainingTypeButtonListener(int levelId, int courseSubTypeId) throws SQLException {
        connection = pool.checkOut();

        List<CourseProperty> courseProperties = FormGetProperties.getCourses(connection, levelId, courseSubTypeId);

        pool.checkIn(connection);

        for (CourseProperty courseProperty:
             courseProperties) {

            course.addItem(new ComboItem(courseProperty.getCourseName(), courseProperty.getCourseId()));

        }

        course.setSelectedIndex(-1);

        if (courseProperties.size() > 0)
        {
            course.setVisible(true);
            courseLabel.setVisible(true);
        } else {
            course.setVisible(false);
            courseLabel.setVisible(false);
        }
    }

    private void deleteButtonListener() throws SQLException {
        connection = pool.checkOut();

        CourseProperty courseProperty = new CourseProperty();

        courseProperty.setCourseId(((ComboItem) course.getSelectedItem()).getLabel());

        CourseFunctions courseDelete = CourseFunctionFactory.applyFunction("Delete", courseProperty, connection);

        courseDelete.steps();

        pool.checkIn(connection);

        JOptionPane.showMessageDialog(null, "Course Deleted");

        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new Course(pool).appForm);
        topFrame.revalidate();

    }

    private void editButtonListener() throws SQLException {
        int courseId = ((ComboItem) course.getSelectedItem()).getLabel();

        topFrame = (JFrame) SwingUtilities.getWindowAncestor(appForm);
        topFrame.remove(appForm);
        topFrame.setContentPane(new UpdateCourse(pool, courseId).appForm);
        topFrame.revalidate();
    }

}
