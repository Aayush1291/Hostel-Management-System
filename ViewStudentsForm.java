import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

class ViewStudentsForm extends JFrame {

    public ViewStudentsForm(JFrame parent) {
        setTitle("View Students");
        setSize(500, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        
        // Define column names for the student table
        String[] columnNames = {"Student ID", "Name", "Age", "Gender", "Email", "Room No"};
        Vector<String> columnNamesVector = new Vector<>();
        
        // Add column names to the vector
        for (String columnName : columnNames) {
            columnNamesVector.add(columnName);
        }
        
        Vector<Vector<Object>> dataVector = new Vector<>();

        try {
            Statement stmt = HostelManagementSystem.connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT student_id, name, age, gender, mail, room_no FROM stud_table");

            // Retrieve data from the ResultSet
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("student_id"));
                row.add(rs.getString("name"));
                row.add(rs.getInt("age"));
                row.add(rs.getString("gender"));
                row.add(rs.getString("mail"));
                row.add(rs.getInt("room_no"));
                dataVector.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create JTable with the retrieved data
        JTable table = new JTable(dataVector, columnNamesVector);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }
}
