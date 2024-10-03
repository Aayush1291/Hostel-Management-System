import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class EditStudentForm extends JFrame {

    private JTextField studentIdField, nameField, ageField, genderField, mailField, roomField;

    public EditStudentForm(MainFrame mainFrame) {
        setTitle("Edit Student");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(7, 2));

        add(new JLabel("Student ID:"));
        studentIdField = new JTextField();
        add(studentIdField);

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

        JButton editButton = new JButton("Edit Student");
        editButton.addActionListener(e -> editStudent());
        add(editButton);

        setVisible(true);
    }

    private void editStudent() {
        int studentId = Integer.parseInt(studentIdField.getText());
        String name = nameField.getText();
        int age = Integer.parseInt(ageField.getText());
        String gender = genderField.getText();
        String mail = mailField.getText();
        int roomNo = Integer.parseInt(roomField.getText());

        try {
            PreparedStatement ps = HostelManagementSystem.connection.prepareStatement(
                "UPDATE stud_table SET name=?, age=?, gender=?, mail=?, room_no=? WHERE student_id=?");
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, gender);
            ps.setString(4, mail);
            ps.setInt(5, roomNo);
            ps.setInt(6, studentId);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student updated successfully!");
            dispose();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating student!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
