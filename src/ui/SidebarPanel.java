package ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import utils.Theme;

public class SidebarPanel extends JPanel {

    private final List<SidebarItem> items = new ArrayList<>();
    private int selectedIndex = 0;

    public SidebarPanel() {
        setLayout(null);
        setOpaque(false);
        setPreferredSize(new Dimension(220, 0));
    }

    @Override protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Theme.BG_SIDEBAR);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(Theme.BORDER_COLOR);
        g2.drawLine(getWidth()-1, 0, getWidth()-1, getHeight());
        g2.dispose();
    }

    public void addItem(String icon, String label, Runnable action) {
        int idx = items.size();
        SidebarItem item = new SidebarItem(icon, label, idx, action);
        item.setBounds(12, 80 + idx * 52, 196, 44);
        items.add(item);
        add(item);
        repaint();
    }

    public void select(int index) {
        selectedIndex = index;
        for (SidebarItem item : items) item.repaint();
    }

    class SidebarItem extends JPanel {
        final String  icon, label;
        final int     index;
        final Runnable action;
        boolean hovered = false;

        SidebarItem(String icon, String label, int index, Runnable action) {
            this.icon = icon; this.label = label;
            this.index = index; this.action = action;
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                public void mouseExited (MouseEvent e) { hovered = false; repaint(); }
                public void mouseClicked(MouseEvent e) { select(index);   action.run(); }
            });
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            boolean active = selectedIndex == index;
            if (active) {
                g2.setColor(new Color(64, 156, 255, 30));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(Theme.ACCENT_BLUE);
                g2.fillRoundRect(0, 8, 4, 28, 4, 4);
            } else if (hovered) {
                g2.setColor(new Color(255, 255, 255, 8));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            }
            g2.setFont(Theme.plain(16));
            g2.setColor(active ? Theme.ACCENT_BLUE : Theme.TEXT_SECONDARY);
            g2.drawString(icon, 16, 28);
            g2.setFont(Theme.bold(13));
            g2.setColor(active ? Theme.TEXT_PRIMARY : Theme.TEXT_SECONDARY);
            g2.drawString(label, 46, 28);
            g2.dispose();
        }
    }
}