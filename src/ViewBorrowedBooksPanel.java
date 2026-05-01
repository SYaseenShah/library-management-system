import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ViewBorrowedBooksPanel extends JFrame {
    private JTable borrowedTable;
    private DefaultTableModel tableModel;

    public ViewBorrowedBooksPanel() {
        setTitle("Borrowed & Returned Books");
        setSize(800, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 248, 255));
        add(mainPanel);

        JLabel titleLabel = new JLabel("Borrowed & Returned Books");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(255, 140, 0));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{
                "Record ID", "Member Name", "Book Title", "Borrow Date", "Due Date", "Return Date"
        }, 0);
        borrowedTable = new JTable(tableModel);
        styleTable(borrowedTable);

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBorder(new LineBorder(new Color(255, 140, 0), 2, true));
        tableContainer.add(new JScrollPane(borrowedTable), BorderLayout.CENTER);
        tableContainer.setBackground(new Color(255, 248, 240));

        mainPanel.add(tableContainer, BorderLayout.CENTER);

        loadBorrowedBooks();
        setVisible(true);
    }

    private void loadBorrowedBooks() {
        try (Connection con = DatabaseHelper.getConnection()) {
            String query = "SELECT br.record_id, m.name, b.title, br.borrow_date, br.due_date, br.return_date " +
                           "FROM borrow_records br " +
                           "JOIN members m ON br.member_id = m.member_id " +
                           "JOIN books b ON br.book_id = b.book_id";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("record_id"),
                        rs.getString("name"),
                        rs.getString("title"),
                        rs.getDate("borrow_date"),
                        rs.getDate("due_date"),
                        rs.getDate("return_date") != null ? rs.getDate("return_date") : "Not Returned"
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading borrowed books: " + e.getMessage());
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