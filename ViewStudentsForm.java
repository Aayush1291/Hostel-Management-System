import javax.swing.*;
import javax.swing.table.JTableHeader; // Added import for JTableHeader
import java.awt.*;
import java.sql.*;
import java.util.Vector;

class ViewStudentsForm extends JFrame {

    public ViewStudentsForm(JFrame parent) {
        setTitle("View Students");
        setSize(900, 600); // Increased window size
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Title Label
        JLabel formTitle = new JLabel("Student Details", SwingConstants.CENTER);
        formTitle.setFont(new Font("Serif", Font.BOLD, 24));
        formTitle.setForeground(new Color(70, 130, 180)); // Steel Blue
        formTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(formTitle, BorderLayout.NORTH);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(245, 245, 245)); // White Smoke

        // Define column names for the student table
        String[] columnNames = {"Student ID", "Name", "Age", "Room No"};
        Vector<String> columnNamesVector = new Vector<>();

        // Add column names to the vector
        for (String columnName : columnNames) {
            columnNamesVector.add(columnName);
        }

        Vector<Vector<Object>> dataVector = new Vector<>();

        try {
            // Adjust the SQL query to select desired columns
            Statement stmt = HostelManagementSystem.connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT student_id, name, age, room_no FROM stud_table");

            // Retrieve data from the ResultSet
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("student_id"));       // Student ID
                row.add(rs.getString("name"));          // Name
                row.add(rs.getInt("age"));              // Age
                row.add(rs.getInt("room_no"));          // Room No
                dataVector.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create JTable with the retrieved data
        JTable table = new JTable(dataVector, columnNamesVector);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.setEnabled(false); // Make table non-editable

        // Customize table header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 16));
        header.setBackground(new Color(70, 130, 180)); // Steel Blue
        header.setForeground(Color.WHITE);

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);

        setVisible(true);
    }
}
