import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Main Menu");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1));

        add(createButton("Add Student", e -> new AddStudentForm(this)));
        add(createButton("Edit Student", e -> new EditStudentForm(this)));
        add(createButton("Delete Student", e -> new DeleteStudentForm(this)));
        add(createButton("View Students", e -> new ViewStudentsForm(this)));
        add(createButton("View Allocated Rooms", e -> new ViewRoomsForm(this)));
    }

    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        return button;
    }
}
