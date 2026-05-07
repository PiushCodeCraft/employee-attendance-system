package ui;

import models.Employee;
import services.EmployeeService;
import ui.admin.AdminDashboard;
import ui.employee.EmployeeDashboard;
import utils.Theme;

import javax.swing.*;
// import javax.swing.border.EmptyBorder;
import java.awt.*;
// import java.awt.event.*;
// import java.awt.geom.RoundRectangle2D;

public class LoginFrame extends JFrame {

    private final EmployeeService employeeService = new EmployeeService();
    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel statusLabel;

    public LoginFrame() {
        setTitle("Attendance System — Login");
        setSize(900, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new GridLayout(1, 2)) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Theme.BG_DARK);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        root.setOpaque(true);

        // LEFT PANEL — branding
        JPanel left = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(20, 30, 60), getWidth(), getHeight(), new Color(10, 15, 35));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Decorative circles
                g2.setColor(new Color(64, 156, 255, 25));
                g2.fillOval(-60, -60, 280, 280);
                g2.setColor(new Color(124, 77, 255, 20));
                g2.fillOval(getWidth() - 150, getHeight() - 150, 250, 250);
                g2.setColor(new Color(0, 212, 143, 15));
                g2.fillOval(50, getHeight() - 200, 200, 200);
                g2.dispose();
            }
        };
        left.setOpaque(false);

        // Logo circle
        JPanel logoCircle = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, Theme.ACCENT_BLUE, getWidth(), getHeight(), Theme.ACCENT_PURPLE);
                g2.setPaint(gp);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(Color.WHITE);
                g2.setFont(Theme.bold(28));
                FontMetrics fm = g2.getFontMetrics();
                String t = "EAS";
                g2.drawString(t, (getWidth() - fm.stringWidth(t)) / 2, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        logoCircle.setOpaque(false);
        logoCircle.setBounds(165, 130, 80, 80);
        left.add(logoCircle);

        JLabel title = Theme.label("Attendance", Theme.bold(28), Theme.TEXT_PRIMARY);
        title.setBounds(100, 230, 250, 36);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        left.add(title);

        JLabel subtitle = Theme.label("Management System", Theme.bold(28), Theme.ACCENT_BLUE);
        subtitle.setBounds(80, 265, 290, 36);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        left.add(subtitle);

        JLabel desc = Theme.label("<html><center>Track attendance, manage<br>leaves and monitor performance</center></html>",
                Theme.plain(13), Theme.TEXT_SECONDARY);
        desc.setBounds(80, 320, 290, 50);
        desc.setHorizontalAlignment(SwingConstants.CENTER);
        left.add(desc);

        // Divider line
        JSeparator sep = new JSeparator(JSeparator.VERTICAL);
        sep.setForeground(Theme.BORDER_COLOR);

        root.add(left);

        // RIGHT PANEL — login form
        JPanel right = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(Theme.BG_CARD);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        right.setOpaque(true);

        JLabel loginTitle = Theme.label("Welcome back", Theme.bold(24), Theme.TEXT_PRIMARY);
        loginTitle.setBounds(60, 90, 280, 32);
        right.add(loginTitle);

        JLabel loginSub = Theme.label("Sign in to your account", Theme.plain(14), Theme.TEXT_SECONDARY);
        loginSub.setBounds(60, 128, 280, 22);
        right.add(loginSub);

        // Email
        JLabel emailLbl = Theme.label("Email Address", Theme.bold(12), Theme.TEXT_SECONDARY);
        emailLbl.setBounds(60, 185, 200, 18);
        right.add(emailLbl);

        emailField = Theme.textField(20);
        emailField.setBounds(60, 207, 320, 40);
        right.add(emailField);

        // Password
        JLabel passLbl = Theme.label("Password", Theme.bold(12), Theme.TEXT_SECONDARY);
        passLbl.setBounds(60, 265, 200, 18);
        right.add(passLbl);

        passwordField = Theme.passwordField(20);
        passwordField.setBounds(60, 287, 320, 40);
        right.add(passwordField);

        // Status
        statusLabel = Theme.label("", Theme.plain(12), Theme.ACCENT_RED);
        statusLabel.setBounds(60, 337, 320, 20);
        right.add(statusLabel);

        // Login button
        JButton loginBtn = Theme.button("Sign In →", Theme.ACCENT_BLUE);
        loginBtn.setBounds(60, 365, 320, 44);
        right.add(loginBtn);

        // Hint
        JLabel hint = Theme.label("Demo: admin@company.com / admin123", Theme.plain(11), Theme.TEXT_MUTED);
        hint.setBounds(60, 425, 320, 18);
        right.add(hint);

        root.add(right);
        setContentPane(root);

        loginBtn.addActionListener(e -> handleLogin());
        passwordField.addActionListener(e -> handleLogin());
        emailField.addActionListener(e -> passwordField.requestFocus());
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        if (email.isEmpty() || password.isEmpty()) {
            statusLabel.setText("⚠  Please enter email and password.");
            return;
        }
        Employee emp = employeeService.login(email, password);
        if (emp != null) {
            dispose();
            if ("admin".equals(emp.getRole())) new AdminDashboard(emp).setVisible(true);
            else new EmployeeDashboard(emp).setVisible(true);
        } else {
            statusLabel.setText("✕  Invalid email or password.");
            passwordField.setText("");
        }
    }
}
