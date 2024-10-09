import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class LoginForm extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginForm() {
        setTitle("Login - Room-mate");
        setSize(700, 500); // Adjusted window size
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Set the content pane to a custom panel with background image
        BackgroundPanel background = new BackgroundPanel("resources/bg.jpeg");
        background.setLayout(new GridBagLayout()); // Centering everything
        add(background);

        // Create a central white box panel
        JPanel whiteBox = new JPanel();
        whiteBox.setLayout(new BorderLayout());
        whiteBox.setBackground(Color.WHITE);
        whiteBox.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // Adjusted padding inside the white box
        whiteBox.setPreferredSize(new Dimension(400, 250)); // Smaller white box size

        // Project Title
        JLabel projectTitle = new JLabel("Room-mate: Hostel Manager", SwingConstants.CENTER);
        projectTitle.setFont(new Font("Serif", Font.BOLD, 18)); // Decreased font size
        projectTitle.setForeground(new Color(70, 130, 180)); // Steel Blue
        projectTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        whiteBox.add(projectTitle, BorderLayout.NORTH);

        // Login Panel
        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 15, 15)); // Adjusted spacing between components
        loginPanel.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        loginPanel.setBackground(Color.WHITE); // White box background

        // Username
        loginPanel.add(createLabel("Username:", new Color(70, 130, 180)));
        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14)); // Decreased input field font size
        loginPanel.add(usernameField);

        // Password
        loginPanel.add(createLabel("Password:", new Color(70, 130, 180)));
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14)); // Decreased input field font size
        loginPanel.add(passwordField);

        // Login Button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16)); // Decreased button font size
        loginButton.setBackground(new Color(70, 130, 180)); // Steel Blue
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));

        // Add Image to Button
        try {
            ImageIcon loginIcon = new ImageIcon("resources/view.png"); // Replace with your login image path
            Image img = loginIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH); // Adjusted image size
            loginButton.setIcon(new ImageIcon(img));
            loginButton.setHorizontalAlignment(SwingConstants.CENTER); // Center the icon
            loginButton.setIconTextGap(8); // Gap between icon and text
        } catch (Exception e) {
            System.err.println("Login icon not found.");
        }

        loginButton.addActionListener(e -> login());
        loginPanel.add(loginButton);

        // Add empty label for alignment
        loginPanel.add(new JLabel());

        whiteBox.add(loginPanel, BorderLayout.CENTER);

        // Add the white box to the center of the background
        background.add(whiteBox);

        setVisible(true);
    }

    // Helper method to create styled labels
    private JLabel createLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14)); // Decreased label font size
        label.setForeground(color);
        return label;
    }

    // Login method with input validation and feedback
    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // For demonstration, using hardcoded credentials
        if (username.equals("admin") && password.equals("admin123")) {
            dispose();
            new MainFrame().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials!", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Custom JPanel to handle background image
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                backgroundImage = new ImageIcon(imagePath).getImage();
            } catch (Exception e) {
                System.err.println("Background image not found: " + imagePath);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                // Draw the background image to fill the panel
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    // Main method to run the application
    public static void main(String[] args) {
        new LoginForm();
    }
}
