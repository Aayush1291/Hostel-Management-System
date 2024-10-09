import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Room-mate: A Hostel Management Service");
        setSize(1000, 800); // Increased window size
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180)); // Steel Blue background
        JLabel titleLabel = new JLabel("Room-mate: A Hostel Management Service");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(new Color(245, 245, 245)); // White Smoke background
        buttonsPanel.setLayout(new GridLayout(5, 1, 20, 20)); // 5 rows, 1 column, 20px gaps

        // Add Buttons with Images
        buttonsPanel.add(createButton("Add Student", "resources/add.png", e -> new AddStudentForm(this)));
        buttonsPanel.add(createButton("Edit Student", "resources/edit.png", e -> new EditStudentForm(this)));
        buttonsPanel.add(createButton("Delete Student", "resources/delete.png", e -> new DeleteStudentForm(this)));
        buttonsPanel.add(createButton("View Students", "resources/view.png", e -> new ViewStudentsForm(this)));
        buttonsPanel.add(createButton("View Allocated Rooms", "resources/rooms.png", e -> new ViewRoomsForm(this)));

        // Add some padding around buttons
        JPanel paddingPanel = new JPanel(new BorderLayout());
        paddingPanel.setBorder(BorderFactory.createEmptyBorder(50, 300, 50, 300)); // top, left, bottom, right
        paddingPanel.setBackground(new Color(245, 245, 245));
        paddingPanel.add(buttonsPanel, BorderLayout.CENTER);

        add(paddingPanel, BorderLayout.CENTER);
    }

    private JButton createButton(String text, String imagePath, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setBackground(new Color(70, 130, 180)); // Steel Blue background
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));

        // Load and set the image icon
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Resize the image
            button.setIcon(new ImageIcon(img));
            button.setHorizontalAlignment(SwingConstants.LEFT); // Align text to the left of the icon
            button.setIconTextGap(20); // Gap between icon and text
        } catch (Exception e) {
            System.err.println("Image not found: " + imagePath);
        }

        button.addActionListener(action);
        return button;
    }
}
