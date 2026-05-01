import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*; // OK to keep this, but not import java.awt.List;
import javax.swing.border.*;

public class BorrowReturnPanel extends JFrame {
    private JComboBox<String> memberBox, bookBox;
    private JButton borrowBtn, returnBtn;

    public BorrowReturnPanel() {
        setTitle("Borrow / Return Books");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout(20, 10));
        contentPane.setBorder(new EmptyBorder(20, 30, 20, 30));
        contentPane.setBackground(new Color(245, 245, 245));
        setContentPane(contentPane);

        JLabel title = new JLabel("Borrow / Return Books", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(new Color(60, 60, 60));
        contentPane.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        centerPanel.setBackground(new Color(245, 245, 245));
        contentPane.add(centerPanel, BorderLayout.CENTER);

        memberBox = new JComboBox<>();
        bookBox = new JComboBox<>();
        styleComboBox(memberBox);
        styleComboBox(bookBox);

        centerPanel.add(labeledPanel("Select Member:", memberBox));
        centerPanel.add(labeledPanel("Select Book:", bookBox));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        buttonPanel.setBackground(new Color(245, 245, 245));
        borrowBtn = new JButton("Borrow Book");
        returnBtn = new JButton("Return Book");
        styleButton(borrowBtn);
        styleButton(returnBtn);
        buttonPanel.add(borrowBtn);
        buttonPanel.add(returnBtn);

        centerPanel.add(buttonPanel);

        borrowBtn.addActionListener(e -> borrowBook());
        returnBtn.addActionListener(e -> returnBook());

        loadMembers();
        loadAvailableBooks();

        setVisible(true);
    }

    private JPanel labeledPanel(String labelText, JComboBox<String> comboBox) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(245, 245, 245));
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        comboBox.setFont(new Font("SansSerif", Font.PLAIN, 16));
        panel.add(label, BorderLayout.NORTH);
        panel.add(comboBox, BorderLayout.CENTER);
        return panel;
    }

    private void styleComboBox(JComboBox<String> box) {
        box.setBackground(new Color(240, 240, 240));
        box.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(255, 140, 0));
        button.setBorder(new RoundedBorder(20));
        button.setFocusPainted(false);
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
            return new Insets(radius, radius, radius, radius);
        }
        public boolean isBorderOpaque() { return true; }
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(c.getForeground());
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }

    private void loadMembers() {
        memberBox.removeAllItems();
        try (Connection con = DatabaseHelper.getConnection()) {
            ResultSet rs = con.createStatement().executeQuery("SELECT name FROM members");
            while (rs.next()) {
                memberBox.addItem(rs.getString("name"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading members: " + e.getMessage());
        }
    }

    private void loadAvailableBooks() {
        bookBox.removeAllItems();
        try (Connection con = DatabaseHelper.getConnection()) {
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM books WHERE available=1");
            while (rs.next()) {
                bookBox.addItem(rs.getInt("book_id") + " - " + rs.getString("title"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading books: " + e.getMessage());
        }
    }

    private void borrowBook() {
        String selectedName = (String) memberBox.getSelectedItem();
        String bookStr = (String) bookBox.getSelectedItem();

        if (selectedName == null || bookStr == null) {
            JOptionPane.showMessageDialog(this, "Select both a member and a book.");
            return;
        }

        int bookId = Integer.parseInt(bookStr.split(" - ")[0]);
        LocalDate today = LocalDate.now();
        LocalDate due = today.plusDays(14);

        try (Connection con = DatabaseHelper.getConnection()) {
            PreparedStatement getId = con.prepareStatement("SELECT member_id FROM members WHERE name=?");
            getId.setString(1, selectedName);
            ResultSet rs = getId.executeQuery();
            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "Member not found.");
                return;
            }

            String memberId = rs.getString("member_id");

            PreparedStatement insert = con.prepareStatement("INSERT INTO borrow_records (member_id, book_id, borrow_date, due_date) VALUES (?, ?, ?, ?)");
            insert.setString(1, memberId);
            insert.setInt(2, bookId);
            insert.setDate(3, Date.valueOf(today));
            insert.setDate(4, Date.valueOf(due));
            insert.executeUpdate();

            con.createStatement().executeUpdate("UPDATE books SET available=0 WHERE book_id=" + bookId);

            JOptionPane.showMessageDialog(this, "Book borrowed. Due on " + due);
            loadAvailableBooks();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error borrowing book: " + e.getMessage());
        }
    }

    private void returnBook() {
        String selectedName = (String) memberBox.getSelectedItem();
        if (selectedName == null) {
            JOptionPane.showMessageDialog(this, "No member selected.");
            return;
        }

        try (Connection con = DatabaseHelper.getConnection()) {
            PreparedStatement getId = con.prepareStatement("SELECT member_id FROM members WHERE name=?");
            getId.setString(1, selectedName);
            ResultSet rs = getId.executeQuery();
            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "Member not found.");
                return;
            }

            String memberId = rs.getString("member_id");

            PreparedStatement ps = con.prepareStatement(
                "SELECT br.book_id, b.title FROM borrow_records br " +
                "JOIN books b ON br.book_id = b.book_id " +
                "WHERE br.return_date IS NULL AND br.member_id = ?"
            );
            ps.setString(1, memberId);
            ResultSet brs = ps.executeQuery();

            List<String> borrowedBooks = new ArrayList<>();
            Map<String, Integer> bookMap = new HashMap<>();

            while (brs.next()) {
                int id = brs.getInt("book_id");
                String title = brs.getString("title");
                String label = id + " - " + title;
                borrowedBooks.add(label);
                bookMap.put(label, id);
            }

            if (borrowedBooks.isEmpty()) {
                JOptionPane.showMessageDialog(this, "This member has no books to return.");
                return;
            }

            String selected = (String) JOptionPane.showInputDialog(this, "Select book to return:", "Return Book",
                    JOptionPane.QUESTION_MESSAGE, null, borrowedBooks.toArray(), borrowedBooks.get(0));
            if (selected == null) return;

            int bookId = bookMap.get(selected);

            PreparedStatement update = con.prepareStatement(
                    "UPDATE borrow_records SET return_date=? WHERE book_id=? AND return_date IS NULL AND member_id=?");
            update.setDate(1, Date.valueOf(LocalDate.now()));
            update.setInt(2, bookId);
            update.setString(3, memberId);
            int updated = update.executeUpdate();

            if (updated > 0) {
                con.createStatement().executeUpdate("UPDATE books SET available=1 WHERE book_id=" + bookId);
                JOptionPane.showMessageDialog(this, "Book returned successfully.");
                loadAvailableBooks();
            } else {
                JOptionPane.showMessageDialog(this, "No active borrow record found.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error returning book: " + e.getMessage());
        }
    }
}