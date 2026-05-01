import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class DashboardFrame extends JFrame {

    public DashboardFrame() {
        setTitle("Library Dashboard");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

       // Sidebar with functional icon buttons
JPanel sidebar = new JPanel();
sidebar.setBackground(new Color(30, 30, 60));
sidebar.setLayout(new GridLayout(7, 1, 10, 10));
sidebar.setPreferredSize(new Dimension(80, 0));

// Icon labels and corresponding actions
String[][] sidebarButtons = {
    {"🏠", "Dashboard"},
    {"📘", "Manage Books"},
    {"📚", "Borrow/Return Book"},
    {"🔍", "Search Books"},
    {"👥", "Register Member"},
    {"🕓", "View Fine Report"},
    {"🚪", "Logout"}
};

for (String[] btnInfo : sidebarButtons) {
    JButton iconBtn = new JButton(btnInfo[0]);
    iconBtn.setFont(new Font("SansSerif", Font.PLAIN, 24));
    iconBtn.setForeground(Color.WHITE);
    iconBtn.setBackground(new Color(30, 30, 60));
    iconBtn.setFocusPainted(false);
    iconBtn.setBorderPainted(false);
    iconBtn.setToolTipText(btnInfo[1]);
    iconBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

    iconBtn.addMouseListener(new MouseAdapter() {
        public void mouseEntered(MouseEvent e) {
            iconBtn.setBackground(new Color(45, 45, 90));
        }

        public void mouseExited(MouseEvent e) {
            iconBtn.setBackground(new Color(30, 30, 60));
        }
    });

    // Click events for sidebar buttons
    switch (btnInfo[1]) {
        case "Dashboard" -> iconBtn.addActionListener(e -> JOptionPane.showMessageDialog(null, "You are on the dashboard!"));
        case "Manage Books" -> iconBtn.addActionListener(e -> new BookPanel());
        case "Borrow/Return Book" -> iconBtn.addActionListener(e -> new BorrowReturnPanel());
        case "Search Books" -> iconBtn.addActionListener(e -> new SearchBooksPanel());
        case "Register Member" -> iconBtn.addActionListener(e -> new MemberPanel());
        case "View Fine Report" -> iconBtn.addActionListener(e -> new FineReportPanel());
        case "Logout" -> iconBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });
    }

    sidebar.add(iconBtn);
}


        // Title
        JLabel titleLabel = new JLabel("📚 Library Management Dashboard");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 0));

        // Button Grid
        JPanel buttonGrid = new JPanel(new GridLayout(4, 3, 20, 20));
        buttonGrid.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));
        buttonGrid.setBackground(Color.WHITE);

        // Define Buttons
        JButton membersBtn = createStyledButton("Register Member", new Color(123, 67, 255), new Color(100, 120, 255));
        JButton booksBtn = createStyledButton("Manage Books", new Color(50, 80, 255), new Color(70, 140, 255));
        JButton borrowBtn = createStyledButton("Borrow/Return Book", new Color(0, 210, 190), new Color(0, 160, 150));
        JButton searchBtn = createStyledButton("Search Books", new Color(255, 153, 102), new Color(255, 94, 98));
        JButton viewMembersBtn = createStyledButton("View Member Details", new Color(255, 70, 160), new Color(240, 100, 200));
        JButton viewBorrowedBtn = createStyledButton("View Borrowed Books", new Color(123, 80, 255), new Color(143, 100, 255));
        JButton fineBtn = createStyledButton("View Fine Report", new Color(255, 120, 60), new Color(255, 100, 20));
        JButton changePasswordBtn = createStyledButton("Change Password", new Color(0, 190, 250), new Color(0, 140, 210));
        JButton discardBtn = createStyledButton("Discard Membership", new Color(255, 60, 100), new Color(255, 20, 70));
        JButton logoutBtn = createStyledButton("Logout", new Color(100, 100, 100), new Color(60, 60, 60));

        // Add buttons to grid
        buttonGrid.add(membersBtn);
        buttonGrid.add(booksBtn);
        buttonGrid.add(borrowBtn);
        buttonGrid.add(searchBtn);
        buttonGrid.add(viewMembersBtn);
        buttonGrid.add(viewBorrowedBtn);
        buttonGrid.add(fineBtn);
        buttonGrid.add(changePasswordBtn);
        buttonGrid.add(discardBtn);
        buttonGrid.add(logoutBtn);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(buttonGrid, BorderLayout.CENTER);

        add(sidebar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);

        // Action Listeners
        membersBtn.addActionListener(e -> new MemberPanel());
        booksBtn.addActionListener(e -> new BookPanel());
        borrowBtn.addActionListener(e -> new BorrowReturnPanel());
        searchBtn.addActionListener(e -> new SearchBooksPanel());
        viewMembersBtn.addActionListener(e -> new ViewMembersPanel());
        viewBorrowedBtn.addActionListener(e -> new ViewBorrowedBooksPanel());
        fineBtn.addActionListener(e -> new FineReportPanel());
        changePasswordBtn.addActionListener(e -> new ChangePasswordPanel());
        discardBtn.addActionListener(e -> new DeleteMemberPanel());
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        setVisible(true);
    }

    // Method to create a styled button using custom GradientButton class
    private JButton createStyledButton(String text, Color color1, Color color2) {
        return new GradientButton(text, color1, color2);
    }
}

// Custom button class with gradient and hover effect
class GradientButton extends JButton {
    private Color color1, color2;
    private boolean hovered = false;

    public GradientButton(String text, Color c1, Color c2) {
        super(text);
        this.color1 = c1;
        this.color2 = c2;
        setForeground(Color.WHITE);
        setFont(new Font("SansSerif", Font.BOLD, 14));
        setContentAreaFilled(false);
        setOpaque(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(200, 50));

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                repaint();
                setLocation(getX(), getY() - 2);
            }

            public void mouseExited(MouseEvent e) {
                hovered = false;
                repaint();
                setLocation(getX(), getY() + 2);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color c1 = hovered ? color1.brighter() : color1;
        Color c2 = hovered ? color2.brighter() : color2;
        GradientPaint gp = new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2);
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        super.paintComponent(g);
    }
}