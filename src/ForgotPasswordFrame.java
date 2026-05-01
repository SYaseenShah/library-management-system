import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;

public class ForgotPasswordFrame extends JFrame {
    private JTextField userField;
    private JPasswordField passField;
    private JButton resetBtn;

    public ForgotPasswordFrame() {
        setTitle("Reset Password");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Content pane with padding and background
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(20, 30, 20, 30));
        contentPane.setBackground(new Color(245, 245, 245));
        setContentPane(contentPane);

        // Title label (same style)
        JLabel title = new JLabel("Reset Password", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(60, 60, 60));
        contentPane.add(title, BorderLayout.NORTH);

        // Form panel with grid layout and spacing
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        formPanel.setBackground(new Color(245, 245, 245));
        contentPane.add(formPanel, BorderLayout.CENTER);

        // Styled fields and labels
        userField = styledTextField();
        passField = styledPasswordField();

        formPanel.add(styledLabel("Username:"));
        formPanel.add(userField);
        formPanel.add(styledLabel("New Password:"));
        formPanel.add(passField);

        // Button panel
        resetBtn = new JButton("Reset Password");
        styleButton(resetBtn);
        resetBtn.addActionListener(e -> resetPassword());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.add(resetBtn);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Styled label (same as ChangePasswordPanel)
    private JLabel styledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        return label;
    }

    // Styled text field (same style as ChangePasswordPanel)
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

    // Styled password field (same style as ChangePasswordPanel)
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

    // Styled button (same as ChangePasswordPanel)
    private void styleButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 230, 179));
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(20));
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(255, 120, 0));
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(255, 140, 0));
            }
        });
    }

    // Reusable rounded border class (same as ChangePasswordPanel)
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

    // Your existing resetPassword() function (unchanged)
    private void resetPassword() {
        String username = userField.getText().trim();
        String newPassword = new String(passField.getPassword()).trim();

        if (username.isEmpty() || newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in both fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection con = DatabaseHelper.getConnection()) {
            PreparedStatement ps = con.prepareStatement("UPDATE users SET password=? WHERE username=?");
            ps.setString(1, newPassword);
            ps.setString(2, username);

            int updated = ps.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Password updated successfully!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Username not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}