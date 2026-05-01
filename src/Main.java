import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Ensure GUI uses the system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Could not set look and feel");
        }

        // Launch the login screen
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}