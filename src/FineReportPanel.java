import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class FineReportPanel extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public FineReportPanel() {
        setTitle("Fine Report");
        setSize(800, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel with background
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 248, 255));
        add(mainPanel);

        // Orange heading
        JLabel titleLabel = new JLabel("Fine Report");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(255, 140, 0)); // Orange
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Table setup
        tableModel = new DefaultTableModel(new String[]{
            "Record ID", "Member", "Book", "Due Date", "Return Date", "Fine (Rs)"
        }, 0);
        table = new JTable(tableModel);
        styleTable(table);

        // Orange-bordered panel for table
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBorder(new LineBorder(new Color(255, 140, 0), 2, true));
        tableContainer.add(new JScrollPane(table), BorderLayout.CENTER);
        tableContainer.setBackground(new Color(255, 248, 240)); // light orange-tinted background

        mainPanel.add(tableContainer, BorderLayout.CENTER);

        loadFines();
        setVisible(true);
    }

    private void loadFines() {
        tableModel.setRowCount(0);
        try (Connection con = DatabaseHelper.getConnection()) {
            String sql = """
                SELECT r.record_id, m.name AS member_name, b.title AS book_title,
                       r.due_date, r.return_date
                FROM borrow_records r
                JOIN members m ON r.member_id = m.member_id
                JOIN books b ON r.book_id = b.book_id
                WHERE r.return_date IS NOT NULL
            """;
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                LocalDate due = rs.getDate("due_date").toLocalDate();
                LocalDate returned = rs.getDate("return_date").toLocalDate();
                long daysLate = ChronoUnit.DAYS.between(due, returned);
                long fine = daysLate > 0 ? daysLate * 500 : 0;

                tableModel.addRow(new Object[]{
                    rs.getInt("record_id"),
                    rs.getString("member_name"),
                    rs.getString("book_title"),
                    due,
                    returned,
                    "Rs " + fine
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading fines: " + e.getMessage());
        }
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(230, 230, 250));
        table.getTableHeader().setForeground(Color.BLACK);
        table.setSelectionBackground(new Color(255, 228, 181)); // Light orange
        ((DefaultTableCellRenderer) table.getDefaultRenderer(Object.class)).setHorizontalAlignment(SwingConstants.CENTER);
    }
}