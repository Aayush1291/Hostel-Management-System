import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

class EditStudentForm extends JFrame {

    private JTextField studentIdField, nameField, ageField, genderField, mailField, roomField;

    public EditStudentForm(MainFrame mainFrame) {
        setTitle("Edit Student");
        setSize(700, 500); // Increased window size
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Title Label
        JLabel formTitle = new JLabel("Edit Student Details", SwingConstants.CENTER);
        formTitle.setFont(new Font("Serif", Font.BOLD, 24));
        formTitle.setForeground(new Color(70, 130, 180)); // Steel Blue
        formTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(formTitle, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        formPanel.setBackground(new Color(245, 245, 245)); // White Smoke

        // Student ID
        formPanel.add(createLabel("Student ID:", new Color(70, 130, 180)));
        studentIdField = new JTextField();
        formPanel.add(studentIdField);

        // Name
        formPanel.add(createLabel("Name:", new Color(70, 130, 180)));
        nameField = new JTextField();
        formPanel.add(nameField);

        // Age
        formPanel.add(createLabel("Age:", new Color(70, 130, 180)));
        ageField = new JTextField();
        formPanel.add(ageField);

        // Gender
        formPanel.add(createLabel("Gender:", new Color(70, 130, 180)));
        genderField = new JTextField();
        formPanel.add(genderField);

        // Email
        formPanel.add(createLabel("Email:", new Color(70, 130, 180)));
        mailField = new JTextField();
        formPanel.add(mailField);

        // Room No
        formPanel.add(createLabel("Room No:", new Color(70, 130, 180)));
        roomField = new JTextField();
        formPanel.add(roomField);

        // Edit Button
        JButton editButton = new JButton("Edit Student");
        editButton.setFont(new Font("Arial", Font.BOLD, 16));
        editButton.setBackground(new Color(70, 130, 180)); // Steel Blue
        editButton.setForeground(Color.WHITE);
        editButton.setFocusPainted(false);
        editButton.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));

        // Add Image to Button
        try {
            ImageIcon editIcon = new ImageIcon("resources/edit.png"); // Replace with your image path
            Image img = editIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            editButton.setIcon(new ImageIcon(img));
            editButton.setHorizontalAlignment(SwingConstants.LEFT); // Align text to the left of the icon
            editButton.setIconTextGap(20); // Gap between icon and text
        } catch (Exception e) {
            System.err.println("Edit Student icon not found.");
        }

        editButton.addActionListener(e -> editStudent());
        formPanel.add(editButton);

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

    private void editStudent() {
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

        String name = nameField.getText().trim();
        String ageText = ageField.getText().trim();
        String gender = genderField.getText().trim();
        String mail = mailField.getText().trim();
        String roomNoText = roomField.getText().trim();

        if (name.isEmpty() || ageText.isEmpty() || gender.isEmpty() || mail.isEmpty() || roomNoText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Incomplete Data", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int age, roomNo;
        try {
            age = Integer.parseInt(ageText);
            roomNo = Integer.parseInt(roomNoText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for Age and Room No.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Start a transaction
            HostelManagementSystem.connection.setAutoCommit(false);

            // Update stud_table
            PreparedStatement ps = HostelManagementSystem.connection.prepareStatement(
                "UPDATE stud_table SET name=?, age=?, gender=?, mail=?, room_no=? WHERE student_id=?");
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, gender);
            ps.setString(4, mail);
            ps.setInt(5, roomNo);
            ps.setInt(6, studentId);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                JOptionPane.showMessageDialog(this, "No student found with the provided ID.", "Update Failed", JOptionPane.ERROR_MESSAGE);
                HostelManagementSystem.connection.rollback();
                return;
            }

            // Update room_table
            PreparedStatement psRoom = HostelManagementSystem.connection.prepareStatement(
                "UPDATE room_table SET room_no=? WHERE student_id=?");
            psRoom.setInt(1, roomNo);
            psRoom.setInt(2, studentId);
            psRoom.executeUpdate();

            // Commit the transaction
            HostelManagementSystem.connection.commit();
            JOptionPane.showMessageDialog(this, "Student updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                // Rollback the transaction in case of an error
                HostelManagementSystem.connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Error updating student!", "Error", JOptionPane.ERROR_MESSAGE);
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
