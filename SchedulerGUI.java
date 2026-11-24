import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class SchedulerGUI extends JFrame {
    private final DefaultTableModel input = new DefaultTableModel(
            new Object[]{"Process", "Arrival", "Burst", "Priority"}, 0);
    private final JTable inputTable = new JTable(input);
    private final JSpinner quantum = new JSpinner(new SpinnerNumberModel(3, 1, 1000, 1));
    private final JSpinner cs = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
    private final JComboBox<String> algo = new JComboBox<>(new String[]{"SJF", "ACS", "Both"});
    private static final Color BG = new Color(0xF4F6FA), CARD = Color.WHITE, ACCENT = new Color(0x4F46E5),
            TEXT = new Color(0x1F2937), MUTED = new Color(0x6B7280), LINE = new Color(0xE5E7EB);
    private static final Font UI = new Font("SansSerif", Font.PLAIN, 13);

    private static JButton btn(String t, boolean primary) {
        JButton b = new JButton(t);
        b.setFont(UI.deriveFont(Font.BOLD));
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
        b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBackground(primary ? ACCENT : CARD);
        b.setForeground(primary ? Color.WHITE : TEXT);
        b.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(primary ? ACCENT : LINE, 1, true), new EmptyBorder(7, 14, 7, 14)));
        return b;
    }

    private static void style(JTable t) {
        t.setFont(UI);
        t.setRowHeight(28);
        t.setShowVerticalLines(false);
        t.setGridColor(LINE);
        t.setSelectionBackground(new Color(0xE0E7FF));
        t.setSelectionForeground(TEXT);
        t.setIntercellSpacing(new Dimension(0, 1));
        JTableHeader h = t.getTableHeader();
        h.setReorderingAllowed(false);
        h.setPreferredSize(new Dimension(0, 30));
        h.setDefaultRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable tb, Object v, boolean s, boolean f, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(tb, v, false, false, r, c);
                l.setBackground(BG); l.setForeground(MUTED);
                l.setFont(UI.deriveFont(Font.BOLD, 12f));
                l.setBorder(new EmptyBorder(0, 8, 0, 8));
                return l;
            }
        });
        DefaultTableCellRenderer cell = new DefaultTableCellRenderer();
        cell.setBorder(new EmptyBorder(0, 8, 0, 8));
        t.setDefaultRenderer(Object.class, cell);
        t.setDefaultRenderer(Integer.class, cell);
    }

    private static JPanel card(String title) {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(CARD);
        p.setBorder(BorderFactory.createCompoundBorder(new LineBorder(LINE, 1, true), new EmptyBorder(12, 12, 12, 12)));
        if (title != null) {
            JLabel l = new JLabel(title);
            l.setFont(UI.deriveFont(Font.BOLD, 15f));
            l.setForeground(TEXT);
            p.add(l, BorderLayout.NORTH);
        }
        return p;
    }

    private final JTabbedPane results = new JTabbedPane();

    public SchedulerGUI() {
        super("CPU Scheduling Simulator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        UIManager.put("Spinner.font", UI);
        UIManager.put("ComboBox.font", UI);
        UIManager.put("Label.font", UI);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout(12, 12));
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(14, 14, 14, 14));
        style(inputTable);
        results.setFont(UI.deriveFont(Font.BOLD));
        results.setBackground(CARD);

        JPanel left = card("Processes");
        JScrollPane sc = new JScrollPane(inputTable);
        sc.setBorder(new LineBorder(LINE));
        sc.getViewport().setBackground(CARD);
        left.add(sc, BorderLayout.CENTER);
        JPanel rowBtns = new JPanel(new GridLayout(2, 2, 8, 8));
        rowBtns.setOpaque(false);
        JButton add = btn("+ Add", false);
        JButton remove = btn("Remove", false);
        JButton tc1 = btn("Test Case 1", false);
        JButton tc2 = btn("Test Case 2", false);
        rowBtns.add(add); rowBtns.add(remove); rowBtns.add(tc1); rowBtns.add(tc2);
        left.add(rowBtns, BorderLayout.SOUTH);

        JPanel top = card(null);
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        controls.setOpaque(false);
        for (JComponent c : new JComponent[]{algo, quantum, cs}) c.setPreferredSize(new Dimension(c == algo ? 90 : 70, 32));
        controls.add(new JLabel("Algorithm")); controls.add(algo);
        controls.add(new JLabel("Quantum (Q)")); controls.add(quantum);
        controls.add(new JLabel("Context switch (CS)")); controls.add(cs);
        JButton run = btn("Run", true);
        top.add(controls, BorderLayout.CENTER);
        top.add(run, BorderLayout.EAST);

        JPanel right = card("Results");
        right.add(results, BorderLayout.CENTER);

        add(top, BorderLayout.NORTH);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        split.setBorder(null);
        split.setOpaque(false);
        split.setDividerSize(12);
        split.setDividerLocation(400);
        add(split, BorderLayout.CENTER);

        add.addActionListener(e -> input.addRow(new Object[]{"P" + (input.getRowCount() + 1), 0, 1, 1}));
        remove.addActionListener(e -> {
            int r = inputTable.getSelectedRow();
            if (r >= 0) input.removeRow(r);
            else if (input.getRowCount() > 0) input.removeRow(input.getRowCount() - 1);
        });
        tc1.addActionListener(e -> { load(Main2.testCase1()); quantum.setValue(3); cs.setValue(0); });
        tc2.addActionListener(e -> { load(Main2.testCase2()); quantum.setValue(3); cs.setValue(1); });
        run.addActionListener(e -> run());

        load(Main2.testCase1());
        setSize(1100, 680);
        setLocationRelativeTo(null);
    }

    private void load(List<Process> ps) {
        if (inputTable.isEditing()) inputTable.getCellEditor().stopCellEditing();
        input.setRowCount(0);
        for (Process p : ps)
            input.addRow(new Object[]{p.id, p.arrivalTime, p.burstTime, p.initialPriority});
    }

    private int num(int r, int c, String name, int min) {
        Object v = input.getValueAt(r, c);
        int n;
        try { n = Integer.parseInt(String.valueOf(v).trim()); }
        catch (Exception ex) { throw new IllegalArgumentException(name + " in row " + (r + 1) + " must be a whole number."); }
        if (n < min) throw new IllegalArgumentException(name + " in row " + (r + 1) + " must be at least " + min + ".");
        return n;
    }

    private void run() {
        if (inputTable.isEditing()) inputTable.getCellEditor().stopCellEditing();
        try {
            if (input.getRowCount() == 0) throw new IllegalArgumentException("Add at least one process.");
            List<Process> ps = new ArrayList<>();
            Set<String> ids = new HashSet<>();
            for (int r = 0; r < input.getRowCount(); r++) {
                String id = String.valueOf(input.getValueAt(r, 0)).trim();
                if (id.isEmpty() || !ids.add(id)) throw new IllegalArgumentException("Process names must be unique and not empty (row " + (r + 1) + ").");
                ps.add(new Process(id, num(r, 1, "Arrival", 0), num(r, 2, "Burst", 1), num(r, 3, "Priority", 0)));
            }
            int q = (Integer) quantum.getValue();
            int c = (Integer) cs.getValue();
            results.removeAll();
            String a = (String) algo.getSelectedItem();
            if (!a.equals("ACS")) results.addTab("SJF", panel(SJF.runPreemptiveSJF(ps, c)));
            if (!a.equals("SJF")) results.addTab("ACS", panel(ACS.runACS(ps, q, c)));
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JComponent panel(Result res) {
        DefaultTableModel m = new DefaultTableModel(
                new Object[]{"Process", "Arrival", "Finish", "Waiting", "Turnaround", "Response"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Process p : res.processes)
            m.addRow(new Object[]{p.id, p.arrivalTime, p.completionTime, p.waitingTime, p.turnaroundTime, p.responseTime});
        JTable t = new JTable(m);
        style(t);
        JPanel stats = new JPanel(new GridLayout(1, 3, 10, 0));
        stats.setOpaque(false);
        stats.add(stat("Avg Waiting", res.averageWaiting));
        stats.add(stat("Avg Turnaround", res.averageTurnaround));
        stats.add(stat("Avg Response", res.averageResponse));
        JTextArea tl = new JTextArea(String.join("\n", res.timeline));
        tl.setEditable(false);
        tl.setMargin(new Insets(8, 10, 8, 10));
        tl.setBackground(new Color(0xFAFAFC));
        tl.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane ts = new JScrollPane(t);
        ts.setBorder(new LineBorder(LINE));
        ts.getViewport().setBackground(CARD);
        JScrollPane ls = new JScrollPane(tl);
        ls.setBorder(new TitledBorder(new LineBorder(LINE), "Timeline"));
        JPanel upper = new JPanel(new BorderLayout(0, 10));
        upper.setOpaque(false);
        upper.add(stats, BorderLayout.NORTH);
        upper.add(ts, BorderLayout.CENTER);
        JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upper, ls);
        sp.setBorder(null);
        sp.setDividerLocation(230);
        return sp;
    }

    private JComponent stat(String name, double v) {
        JPanel p = new JPanel(new GridLayout(2, 1));
        p.setBackground(new Color(0xEEF2FF));
        p.setBorder(new EmptyBorder(8, 12, 8, 12));
        JLabel n = new JLabel(name);
        n.setForeground(MUTED);
        JLabel val = new JLabel(String.format(Locale.US, "%.2f", v));
        val.setFont(UI.deriveFont(Font.BOLD, 20f));
        val.setForeground(ACCENT);
        p.add(n); p.add(val);
        return p;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new SchedulerGUI().setVisible(true));
    }
}
