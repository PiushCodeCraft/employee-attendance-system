package ui.admin;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import models.Employee;
import services.EmployeeService;
import utils.Theme;

public class ManageEmployees extends JPanel {

    private final EmployeeService empService;
    private DefaultTableModel     tableModel;
    private JTable                table;

    public ManageEmployees(EmployeeService empService) {
        this.empService = empService;
        setOpaque(false);
        setLayout(new BorderLayout(0, 16));
        initUI();
        loadData();
    }

    private void initUI() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(Theme.label("Manage Employees", Theme.bold(22), Theme.TEXT_PRIMARY),
                   BorderLayout.WEST);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setOpaque(false);
        JButton addBtn = Theme.button("+ Add Employee", Theme.ACCENT_BLUE);
        JButton delBtn = Theme.button("Delete",         Theme.ACCENT_RED);
        addBtn.addActionListener(e -> showAddDialog());
        delBtn.addActionListener(e -> deleteSelected());
        btnRow.add(delBtn); btnRow.add(addBtn);
        header.add(btnRow, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        String[] cols = {"ID","Name","Email","Phone","Department","Designation","Role"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        Theme.styleTable(table);
        table.getColumnModel().getColumn(6).setCellRenderer(
            (t, v, s, f, row, col) -> Theme.badge(v != null ? v.toString() : ""));

        JScrollPane sp = new JScrollPane(table);
        Theme.styleScrollPane(sp);
        JPanel card = Theme.card();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(0, 0, 0, 0));
        card.add(sp, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        for (Employee e : empService.getAllEmployees()) {
            tableModel.addRow(new Object[]{
                e.getEmpId(), e.getName(), e.getEmail(), e.getPhone(),
                e.getDepartment(), e.getDesignation(), e.getRole()
            });
        }
    }

    private void showAddDialog() {
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                                  "Add Employee", true);
        dlg.setSize(440, 460);
        dlg.setLocationRelativeTo(this);
        dlg.getContentPane().setBackground(Theme.BG_CARD);

        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Theme.BG_CARD);
        p.setBorder(new EmptyBorder(24, 24, 24, 24));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 4, 6, 4);
        g.fill   = GridBagConstraints.HORIZONTAL;

        JTextField     name  = Theme.textField(18);
        JTextField     email = Theme.textField(18);
        JTextField     phone = Theme.textField(18);
        JTextField     desig = Theme.textField(18);
        JTextField     pass  = Theme.textField(18);
        JComboBox<String> dept = Theme.comboBox(
            new String[]{"Engineering","Human Resources","Finance","Marketing","Operations"});
        JComboBox<String> role = Theme.comboBox(new String[]{"employee","admin"});

        Object[][] rows = {
            {"Name",        name},  {"Email",      email},
            {"Phone",       phone}, {"Designation",desig},
            {"Department",  dept},  {"Role",       role},
            {"Password",    pass}
        };
        for (int i = 0; i < rows.length; i++) {
            g.gridx = 0; g.gridy = i; g.weightx = 0.3;
            p.add(Theme.label(rows[i][0].toString(), Theme.bold(12), Theme.TEXT_SECONDARY), g);
            g.gridx = 1; g.weightx = 0.7;
            p.add((Component) rows[i][1], g);
        }

        JButton save = Theme.button("Save Employee", Theme.ACCENT_GREEN);
        g.gridx = 0; g.gridy = rows.length; g.gridwidth = 2;
        g.insets = new Insets(16, 4, 0, 4);
        p.add(save, g);

        save.addActionListener(e -> {
            Employee emp = new Employee();
            emp.setName       (name.getText().trim());
            emp.setEmail      (email.getText().trim());
            emp.setPhone      (phone.getText().trim());
            emp.setDesignation(desig.getText().trim());
            emp.setDepartment ((String) dept.getSelectedItem());
            emp.setRole       ((String) role.getSelectedItem());
            emp.setPassword   (pass.getText().trim());
            empService.registerEmployee(emp);
            loadData();
            dlg.dispose();
        });

        dlg.add(p);
        dlg.setVisible(true);
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select an employee first."); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        int c  = JOptionPane.showConfirmDialog(this, "Delete this employee?",
                     "Confirm", JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) { empService.deleteEmployee(id); loadData(); }
    }
}