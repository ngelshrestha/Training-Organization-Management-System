import AppGui.MainAppForm.AppForm;
import JDBCConnection.JDBCConnectionPool;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Created by angel on 3/7/17.
 */
public class Main {

    public static void main(String [] args) throws SQLException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {

        JDBCConnectionPool pool = new JDBCConnectionPool("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/designPatterns?verifyServerCertificate=false&useSSL=true", "root", "root");

//        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        JFrame jFrame = new JFrame("Application");
        jFrame.setContentPane(new AppForm(pool).appForm);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setPreferredSize(new Dimension(640,480));
        jFrame.pack();

        jFrame.setVisible(true);

    }

}
