import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class SearchBooksPanel extends JFrame {
    private JTextField searchField;
    private JButton searchButton;
    private JTable resultsTable;
    private DefaultTableModel tableModel;

    public SearchBooksPanel() {
        setTitle("Search Books");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 248, 255));
        add(mainPanel);

        // Top panel for search input
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(new Color(240, 248, 255));

        JPanel searchBoxPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        searchBoxPanel.setBackground(new Color(240, 248, 255));

        JLabel searchLabel = new JLabel("Enter Book Title:");
        styleLabel(searchLabel);

        searchField = new JTextField();
        styleField(searchField);

        searchBoxPanel.add(searchLabel);
        searchBoxPanel.add(searchField);

        searchButton = new JButton("Search");
        styleButton(searchButton);

        topPanel.add(searchBoxPanel, BorderLayout.CENTER);
        topPanel.add(searchButton, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(new String[]{"Book ID", "Title", "Author", "Available"}, 0);
        resultsTable = new JTable(tableModel);
        styleTable(resultsTable);
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Search action
        searchButton.addActionListener(e -> performSearch());

        setVisible(true);
    }

    private void performSearch() {
        String keyword = searchField.getText().trim();
        tableModel.setRowCount(0);

        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a title to search.");
            return;
        }

        try (Connection conn = DatabaseHelper.getConnection()) {
            String sql = "SELECT book_id, title, author, available FROM books WHERE LOWER(title) LIKE ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, "%" + keyword.toLowerCase() + "%");
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getBoolean("available") ? "Yes" : "No"
                    });
                }

                if (tableModel.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this,
                            "No books found with the title: " + keyword,
                            "No Results",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Database Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---- Styling methods ----
    private void styleLabel(JLabel label) {
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setForeground(new Color(50, 50, 50));
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(255, 140, 0)); // Orange
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createLineBorder(new Color(255, 140, 0), 2));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 120, 0));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 140, 0));
            }
        });
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