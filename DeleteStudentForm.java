import javax.swing.*;
import java.awt.*;
import java.sql.*;

class DeleteStudentForm extends JFrame {

    private JTextField studentIdField;

    public DeleteStudentForm(MainFrame mainFrame) {
        setTitle("Delete Student");
        setSize(500, 300); // Increased window size
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Title Label
        JLabel formTitle = new JLabel("Delete Student", SwingConstants.CENTER);
        formTitle.setFont(new Font("Serif", Font.BOLD, 24));
        formTitle.setForeground(new Color(220, 20, 60)); // Crimson Red
        formTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(formTitle, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        formPanel.setBackground(new Color(245, 245, 245)); // White Smoke

        // Student ID
        formPanel.add(createLabel("Student ID:", new Color(220, 20, 60)));
        studentIdField = new JTextField();
        formPanel.add(studentIdField);

        // Delete Button
        JButton deleteButton = new JButton("Delete Student");
        deleteButton.setFont(new Font("Arial", Font.BOLD, 16));
        deleteButton.setBackground(new Color(220, 20, 60)); // Crimson Red
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setBorder(BorderFactory.createLineBorder(new Color(220, 20, 60), 2));

        // Add Image to Button
        try {
            ImageIcon deleteIcon = new ImageIcon("resources/delete.png"); // Replace with your image path
            Image img = deleteIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            deleteButton.setIcon(new ImageIcon(img));
            deleteButton.setHorizontalAlignment(SwingConstants.LEFT); // Align text to the left of the icon
            deleteButton.setIconTextGap(20); // Gap between icon and text
        } catch (Exception e) {
            System.err.println("Delete Student icon not found.");
        }

        deleteButton.addActionListener(e -> deleteStudent());
        formPanel.add(deleteButton);

        // Empty Label for alignment
        formPanel.add(new JLabel());

        add(formPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JLabel createLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(color);
        return label;
    }

    private void deleteStudent() {
        String studentIdText = studentIdField.getText().trim();
        if (studentIdText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the Student ID.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int studentId;
        try {
            studentId = Integer.parseInt(studentIdText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Student ID.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this student?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            // Start a transaction
            HostelManagementSystem.connection.setAutoCommit(false);

            // Delete from stud_table
            PreparedStatement ps = HostelManagementSystem.connection.prepareStatement(
                "DELETE FROM stud_table WHERE student_id=?");
            ps.setInt(1, studentId);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                JOptionPane.showMessageDialog(this, "No student found with the provided ID.", "Deletion Failed", JOptionPane.ERROR_MESSAGE);
                HostelManagementSystem.connection.rollback();
                return;
            }

            // Delete from room_table
            PreparedStatement psRoom = HostelManagementSystem.connection.prepareStatement(
                "DELETE FROM room_table WHERE student_id=?");
            psRoom.setInt(1, studentId);
            psRoom.executeUpdate();

            // Commit the transaction
            HostelManagementSystem.connection.commit();
            JOptionPane.showMessageDialog(this, "Student deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                // Rollback the transaction in case of an error
                HostelManagementSystem.connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Error deleting student!", "Error", JOptionPane.ERROR_MESSAGE);
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
