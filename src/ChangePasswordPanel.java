import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;

public class ChangePasswordPanel extends JFrame {
    private JTextField usernameField;
    private JPasswordField oldPasswordField, newPasswordField;
    private JButton updateBtn;

    public ChangePasswordPanel() {
        setTitle("Change Password");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(20, 30, 20, 30));
        contentPane.setBackground(new Color(245, 245, 245));
        setContentPane(contentPane);

        JLabel title = new JLabel("Change Password", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(60, 60, 60));
        contentPane.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 15, 15));
        formPanel.setBackground(new Color(245, 245, 245));
        contentPane.add(formPanel, BorderLayout.CENTER);

        usernameField = styledTextField();
        oldPasswordField = styledPasswordField();
        newPasswordField = styledPasswordField();

        formPanel.add(styledLabel("Username:"));
        formPanel.add(usernameField);
        formPanel.add(styledLabel("Old Password:"));
        formPanel.add(oldPasswordField);
        formPanel.add(styledLabel("New Password:"));
        formPanel.add(newPasswordField);

        updateBtn = new JButton("Update Password");
        styleButton(updateBtn);
        updateBtn.addActionListener(e -> changePassword());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.add(updateBtn);
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

    private JPasswordField styledPasswordField() {
        JPasswordField field = new JPasswordField();
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
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(255, 140, 0));
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(20));
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(255, 120, 0));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(255, 140, 0));
            }
        });
    }

    // Reusable rounded border class
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

    private void changePassword() {
        String username = usernameField.getText().trim();
        String oldPass = new String(oldPasswordField.getPassword());
        String newPass = new String(newPasswordField.getPassword());

        if (username.isEmpty() || oldPass.isEmpty() || newPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        try (Connection con = DatabaseHelper.getConnection()) {
            PreparedStatement check = con.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
            check.setString(1, username);
            check.setString(2, oldPass);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                PreparedStatement update = con.prepareStatement("UPDATE users SET password=? WHERE username=?");
                update.setString(1, newPass);
                update.setString(2, username);
                update.executeUpdate();
                JOptionPane.showMessageDialog(this, "Password updated successfully.");
                oldPasswordField.setText("");
                newPasswordField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid current password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}