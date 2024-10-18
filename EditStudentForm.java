import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class EditStudentForm extends JFrame {

    private JTextField studentIdField, nameField, ageField, mailField;
    private JComboBox<String> genderComboBox, roomComboBox, roomTypeComboBox;
    private MainFrame mainFrame; // Store the reference to MainFrame

    public EditStudentForm(MainFrame mainFrame) {
        this.mainFrame = mainFrame; // Initialize the mainFrame variable

        setTitle("Edit Student");
        setSize(700, 500); 
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Title Label
        JLabel formTitle = new JLabel("Edit Student Details", SwingConstants.CENTER);
        formTitle.setFont(new Font("Serif", Font.BOLD, 24));
        formTitle.setForeground(new Color(70, 130, 180)); 
        formTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(formTitle, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10)); // Increased rows from 7 to 8 to accommodate new field
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        formPanel.setBackground(new Color(245, 245, 245)); 

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

        // Gender (ComboBox)
        formPanel.add(createLabel("Gender:", new Color(70, 130, 180)));
        String[] genders = {"Select Gender", "Male", "Female", "Other"};
        genderComboBox = new JComboBox<>(genders);
        genderComboBox.setSelectedIndex(0); 
        formPanel.add(genderComboBox);

        // Email
        formPanel.add(createLabel("Email:", new Color(70, 130, 180)));
        mailField = new JTextField();
        formPanel.add(mailField);

        // Room Type (ComboBox)
        formPanel.add(createLabel("Room Type:", new Color(70, 130, 180)));
        String[] roomTypes = {"Select Room Type", "Shared", "Single"};
        roomTypeComboBox = new JComboBox<>(roomTypes);
        roomTypeComboBox.setSelectedIndex(0); // Default value
        formPanel.add(roomTypeComboBox);

        // Room No (ComboBox)
        formPanel.add(createLabel("Room No:", new Color(70, 130, 180)));
        roomComboBox = new JComboBox<>(new String[]{"Select Room"}); // Initially empty
        formPanel.add(roomComboBox);

        // Add action listener for roomTypeComboBox
        roomTypeComboBox.addActionListener(e -> updateRoomComboBox());

        // Edit Button
        JButton editButton = new JButton("Edit Student");
        editButton.setFont(new Font("Arial", Font.BOLD, 16));
        editButton.setBackground(new Color(70, 130, 180)); 
        editButton.setForeground(Color.WHITE);
        editButton.setFocusPainted(false);
        editButton.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));

        // Add Image to Button
        try {
            ImageIcon editIcon = new ImageIcon("resources/edit.png"); // Replace with your image path
            Image img = editIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            editButton.setIcon(new ImageIcon(img));
            editButton.setHorizontalAlignment(SwingConstants.LEFT);
            editButton.setIconTextGap(20);
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

    private List<String> getAvailableRooms(String roomType) {
        List<String> availableRooms = new ArrayList<>();

        // Rooms based on room type
        String[] sharedRooms = {"101", "201", "301"};
        String[] singleRooms = {"102", "202", "302"};

        String[] rooms = roomType.equals("Shared") ? sharedRooms : singleRooms;

        // Initially add all rooms based on the type to the available list
        for (String room : rooms) {
            availableRooms.add(room);
        }

        try {
            // Query the stud_table to count the number of students per room
            PreparedStatement ps = HostelManagementSystem.connection.prepareStatement(
                "SELECT room_no, COUNT(*) as student_count FROM stud_table GROUP BY room_no"
            );
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String roomNo = rs.getString("room_no");
                int studentCount = rs.getInt("student_count");

                if (roomType.equals("Shared") && studentCount >= 2) {
                    availableRooms.remove(roomNo); // If room is shared and already has 2 students, remove it
                } else if (roomType.equals("Single") && studentCount >= 1) {
                    availableRooms.remove(roomNo); // If room is single and has 1 student, remove it
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return availableRooms;
    }

    private void updateRoomComboBox() {
        String selectedRoomType = (String) roomTypeComboBox.getSelectedItem();

        // Skip if the room type is not selected
        if (selectedRoomType == null || selectedRoomType.equals("Select Room Type")) {
            roomComboBox.setModel(new DefaultComboBoxModel<>(new String[]{})); // Clear the combo box
            return;
        }

        // Get available rooms based on the selected room type
        List<String> availableRooms = getAvailableRooms(selectedRoomType);
        
        if (availableRooms.isEmpty()) {
            roomComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"No Available Rooms"}));
        } else {
            roomComboBox.setModel(new DefaultComboBoxModel<>(availableRooms.toArray(new String[0])));
        }
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
        String genderSelection = (String) genderComboBox.getSelectedItem();
        String mail = mailField.getText().trim();
        String roomSelection = (String) roomComboBox.getSelectedItem();
        String roomTypeSelection = (String) roomTypeComboBox.getSelectedItem();

        // Validation
        if (name.isEmpty() || ageText.isEmpty() || genderSelection.equals("Select Gender") || mail.isEmpty() || roomSelection.equals("Select Room") || roomTypeSelection.equals("Select Room Type")) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Incomplete Data", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int age, roomNo;
        try {
            age = Integer.parseInt(ageText);
            roomNo = Integer.parseInt(roomSelection);
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
            ps.setString(3, genderSelection);
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
                "UPDATE room_table SET student_name=?, room_no=? WHERE student_id=?");
            psRoom.setString(1, name);
            psRoom.setInt(2, roomNo);
            psRoom.setInt(3, studentId);
            psRoom.executeUpdate();

            // Commit the transaction
            HostelManagementSystem.connection.commit();

            JOptionPane.showMessageDialog(this, "Student details updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Close the form

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                HostelManagementSystem.connection.rollback(); // Rollback in case of an error
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        } finally {
            try {
                HostelManagementSystem.connection.setAutoCommit(true); // Reset auto-commit mode
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
