package inventorymanagement;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.dbcp2.*;

public class ConnectDatabase {
    private static JPanel errorPanel = new JPanel();
    private static  BasicDataSource ds = new BasicDataSource();
    private static Connection con;
    private static  String host = "";
    private static String username = "";
    private static  String password = "";
    //Used to create a connection pool for effecient access to the database
    ConnectDatabase() {
        ds.setUrl(host);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setInitialSize(5);
        ds.setMaxTotal(5);
        ds.setMinIdle(5);
        ds.setMaxIdle(5);
        ds.setMaxOpenPreparedStatements(100);
    }
    
    public static Connection getConnection() {
        try {
            con = ds.getConnection();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(errorPanel, "Can't connect to database.", "Error", 0);
            System.exit(0);
        }
        return con;
    }
}
