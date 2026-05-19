package utils;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class Theme {

    // ── Colors ───────────────────────────────────────────────────────────────
    public static final Color BG_DARK        = new Color(13,  17,  28);
    public static final Color BG_CARD        = new Color(22,  28,  45);
    public static final Color BG_SIDEBAR     = new Color(17,  22,  36);
    public static final Color ACCENT_BLUE    = new Color(64,  156, 255);
    public static final Color ACCENT_PURPLE  = new Color(124, 77,  255);
    public static final Color ACCENT_GREEN   = new Color(0,   212, 143);
    public static final Color ACCENT_ORANGE  = new Color(255, 149, 64);
    public static final Color ACCENT_RED     = new Color(255, 75,  95);
    public static final Color TEXT_PRIMARY   = new Color(236, 240, 255);
    public static final Color TEXT_SECONDARY = new Color(130, 145, 180);
    public static final Color TEXT_MUTED     = new Color(70,  85,  115);
    public static final Color BORDER_COLOR   = new Color(35,  45,  70);
    public static final Color ROW_HOVER      = new Color(30,  40,  65);
    public static final Color ROW_ALT        = new Color(18,  24,  38);

    // ── Fonts ─────────────────────────────────────────────────────────────────
    public static Font font(int style, float size) { return new Font("Segoe UI", style, (int) size); }
    public static Font bold(float size)            { return font(Font.BOLD,  size); }
    public static Font plain(float size)           { return font(Font.PLAIN, size); }

    // ── Button ────────────────────────────────────────────────────────────────
    public static JButton button(String text, Color bg) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed()  ? bg.darker()  :
                            getModel().isRollover() ? bg.brighter() : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth()  - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setFont(bold(13));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 20, 38));
        return btn;
    }

    // ── TextField ─────────────────────────────────────────────────────────────
    public static JTextField textField(int cols) {
        JTextField tf = new JTextField(cols) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_DARK);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        tf.setFont(plain(13));
        tf.setForeground(TEXT_PRIMARY);
        tf.setBackground(BG_DARK);
        tf.setCaretColor(ACCENT_BLUE);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(6, 10, 6, 10)
        ));
        tf.setOpaque(false);
        return tf;
    }

    // ── PasswordField ─────────────────────────────────────────────────────────
    public static JPasswordField passwordField(int cols) {
        JPasswordField pf = new JPasswordField(cols);
        pf.setFont(plain(13));
        pf.setForeground(TEXT_PRIMARY);
        pf.setBackground(BG_DARK);
        pf.setCaretColor(ACCENT_BLUE);
        pf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(6, 10, 6, 10)
        ));
        return pf;
    }

    // ── ComboBox ──────────────────────────────────────────────────────────────
    public static JComboBox<String> comboBox(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(plain(13));
        cb.setForeground(TEXT_PRIMARY);
        cb.setBackground(BG_DARK);
        cb.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1, true));
        cb.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(
                    JList<?> list, Object val, int idx, boolean sel, boolean focus) {
                super.getListCellRendererComponent(list, val, idx, sel, focus);
                setBackground(sel ? ACCENT_BLUE : BG_DARK);
                setForeground(TEXT_PRIMARY);
                setBorder(new EmptyBorder(4, 10, 4, 10));
                return this;
            }
        });
        return cb;
    }

    // ── ScrollPane ────────────────────────────────────────────────────────────
    public static void styleScrollPane(JScrollPane sp) {
        sp.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        sp.getViewport().setBackground(BG_CARD);
        sp.setBackground(BG_CARD);
        styleScrollBar(sp.getVerticalScrollBar());
        styleScrollBar(sp.getHorizontalScrollBar());
    }

    private static void styleScrollBar(JScrollBar bar) {
        bar.setBackground(BG_CARD);
        bar.setUI(new BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                thumbColor = new Color(50, 65, 100);
                trackColor = BG_CARD;
            }
            @Override protected JButton createDecreaseButton(int o) { return invis(); }
            @Override protected JButton createIncreaseButton(int o) { return invis(); }
            private JButton invis() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                return b;
            }
        });
    }

    // ── Table ─────────────────────────────────────────────────────────────────
    public static void styleTable(JTable table) {
        table.setBackground(BG_CARD);
        table.setForeground(TEXT_PRIMARY);
        table.setFont(plain(13));
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(64, 156, 255, 60));
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setBackground(BG_SIDEBAR);
        table.getTableHeader().setForeground(TEXT_SECONDARY);
        table.getTableHeader().setFont(bold(12));
        table.getTableHeader().setBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
        table.getTableHeader().setPreferredSize(new Dimension(0, 42));
    }

    // ── Card panel ────────────────────────────────────────────────────────────
    public static JPanel card() {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        return p;
    }

    // ── Label ─────────────────────────────────────────────────────────────────
    public static JLabel label(String text, Font font, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }

    // ── Status Badge  (updated: WFH case added) ───────────────────────────────
    public static JLabel badge(String status) {
        JLabel lbl = new JLabel(status, SwingConstants.CENTER) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = switch (status) {
                    case "Present", "Approved" -> new Color(0,   212, 143, 30);
                    case "Late",    "Pending"  -> new Color(255, 149,  64, 30);
                    case "Absent",  "Rejected" -> new Color(255,  75,  95, 30);
                    case "WFH"                 -> new Color(124,  77, 255, 30); // ← NEW
                    default                    -> new Color( 64, 156, 255, 30);
                };
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        Color fg = switch (status) {
            case "Present", "Approved" -> ACCENT_GREEN;
            case "Late",    "Pending"  -> ACCENT_ORANGE;
            case "Absent",  "Rejected" -> ACCENT_RED;
            case "WFH"                 -> ACCENT_PURPLE; // ← NEW
            default                    -> ACCENT_BLUE;
        };
        lbl.setForeground(fg);
        lbl.setFont(bold(11));
        lbl.setOpaque(false);
        lbl.setPreferredSize(new Dimension(80, 24));
        return lbl;
    }
}