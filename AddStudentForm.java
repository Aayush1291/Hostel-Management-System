import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class AddStudentForm extends JFrame {

    private JTextField nameField, ageField, mailField;
    private JComboBox<String> genderComboBox, roomTypeComboBox, roomComboBox;

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
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
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

        // Gender (ComboBox with placeholder)
        formPanel.add(createLabel("Gender:", new Color(70, 130, 180)));
        String[] genders = {"Select Gender", "Male", "Female", "Other"};
        genderComboBox = new JComboBox<>(genders);
        genderComboBox.setSelectedIndex(0); // Default to the placeholder
        formPanel.add(genderComboBox);

        // Email
        formPanel.add(createLabel("Email:", new Color(70, 130, 180)));
        mailField = new JTextField();
        formPanel.add(mailField);

        // Room Type (ComboBox)
        formPanel.add(createLabel("Room Type:", new Color(70, 130, 180)));
        String[] roomTypes = {"Single Room", "Shared Room"};
        roomTypeComboBox = new JComboBox<>(roomTypes);
        roomTypeComboBox.setSelectedIndex(0); // Default to the placeholder
        formPanel.add(roomTypeComboBox);

        // Room No (ComboBox)
        formPanel.add(createLabel("Room No:", new Color(70, 130, 180)));
        roomComboBox = new JComboBox<>();
        updateRoomComboBox(); // Populate based on default selection
        formPanel.add(roomComboBox);

        // Action Listener for Room Type Selection
        roomTypeComboBox.addActionListener(e -> updateRoomComboBox());

        // Add Button
        JButton addButton = new JButton("Add Student");
        addButton.setFont(new Font("Arial", Font.BOLD, 16));
        addButton.setBackground(new Color(34, 139, 34)); // Forest Green
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 2));

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

    private void updateRoomComboBox() {
        roomComboBox.removeAllItems(); // Clear previous items
        String roomType = (String) roomTypeComboBox.getSelectedItem();

        try {
            PreparedStatement ps = HostelManagementSystem.connection.prepareStatement(
                "SELECT room_no, COUNT(room_no) AS room_count FROM stud_table GROUP BY room_no");
            ResultSet rs = ps.executeQuery();

            // Map to store room numbers and their occupancy
            Map<Integer, Integer> roomOccupancyMap = new HashMap<>();

            while (rs.next()) {
                int roomNo = rs.getInt("room_no");
                int roomCount = rs.getInt("room_count");
                roomOccupancyMap.put(roomNo, roomCount);
            }

            // For Single Room: check if the room is already taken
            if (roomType.equals("Single Room")) {
                if (!roomOccupancyMap.containsKey(102)) roomComboBox.addItem("102");
                if (!roomOccupancyMap.containsKey(202)) roomComboBox.addItem("202");
                if (!roomOccupancyMap.containsKey(302)) roomComboBox.addItem("302");
            } 
            // For Shared Room: allow only if there are fewer than 2 occupants
            else if (roomType.equals("Shared Room")) {
                if (roomOccupancyMap.getOrDefault(101, 0) < 2) roomComboBox.addItem("101");
                if (roomOccupancyMap.getOrDefault(201, 0) < 2) roomComboBox.addItem("201");
                if (roomOccupancyMap.getOrDefault(301, 0) < 2) roomComboBox.addItem("301");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching room availability!", "Error", JOptionPane.ERROR_MESSAGE);
        }
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

        // Get gender selection
        String genderSelection = (String) genderComboBox.getSelectedItem();
        if (genderSelection.equals("Select Gender")) {
            JOptionPane.showMessageDialog(this, "Please select a valid gender.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String mail = mailField.getText();

        // Get room number selection
        String roomSelection = (String) roomComboBox.getSelectedItem();
        if (roomSelection == null || roomSelection.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a valid room number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int roomNo = Integer.parseInt(roomSelection);

        // Regex for name validation (allows only alphabets and spaces)
        Pattern namePattern = Pattern.compile("^[a-zA-Z\\s]+$");
        Matcher nameMatcher = namePattern.matcher(name);
        if (!nameMatcher.matches()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid name (letters and spaces only).", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Regex for email validation
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
        Matcher emailMatcher = emailPattern.matcher(mail);
        if (!emailMatcher.matches()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Start a transaction
            HostelManagementSystem.connection.setAutoCommit(false);

            // Insert into stud_table
            PreparedStatement ps1 = HostelManagementSystem.connection.prepareStatement(
                "INSERT INTO stud_table (name, age, gender, mail, room_no) VALUES (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS); // Retrieve generated student ID
            ps1.setString(1, name);
            ps1.setInt(2, age);
            ps1.setString(3, genderSelection);
            ps1.setString(4, mail);
            ps1.setInt(5, roomNo);
            ps1.executeUpdate();

            // For room_table, you might want to update it accordingly
            PreparedStatement ps2 = HostelManagementSystem.connection.prepareStatement(
                "INSERT INTO room_table (student_name, room_no) VALUES (?, ?)");
            ps2.setString(1, name);
            ps2.setInt(2, roomNo);
            ps2.executeUpdate();

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
