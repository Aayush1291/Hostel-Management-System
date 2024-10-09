import javax.swing.*;
import java.awt.*;
import java.sql.*;

class AddStudentForm extends JFrame {

    private JTextField nameField, ageField, genderField, mailField, roomField;

    public AddStudentForm(MainFrame mainFrame) {
        setTitle("Add Student");
        setSize(600, 400); // Increased window size
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Title Label
        JLabel formTitle = new JLabel("Add New Student", SwingConstants.CENTER);
        formTitle.setFont(new Font("Serif", Font.BOLD, 22));
        formTitle.setForeground(new Color(70, 130, 180)); // Steel Blue
        formTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(formTitle, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        formPanel.setBackground(new Color(245, 245, 245)); // White Smoke

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

        // Add Button
        JButton addButton = new JButton("Add Student");
        addButton.setFont(new Font("Arial", Font.BOLD, 16));
        addButton.setBackground(new Color(34, 139, 34)); // Forest Green
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 2));

        // Add Image to Button
        try {
            ImageIcon addIcon = new ImageIcon("resources/add_student_icon.png"); // Replace with your image path
            Image img = addIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            addButton.setIcon(new ImageIcon(img));
            addButton.setHorizontalAlignment(SwingConstants.LEFT); // Align text to the left of the icon
            addButton.setIconTextGap(20); // Gap between icon and text
        } catch (Exception e) {
            System.err.println("Add Student icon not found.");
        }

        addButton.addActionListener(e -> addStudent());
        formPanel.add(addButton);

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

    private void addStudent() {
        String name = nameField.getText();
        int age;
        try {
            age = Integer.parseInt(ageField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid age.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String gender = genderField.getText();
        String mail = mailField.getText();
        int roomNo;
        try {
            roomNo = Integer.parseInt(roomField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid room number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

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
            JOptionPane.showMessageDialog(this, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
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
