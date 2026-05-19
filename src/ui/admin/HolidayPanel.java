package ui.admin;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import models.Holiday;
import services.HolidayService;
import utils.Theme;

public class HolidayPanel extends JPanel {

    private final HolidayService   holidayService;
    private DefaultTableModel      tableModel;
    private JTable                 table;

    public HolidayPanel(HolidayService holidayService) {
        this.holidayService = holidayService;
        setOpaque(false);
        setLayout(new BorderLayout(0, 16));
        initUI();
    }

    private void initUI() {
        // ── header ────────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(Theme.label("Holiday Calendar", Theme.bold(22), Theme.TEXT_PRIMARY),
                   BorderLayout.WEST);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setOpaque(false);
        JButton addBtn = Theme.button("+ Add Holiday", Theme.ACCENT_BLUE);
        JButton delBtn = Theme.button("Remove",        Theme.ACCENT_RED);
        addBtn.addActionListener(e -> showAddDialog());
        delBtn.addActionListener(e -> removeSelected());
        btnRow.add(delBtn);
        btnRow.add(addBtn);
        header.add(btnRow, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // ── table ─────────────────────────────────────────────────────────────
        String[] cols = {"ID", "Holiday Name", "Date", "Day"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        Theme.styleTable(table);

        JScrollPane sp = new JScrollPane(table);
        Theme.styleScrollPane(sp);
        JPanel card = Theme.card();
        card.setLayout(new BorderLayout());
        card.add(sp, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);

        // ── upcoming info bar ─────────────────────────────────────────────────
        JPanel info = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        info.setOpaque(false);
        List<Holiday> upcoming = holidayService.getUpcoming();
        if (!upcoming.isEmpty()) {
            Holiday next = upcoming.get(0);
            info.add(Theme.label("Next holiday: " + next.getName()
                    + "  (" + next.getDate() + ")",
                    Theme.bold(13), Theme.ACCENT_ORANGE));
        }
        add(info, BorderLayout.SOUTH);

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        for (Holiday h : holidayService.getAll()) {
            tableModel.addRow(new Object[]{
                h.getId(),
                h.getName(),
                h.getDate(),
                h.getDate().getDayOfWeek().toString()
            });
        }
    }

    private void showAddDialog() {
        JTextField nameField = Theme.textField(18);
        JTextField dateField = Theme.textField(18);
        dateField.setText(LocalDate.now().toString());

        JPanel form = new JPanel(new GridLayout(4, 1, 0, 8));
        form.setBackground(Theme.BG_CARD);
        form.setBorder(new EmptyBorder(8, 8, 8, 8));
        form.add(Theme.label("Holiday Name:",    Theme.bold(12), Theme.TEXT_SECONDARY));
        form.add(nameField);
        form.add(Theme.label("Date (yyyy-MM-dd):", Theme.bold(12), Theme.TEXT_SECONDARY));
        form.add(dateField);

        int result = JOptionPane.showConfirmDialog(
            this, form, "Add Holiday", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Holiday name cannot be empty.");
                return;
            }
            try {
                LocalDate date = LocalDate.parse(dateField.getText().trim());
                holidayService.add(name, date);
                loadData();
                JOptionPane.showMessageDialog(this, "Holiday added: " + name);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid date. Use yyyy-MM-dd format.");
            }
        }
    }

    private void removeSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a holiday to remove.");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(
            this, "Remove this holiday?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            holidayService.remove(id);
            loadData();
        }
    }
}