import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ViewMembersPanel extends JFrame {
    private JTable memberTable;
    private DefaultTableModel tableModel;

    public ViewMembersPanel() {
        setTitle("View Member Details");
        setSize(800, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 248, 255));
        add(mainPanel);

        JLabel titleLabel = new JLabel("Member Details");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(255, 140, 0));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{
                "Member ID", "Registration No", "Name", "Email", "Contact", "Membership Date"
        }, 0);
        memberTable = new JTable(tableModel);
        styleTable(memberTable);

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBorder(new LineBorder(new Color(255, 140, 0), 2, true));
        tableContainer.add(new JScrollPane(memberTable), BorderLayout.CENTER);
        tableContainer.setBackground(new Color(255, 248, 240));

        mainPanel.add(tableContainer, BorderLayout.CENTER);

        loadMemberData();
        setVisible(true);
    }

    private void loadMemberData() {
        try (Connection conn = DatabaseHelper.getConnection()) {
            String sql = "SELECT * FROM members";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getString("member_id"),
                        rs.getString("reg_no"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("contact"),
                        rs.getString("membership_date")
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading member data: " + ex.getMessage());
        }
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(230, 230, 250));
        table.getTableHeader().setForeground(Color.BLACK);
        table.setSelectionBackground(new Color(255, 228, 181));
        ((DefaultTableCellRenderer) table.getDefaultRenderer(Object.class)).setHorizontalAlignment(SwingConstants.CENTER);
    }
}