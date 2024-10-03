import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class HostelManagementSystem {
    static Connection connection;
    
    public static void main(String[] args) {
        connectToDatabase(); // Make sure this is called first
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
    
    public static void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Correct for Connector/J 9.0.0
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", "root", "aayush1291");
            System.out.println("Database connection established."); // Debugging line
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found. Please ensure the MySQL Connector/J JAR is included in the classpath.");
            e.printStackTrace(); // This will print the stack trace for debugging
        } catch (SQLException e) {
            System.err.println("Database connection failed. Please check your database URL, username, and password.");
            e.printStackTrace(); // This will print the stack trace for debugging
        } catch (Exception e) {
            System.err.println("An unexpected error occurred.");
            e.printStackTrace(); // This will print the stack trace for debugging
        }
    }
}
