package Admin;

import Course.CourseProperty;
import JDBCConnection.JDBCConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by angel on 3/8/17.
 */
public class Main {


    public static void main(String[] args) throws SQLException {
        JDBCConnectionPool pool = new JDBCConnectionPool("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/designPatterns?verifyServerCertificate=false&useSSL=true", "root", "root");

        Connection connection = pool.checkOut();

        Hierarchy hierarchy = HierarchyFactory.getHierarchy("Package");

        hierarchy.setConnection(connection);
        hierarchy.setStatement();

        List<Hierarchy> hierarchies = hierarchy.getAll();

        for (Hierarchy hierarchyListed :
             hierarchies) {
            System.out.println("name : " + hierarchyListed.getProperty().getData().get("name") + " and id : " + hierarchyListed.getProperty().getData().get("id"));
            Hierarchy level = (Hierarchy) hierarchyListed.getProperty().getData().get("level");

            System.out.println("Level : " + level.getProperty().getData().get("id"));

            List<CourseProperty> courseProperties = (List<CourseProperty>) hierarchyListed.getProperty().getData().get("courseIds");

            for (CourseProperty course:
                 courseProperties) {
                System.out.println("Course Id for package " + hierarchyListed.getProperty().getData().get("name") + " : " + course.getCourseId());
            }
        }

//        Hierarchy hierarchy = HierarchyFactory.getHierarchy("Administrative Level");

    }

}
