import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginFrame() {
        setTitle("Login");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Left panel (mint green with logo)
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(0, 230, 179)); // Mint green
        leftPanel.setPreferredSize(new Dimension(300, 0));
        leftPanel.setLayout(new GridBagLayout());

        JLabel logo = new JLabel("Welcome To Login");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        logo.setForeground(Color.BLACK);

        JLabel slogan = new JLabel("<html><center><br></center></html>");
        slogan.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        slogan.setForeground(Color.BLACK);
        slogan.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        leftPanel.add(logo, gbc);
        gbc.gridy++;
        leftPanel.add(slogan, gbc);

        // Right panel (white login area)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel loginLabel = new JLabel("Log in");
        loginLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(loginLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rightPanel.add(userLabel, gbc);

        gbc.gridy++;
        usernameField = new JTextField();
        rightPanel.add(usernameField, gbc);

        gbc.gridy++;
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rightPanel.add(passLabel, gbc);

        gbc.gridy++;
        passwordField = new JPasswordField();
        rightPanel.add(passwordField, gbc);

        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        JButton forgotPass = new JButton("forgot password?");
        forgotPass.setBorderPainted(false);
        forgotPass.setContentAreaFilled(false);
        forgotPass.setForeground(Color.GRAY);
        forgotPass.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        forgotPass.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgotPass.addActionListener(e -> new ForgotPasswordFrame().setVisible(true));
        rightPanel.add(forgotPass, gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 230, 179));
        loginButton.setForeground(Color.BLACK);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        loginButton.addActionListener(e -> login());
        rightPanel.add(loginButton, gbc);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection con = DatabaseHelper.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                new DashboardFrame();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }
}