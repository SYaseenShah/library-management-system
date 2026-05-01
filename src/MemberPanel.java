import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;

public class MemberPanel extends JFrame {
    private JTextField regField, nameField, emailField, contactField;
    private JButton registerBtn;

    public MemberPanel() {
        setTitle("Register Member");
        setSize(500, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(20, 30, 20, 30));
        contentPane.setBackground(new Color(245, 245, 245));
        setContentPane(contentPane);

        JLabel title = new JLabel("Register New Member", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(60, 60, 60));
        contentPane.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 15, 15));
        formPanel.setBackground(new Color(245, 245, 245));
        contentPane.add(formPanel, BorderLayout.CENTER);

        Font labelFont = new Font("SansSerif", Font.PLAIN, 16);
        Color fieldBg = new Color(240, 240, 240);

        regField = styledTextField();
        nameField = styledTextField();
        emailField = styledTextField();
        contactField = styledTextField();

        formPanel.add(styledLabel("Registration No:"));
        formPanel.add(regField);
        formPanel.add(styledLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(styledLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(styledLabel("Contact:"));
        formPanel.add(contactField);

        registerBtn = new JButton("Register");
        styleButton(registerBtn);
        registerBtn.addActionListener(e -> registerMember());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.add(registerBtn);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JLabel styledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        return label;
    }

    private JTextField styledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBackground(new Color(240, 240, 240));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }

private void styleButton(JButton button) {
    button.setFont(new Font("SansSerif", Font.BOLD, 16));
    button.setForeground(Color.WHITE); // Black text
    button.setBackground(new Color(255, 140, 0)); // Orange base
    button.setFocusPainted(false);
    button.setBorder(new RoundedBorder(20));
    button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    // Fix the white box issue
    button.setContentAreaFilled(false);  // Prevent default fill
    button.setOpaque(true);              // Ensure background shows through

    // Hover effect with darker orange
    button.addMouseListener(new MouseAdapter() {
        public void mouseEntered(MouseEvent e) {
            button.setBackground(new Color(255, 120, 0));
        }

        public void mouseExited(MouseEvent e) {
            button.setBackground(new Color(255, 140, 0));
        }
    });
}

    // Custom rounded border class
    class RoundedBorder implements Border {
        private int radius;
        RoundedBorder(int radius) {
            this.radius = radius;
        }
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
        }
        public boolean isBorderOpaque() {
            return true;
        }
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(c.getForeground());
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }

    private void registerMember() {
        String regNo = regField.getText().trim();
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String contact = contactField.getText().trim();

        if (regNo.isEmpty() || name.isEmpty() || email.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        int newId = 101;

        try (Connection con = DatabaseHelper.getConnection()) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(CAST(member_id AS UNSIGNED)) AS max_id FROM members");
            if (rs.next() && rs.getInt("max_id") > 0) {
                newId = rs.getInt("max_id") + 1;
            }

            String sql = "INSERT INTO members (member_id, reg_no, name, email, contact, membership_date) VALUES (?, ?, ?, ?, ?, CURDATE())";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, String.valueOf(newId));
            ps.setString(2, regNo);
            ps.setString(3, name);
            ps.setString(4, email);
            ps.setString(5, contact);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Member Registered! ID: " + newId);
            regField.setText("");
            nameField.setText("");
            emailField.setText("");
            contactField.setText("");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error registering member: " + e.getMessage());
        }
    }


}