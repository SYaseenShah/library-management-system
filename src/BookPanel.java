import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class BookPanel extends JFrame {
    private JTextField titleField, authorField;
    private JTable table;
    private DefaultTableModel tableModel;

    public BookPanel() {
        setTitle("Manage Books");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 248, 255)); // Soft background
        add(mainPanel);

        // Top Panel (Add Book) with proper layout
        JPanel addPanel = new JPanel(new BorderLayout(10, 10));
        addPanel.setBackground(new Color(240, 248, 255));

        JPanel fieldsPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        fieldsPanel.setBackground(new Color(240, 248, 255));

        JLabel titleLbl = new JLabel("Title:");
        JLabel authorLbl = new JLabel("Author:");
        styleLabel(titleLbl);
        styleLabel(authorLbl);

        titleField = new JTextField();
        authorField = new JTextField();
        styleField(titleField);
        styleField(authorField);

        fieldsPanel.add(titleLbl);
        fieldsPanel.add(titleField);
        fieldsPanel.add(authorLbl);
        fieldsPanel.add(authorField);

        JButton addButton = new JButton("Add Book");
        styleButton(addButton);

        addPanel.add(fieldsPanel, BorderLayout.CENTER);
        addPanel.add(addButton, BorderLayout.EAST);

        mainPanel.add(addPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Title", "Author", "Available"}, 0);
        table = new JTable(tableModel);
        styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Load data
        loadBooks();

        // Add book logic
        addButton.addActionListener(e -> addBook());

        setVisible(true);
    }

    private void addBook() {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();

        if (title.isEmpty() || author.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill all fields.");
            return;
        }

        try (Connection con = DatabaseHelper.getConnection()) {
            String sql = "INSERT INTO books (title, author, available) VALUES (?, ?, TRUE)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, title);
            ps.setString(2, author);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Book added.");
            titleField.setText("");
            authorField.setText("");
            loadBooks();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadBooks() {
        tableModel.setRowCount(0);
        try (Connection con = DatabaseHelper.getConnection()) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM books");
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getBoolean("available") ? "Yes" : "No"
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
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