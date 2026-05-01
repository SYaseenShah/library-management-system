import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class DeleteMemberPanel extends JFrame {
    private JComboBox<String> memberCombo;
    private JButton deleteButton;
    private DefaultComboBoxModel<String> comboModel;
    private Map<String, String> nameToIdMap;

    public DeleteMemberPanel() {
        setTitle("Discard Membership");
        setSize(580, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(20, 30, 20, 30));
        contentPane.setBackground(new Color(245, 245, 245));
        setContentPane(contentPane);

        JLabel title = new JLabel("Discard Membership", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(new Color(60, 60, 60));
        contentPane.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        centerPanel.setBackground(new Color(245, 245, 245));
        contentPane.add(centerPanel, BorderLayout.CENTER);

        comboModel = new DefaultComboBoxModel<>();
        memberCombo = new JComboBox<>(comboModel);
        memberCombo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        memberCombo.setBackground(new Color(240, 240, 240));
        memberCombo.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        centerPanel.add(new JLabel("Select Member to Discard:"));
        centerPanel.add(memberCombo);

        deleteButton = new JButton("Discard Membership");
        styleButton(deleteButton);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(245, 245, 245));
        bottomPanel.add(deleteButton);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        deleteButton.addActionListener(e -> deleteMember());
        loadMembers();

        setVisible(true);
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

    class RoundedBorder implements Border {
        private int radius;
        RoundedBorder(int radius) { this.radius = radius; }
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
        }
        public boolean isBorderOpaque() { return true; }
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            g.setColor(c.getForeground());
            g.drawRoundRect(x, y, w - 1, h - 1, radius, radius);
        }
    }

    private void loadMembers() {
        comboModel.removeAllElements();
        nameToIdMap = new HashMap<>();
        try (Connection con = DatabaseHelper.getConnection()) {
            ResultSet rs = con.createStatement().executeQuery("SELECT member_id, name FROM members");
            while (rs.next()) {
                String id = rs.getString("member_id");
                String name = rs.getString("name");
                String display = id + " - " + name;
                comboModel.addElement(display);
                nameToIdMap.put(display, id);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading members: " + e.getMessage());
        }
    }

    private void deleteMember() {
        String selected = (String) memberCombo.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "No member selected.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        String memberId = nameToIdMap.get(selected);

        try (Connection con = DatabaseHelper.getConnection()) {
            PreparedStatement checkStmt = con.prepareStatement(
                    "SELECT COUNT(*) FROM borrow_records WHERE member_id = ? AND return_date IS NULL");
            checkStmt.setString(1, memberId);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "Cannot delete member with unreturned books.");
                return;
            }

            PreparedStatement delStmt = con.prepareStatement("DELETE FROM members WHERE member_id = ?");
            delStmt.setString(1, memberId);
            if (delStmt.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Membership discarded.");
                loadMembers();
            } else {
                JOptionPane.showMessageDialog(this, "Deletion failed.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}