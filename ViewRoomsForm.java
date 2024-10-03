import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

class ViewRoomsForm extends JFrame {

    public ViewRoomsForm(JFrame parent) {
        setTitle("View Rooms");
        setSize(400, 300); // Size of the window
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent); // Center the window relative to the parent

        // Define column names for the room table
        String[] columnNames = {"Room No", "Student ID", "Name"};
        Vector<String> columnNamesVector = new Vector<>();

        // Add column names to the vector
        for (String columnName : columnNames) {
            columnNamesVector.add(columnName);
        }

        Vector<Vector<Object>> dataVector = new Vector<>(); // Store the table data

        try {
            // Create a statement to execute SQL queries
            Statement stmt = HostelManagementSystem.connection.createStatement();
            // Execute query to get the relevant data
            ResultSet rs = stmt.executeQuery("SELECT room_table.room_no, stud_table.student_id, stud_table.name " +
                                              "FROM room_table LEFT JOIN stud_table ON room_table.student_id = stud_table.student_id");

            // Retrieve data from the ResultSet and fill the data vector
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("room_no"));          // Room No
                row.add(rs.getInt("student_id"));       // Student ID
                row.add(rs.getString("name"));          // Student Name
                dataVector.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create JTable with the retrieved data
        JTable table = new JTable(dataVector, columnNamesVector);
        // Make the table non-editable
        table.setEnabled(false); 

        // Wrap the table in a JScrollPane to allow scrolling
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER); // Add the scroll pane to the frame

        setVisible(true); // Make the frame visible
    }
}
