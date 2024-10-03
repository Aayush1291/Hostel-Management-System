import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class DeleteStudentForm extends JFrame {

    private JTextField studentIdField;

    public DeleteStudentForm(MainFrame mainFrame) {
        setTitle("Delete Student");
        setSize(300, 150);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(2, 2));

        add(new JLabel("Student ID:"));
        studentIdField = new JTextField();
        add(studentIdField);

        JButton deleteButton = new JButton("Delete Student");
        deleteButton.addActionListener(e -> deleteStudent());
        add(deleteButton);

        setVisible(true);
    }

    private void deleteStudent() {
        int studentId = Integer.parseInt(studentIdField.getText());

        try {
            PreparedStatement ps = HostelManagementSystem.connection.prepareStatement(
                "DELETE FROM stud_table WHERE student_id=?");
            ps.setInt(1, studentId);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student deleted successfully!");
            dispose();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting student!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
