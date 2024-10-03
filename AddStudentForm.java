import javax.swing.*;
import java.awt.*;
import java.sql.*;

class AddStudentForm extends JFrame {

    private JTextField nameField, ageField, genderField, mailField, roomField;

    public AddStudentForm(MainFrame mainFrame) {
        setTitle("Add Student");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2));

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Age:"));
        ageField = new JTextField();
        add(ageField);

        add(new JLabel("Gender:"));
        genderField = new JTextField();
        add(genderField);

        add(new JLabel("Email:"));
        mailField = new JTextField();
        add(mailField);

        add(new JLabel("Room No:"));
        roomField = new JTextField();
        add(roomField);

        JButton addButton = new JButton("Add Student");
        addButton.addActionListener(e -> addStudent());
        add(addButton);

        setVisible(true);
    }

    private void addStudent() {
        String name = nameField.getText();
        int age = Integer.parseInt(ageField.getText());
        String gender = genderField.getText();
        String mail = mailField.getText();
        int roomNo = Integer.parseInt(roomField.getText());

        try {
            // Start a transaction
            HostelManagementSystem.connection.setAutoCommit(false);

            // Insert into stud_table
            PreparedStatement ps = HostelManagementSystem.connection.prepareStatement(
                "INSERT INTO stud_table (name, age, gender, mail, room_no) VALUES (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS); // Retrieve generated student ID
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, gender);
            ps.setString(4, mail);
            ps.setInt(5, roomNo);
            ps.executeUpdate();

            // Retrieve the generated student ID
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                int studentId = generatedKeys.getInt(1);

                // Insert into room_table
                PreparedStatement psRoom = HostelManagementSystem.connection.prepareStatement(
                    "INSERT INTO room_table (room_no, student_id, student_name) VALUES (?, ?, ?)");
                psRoom.setInt(1, roomNo);
                psRoom.setInt(2, studentId);
                psRoom.setString(3, name);
                psRoom.executeUpdate();
            }

            // Commit the transaction
            HostelManagementSystem.connection.commit();
            JOptionPane.showMessageDialog(this, "Student added successfully!");
            dispose();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                // Rollback the transaction in case of an error
                HostelManagementSystem.connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Error adding student!", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                // Reset auto-commit to true
                HostelManagementSystem.connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
