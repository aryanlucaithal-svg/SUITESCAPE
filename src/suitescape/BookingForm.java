package suitescape;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class BookingForm extends JFrame {
    private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("yyyy-MM-dd");
    private static final String[] PACKAGES = {
        "Day Tour (9:00 AM - 5:00 PM) - P5,500","Night Swim (7:00 PM - 3:00 AM) - P6,500","Exclusive (24-Hours) - P15,500 "};
    
    private final ArrayList<Record> bookings = new ArrayList<>();
    private final ArrayList<Record> events = new ArrayList<>();
    private final ArrayList<Transaction> transactions = new ArrayList<>();
    private int nextId = 1001;
    private int nextTxnId = 1001;
    
    private final DefaultTableModel tableModel = new DefaultTableModel(new String[] {
        "ID", "Guest", "Mobile", "Date", "Guests", "Package", "Payment"},0);
    private final DefaultTableModel txnTableModel = new DefaultTableModel(new String[] {
        "Txn #","Booking ID", "Guest", "Total", "Method", "Status"},0);
   
    private JTextField txtName,txtMobile,fieldBookingDate,txtSubtotal,txtDiscount;
    private JTextField txtTotal,txtTendered,txtEvent,fieldEventDate,txtSearchTxn;
    private JComboBox comboPackage,comboMethod,comboDiscount;
    private JLabel lblChange,lblTotalSales;
    private JTable table,txnTable;
    private JSpinner spinGuests;
    private DefaultListModel<String> blockListModel = new DefaultListModel<String>();
    private JList<String> blockList;
    private JButton btnSave, btnClear;
    private String editingId = null;
    private static final Color PINK  = new Color(255, 192, 203);
    private static final Color GREEN = new Color(52, 138, 108);
    private static final Color BG    = new Color(242, 247, 245);
    private static final Font FONT = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font BOLD = new Font("SansSerif", Font.BOLD, 13);
    
    public BookingForm(){
        setTitle("Reception Console ");
        setIconImage(new ImageIcon("C:\\Users\\User\\OneDrive\\Pictures\\Saved Pictures\\zyzy.jpg").getImage());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 820);
        setLocationRelativeTo(null);
        
        JLabel title = new JLabel("SUITESCAPE Resort Reservation System");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(new Color(41,11,6));
        title.setOpaque(true);
        title.setBackground(PINK);
        title.setBorder(new EmptyBorder(15, 25, 15, 25));
        
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(BOLD);
        tabs.addTab("New Reservation",bookingTab());
        tabs.addTab("Blocked Dates", blockedTab());
        tabs.addTab("Transactions", transactionTab());

        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }
    
    private JPanel bookingTab(){
       JPanel tab = new JPanel(new BorderLayout(0, 15));
        tab.setBackground(BG);
        tab.setBorder(new EmptyBorder(20, 25, 20, 25));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);

        txtName = new JTextField();
        txtMobile = new JTextField();
        fieldBookingDate = makeField(DATE_FMT.format(new Date()), true);
        spinGuests = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        comboPackage = new JComboBox(PACKAGES);
        comboMethod = new JComboBox(new String[]{"Cash", "Card", "GCash", "Maya", "Bank Transfer"});
        comboDiscount = new JComboBox(new String[]{"No Discount (0%)", "Senior Discount (7%)", "PWD Discount (8%)"});
        txtSubtotal = makeField("", false);
        txtDiscount = makeField("", false);
        txtTotal = makeField("", false);
        txtTendered = new JTextField("");

        lblChange = new JLabel("P0.00");
        lblChange.setFont(BOLD);
        lblChange.setForeground(GREEN);

        comboPackage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { 
                updateReservationTotals(); 
            }
        });
        comboDiscount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { 
                updateReservationTotals(); 
            }
        });
        DocumentListener live = new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { 
                updateReservationTotals(); 
            }
            public void removeUpdate(DocumentEvent e)  { 
                updateReservationTotals(); 
            }
            public void changedUpdate(DocumentEvent e) { 
                updateReservationTotals(); 
            }
        };
        txtTendered.getDocument().addDocumentListener(live);

        addField(form, "Guest Name", txtName, 0, 0);
        addField(form, "Mobile Number (11 digits)", txtMobile, 1, 0);
        addField(form, "Guest Count", spinGuests, 0, 1);
        addField(form, "Reservation Date (YYYY-MM-DD)", fieldBookingDate, 1, 1);
        addField(form, "Rate Package & Session", comboPackage, 0, 2);
        addField(form, "Subtotal", txtSubtotal, 1, 2);
        addField(form, "Discount Type", comboDiscount, 0, 3);
        addField(form, "Discount Amount", txtDiscount, 1, 3);
        addField(form, "Total", txtTotal, 0, 4);
        addField(form, "Payment Method", comboMethod, 1, 4);
        addField(form, "Amount Tendered", txtTendered, 0, 5);
        addField(form, "Change", lblChange, 1, 5);

        btnSave  = makeButton("Save Reservation & Payment", GREEN, Color.WHITE);
        btnClear = makeButton("Clear Form", new Color(213, 230, 240), Color.DARK_GRAY);

        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { saveBooking(); }
        });
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { clearForm(); }
        });
        //DITO AKO NAHINTO

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttons.setOpaque(false);
        buttons.add(btnSave);
        buttons.add(btnClear);

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.gridwidth = 2; g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(10, 8, 4, 8);
        g.gridy = 12; form.add(buttons, g);

        table = new JTable(tableModel);
        table.setRowHeight(26);
        table.setFont(FONT);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(BOLD);

        JButton btnUpdate = makeButton("Update Selected Reservation", PINK, Color.WHITE);
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { updateReservation(); }
        });

        JButton btnCancel = makeButton("Cancel Selected Reservation", new Color(200, 70, 70), Color.WHITE);
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { cancelReservation(); }
        });

        JPanel cancelBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        cancelBar.setOpaque(false);
        cancelBar.add(btnUpdate);
        cancelBar.add(btnCancel);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        tablePanel.add(cancelBar, BorderLayout.SOUTH);

        tab.add(form, BorderLayout.NORTH);
        tab.add(tablePanel, BorderLayout.CENTER);

        updateReservationTotals();
        return tab;
    }
    private JPanel blockedTab(){
        JPanel tab = new JPanel(new BorderLayout(0, 15));
        tab.setBackground(BG);
        tab.setBorder(new EmptyBorder(20, 25, 20, 25));

        JPanel eventPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        eventPanel.setBackground(Color.WHITE);

        txtEvent = new JTextField(20);
        fieldEventDate = makeField(DATE_FMT.format(new Date()), true);

        JButton btnAdd = makeButton("Add to Blocked List", GREEN, Color.WHITE);
        btnAdd.addActionListener((ActionEvent e) -> {
            addEvent();
        });

        eventPanel.add(new JLabel("Label / Reason:"));
        eventPanel.add(txtEvent);
        eventPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        eventPanel.add(fieldEventDate);
        eventPanel.add(btnAdd);

        blockList = new JList<String>(blockListModel);
        blockList.setFont(FONT);
        blockList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        blockList.setFixedCellHeight(28);
        blockList.setBackground(Color.WHITE);

        JButton btnUnblock = makeButton("Unblock Selected Date", new Color(200, 70, 70), Color.WHITE);
        btnUnblock.addActionListener((ActionEvent e) -> {
            unblockSelected();
        });

        JPanel unblockBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 8));
        unblockBar.setOpaque(false);
        unblockBar.add(btnUnblock);

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(Color.WHITE);
        listPanel.add(new JScrollPane(blockList), BorderLayout.CENTER);
        listPanel.add(unblockBar, BorderLayout.SOUTH);

        tab.add(eventPanel, BorderLayout.NORTH);
        tab.add(listPanel,  BorderLayout.CENTER);
        return tab;
    }
    private JPanel transactionTab(){
        JPanel tab = new JPanel(new BorderLayout(0, 15));
        tab.setBackground(BG);
        tab.setBorder(new EmptyBorder(20, 25, 20, 25));

        txnTable = new JTable(txnTableModel);
        txnTable.setRowHeight(26);
        txnTable.setFont(FONT);
        txnTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        txnTable.getTableHeader().setFont(BOLD);

        txtSearchTxn = new JTextField(20);
        JButton btnSearchTxn = makeButton("Search", GREEN, Color.WHITE);
        JButton btnClearTxn  = makeButton("Clear Search", new Color(213, 230, 240), Color.DARK_GRAY);
        //SEACRH BUTTON
        btnSearchTxn.addActionListener((ActionEvent e) -> {
            searchTransactions();
        });
        //CLEAR BUTTON
        btnClearTxn.addActionListener((ActionEvent e) -> {
            txtSearchTxn.setText("");
            txnTable.clearSelection();
        });
        txtSearchTxn.addActionListener((ActionEvent e) -> {
            searchTransactions();
        });

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        searchBar.setOpaque(false);
        searchBar.add(new JLabel("Search Transaction No."));
        searchBar.add(txtSearchTxn);
        searchBar.add(btnSearchTxn);
        searchBar.add(btnClearTxn);

        lblTotalSales = new JLabel("Total Sales: P0.00");
        lblTotalSales.setFont(BOLD);
        lblTotalSales.setForeground(new Color(20, 110, 80));

        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 8));
        bottomBar.setOpaque(false);
        bottomBar.add(lblTotalSales);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.add(searchBar, BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(txnTable), BorderLayout.CENTER);
        tablePanel.add(bottomBar, BorderLayout.SOUTH);

        tab.add(tablePanel, BorderLayout.CENTER);
        return tab;
    }
    private void searchTransactions(){
        String query = txtSearchTxn.getText().trim();

        if (query.equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter a Transaction No. to search.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int low = 0;
        int high = transactions.size() - 1;
        int foundIndex = -1;

        while (low <= high) {
            int mid = (low + high) / 2;
            Transaction midTxn = transactions.get(mid);
            int bs = midTxn.compareTxnTo(query);

            if (bs == 0) {
                foundIndex = mid;
                break;
            } else if (bs < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        if (foundIndex == -1) {
            JOptionPane.showMessageDialog(this, "No transaction found with No. \"" + query + "\".", "Not Found", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        txnTable.setRowSelectionInterval(foundIndex, foundIndex);
        txnTable.scrollRectToVisible(txnTable.getCellRect(foundIndex, 0, true));
    }
    private void updateReservationTotals(){
        String pkg = (String) comboPackage.getSelectedItem();
        if (pkg == null) return;

        double subtotal = getPackagePrice(pkg);
        txtSubtotal.setText(peso(subtotal));

        double discountRate = getDiscountRate((String) comboDiscount.getSelectedItem());
        double discount = subtotal * discountRate / 100.0;
        txtDiscount.setText(peso(discount));

        double total = subtotal - discount;
        if (total < 0) total = 0;
        txtTotal.setText(peso(total));

        String tenderedStr = txtTendered.getText().trim();
        if (tenderedStr.equals("")) tenderedStr = "0";
        double tendered = 0;
        if (isNumber(tenderedStr)) tendered = Double.parseDouble(tenderedStr);

        double change = tendered - total;
        if (change < 0) change = 0;
        lblChange.setText(peso(change));
    }    
    private void saveBooking(){
        String name    = txtName.getText().trim();
        String mobile  = txtMobile.getText().trim();
        String date    = fieldBookingDate.getText().trim();
        String pkg     = (String) comboPackage.getSelectedItem();
        String method  = (String) comboMethod.getSelectedItem();

        // CHECKS IF THERES MISSING SPOTS LEFT
        if (name.equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter the guest name.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (mobile.length() != 11 || !isAllDigits(mobile)) {
            JOptionPane.showMessageDialog(this, "Mobile number must be exactly 11 digits.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!isValidDate(date)) {
            JOptionPane.showMessageDialog(this, "Please enter the reservation date as YYYY-MM-DD (e.g. 2026-12-25).", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // THIS CHECKS IF THERE IS A RECORD AND ID
        Record existingRec = null;
        if (editingId != null) {
            for (int i = 0; i < bookings.size(); i++) {
                if (bookings.get(i).hasId(editingId)) {
                    existingRec = bookings.get(i);
                    break;
                }
            }
            if (existingRec == null) {
                JOptionPane.showMessageDialog(this, "That booking no longer exists.", "Error", JOptionPane.ERROR_MESSAGE);
                clearForm();
                return;
            }
        }

        // THIS ONE CHECKS IF THE DATE IS A NEW BOOKING OR IF ITS JUST CHANGED
        boolean dateChanged = (editingId == null) || !date.equals(existingRec.date);
        if (dateChanged) {
            if (isPast(date)) {
                JOptionPane.showMessageDialog(this, "Reservations cannot be made for a date that has already passed.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (isBooked(date, editingId)) {
                JOptionPane.showMessageDialog(this, "That date is already reserved. Please pick another date.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (isBlocked(date)) {
                JOptionPane.showMessageDialog(this, "That date is blocked. Please pick another date.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // ---------- money ----------
        double subtotal = getPackagePrice(pkg);
        double discountRate = getDiscountRate((String) comboDiscount.getSelectedItem());
        double discount = subtotal * discountRate / 100.0;
        double total = subtotal - discount;
        if (total < 0) 
            total = 0;

        String tenderedStr = txtTendered.getText().trim();
        if (tenderedStr.equals("")) tenderedStr = "0";
        if (!isNumber(tenderedStr)) {
            JOptionPane.showMessageDialog(this, "Amount tendered must be a valid number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double tendered = Double.parseDouble(tenderedStr);

        if (tendered < total) {
            JOptionPane.showMessageDialog(this, "Amount tendered is less than the total.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double change = tendered - total;

        int guests = (int) spinGuests.getValue();

        if (editingId == null) {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Subtotal:  " + peso(subtotal) + "\n" +
                    "Discount:  " + peso(discount) + "\n" +
                    "Total:     " + peso(total) + "\n" +
                    "Tendered:  " + peso(tendered) + "\n" +
                    "Change:    " + peso(change) + "\n" +
                    "Method:    " + method + "\n\n" +
                    "Confirm and save reservation?",
                    "Confirm Payment", JOptionPane.YES_NO_OPTION);
            if (choice != JOptionPane.YES_OPTION) return;

            String id = "BK-" + nextId;
            nextId++;

            bookings.add(new Record(id, name, mobile, date, guests, pkg, "Paid"));
            tableModel.addRow(new Object[]{id, name, mobile, date, guests, pkg, "Paid"});

            String txnId = "TX-" + nextTxnId;
            nextTxnId++;

            Transaction txn = new Transaction(txnId, id, name, method, total);
            transactions.add(txn);
            txnTableModel.addRow(new Object[]{txnId, id, name, peso(total), method, txn.status});

            updateTotalSales();
            clearForm();
            showReceipt(txnId, id, name, date, guests, pkg, subtotal, discount, total, method, tendered, change);

        } else {
            //THIS FOR EDITING
            existingRec.name = name;
            existingRec.mobile = mobile;
            existingRec.date = date;
            existingRec.guest = guests;
            existingRec.packageName = pkg;

            for (int i = 0; i < transactions.size(); i++) {
                Transaction t = transactions.get(i);
                if (t.belongsTo(editingId)) {
                    t.guest = name;
                    t.method = method;
                    t.total = total;

                    for (int j = 0; j < txnTableModel.getRowCount(); j++) {
                        if (txnTableModel.getValueAt(j, 0).equals(t.txnId)) {
                            txnTableModel.setValueAt(name,j, 2);
                            txnTableModel.setValueAt(peso(total), j, 3);
                            txnTableModel.setValueAt(method,j, 4);
                            break;
                        }
                    }
                    break;
                }
            }

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).equals(editingId)) {
                    tableModel.setValueAt(name,   i, 1);
                    tableModel.setValueAt(mobile, i, 2);
                    tableModel.setValueAt(date,   i, 3);
                    tableModel.setValueAt(guests, i, 4);
                    tableModel.setValueAt(pkg,    i, 5);
                    break;
                }
            }

            updateTotalSales();
            clearForm();
        }
    }   
    private void showReceipt(String txnId, String bookingId, String guest, 
                             String date, int guests, String pkg, double subtotal, 
                             double discount, double total, String method, double tendered, double change){
        String line = "----------------------------------------\n";
        String receipt = "";
        receipt = receipt + "         SUITESCAPE RESORT\n";
        receipt = receipt + "           Official Receipt\n";
        receipt = receipt + line;
        receipt = receipt + "Txn No.    : " + txnId + "\n";
        receipt = receipt + "Booking ID : " + bookingId + "\n";
        receipt = receipt + line;
        receipt = receipt + "Guest      : " + guest + "\n";
        receipt = receipt + "Reserv.    : " + date + "\n";
        receipt = receipt + "Pax        : " + guests + "\n";
        receipt = receipt + "Package    : " + pkg + "\n";
        receipt = receipt + line;
        receipt = receipt + "Subtotal   : " + peso(subtotal) + "\n";
        receipt = receipt + "Discount   : " + peso(discount) + "\n";
        receipt = receipt + "TOTAL      : " + peso(total) + "\n";
        receipt = receipt + line;
        receipt = receipt + "Method     : " + method + "\n";
        receipt = receipt + "Tendered   : " + peso(tendered) + "\n";
        receipt = receipt + "Change     : " + peso(change) + "\n";
        receipt = receipt + line;
        receipt = receipt + "    Thank you for your reservation!\n";

        JTextArea area = new JTextArea(receipt);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        area.setEditable(false);
        area.setBackground(Color.WHITE);
        area.setBorder(new EmptyBorder(10, 10, 10, 10));

        JOptionPane.showMessageDialog(this, area, "Receipt - " + txnId, JOptionPane.INFORMATION_MESSAGE);
    }
    private void updateReservation(){
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a reservation in the table first.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String editId = (String) tableModel.getValueAt(row, 0);

        Record found = null;
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).hasId(editId)) {
                found = bookings.get(i);
                break;
            }
        }
        if (found == null) return;

        Transaction txn = null;
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).belongsTo(editId)) {
                txn = transactions.get(i);
                break;
            }
        }

        editingId = editId;

        txtName.setText(found.name);
        txtMobile.setText(found.mobile);
        fieldBookingDate.setText(found.date);
        spinGuests.setValue(found.guest);
        comboPackage.setSelectedItem(found.packageName);

        if (txn != null) {
            comboMethod.setSelectedItem(txn.method);

            double sub = getPackagePrice(found.packageName);
            double rate = 0;
            if (sub > 0) {
                rate = ((sub - txn.total) / sub) * 100.0;
            }

            int bestIndex = 0;
            double bestDiff = Math.abs(rate - 0);
            if (Math.abs(rate - 7) < bestDiff) { bestIndex = 1; bestDiff = Math.abs(rate - 7); }
            if (Math.abs(rate - 8) < bestDiff) { bestIndex = 2; }
            comboDiscount.setSelectedIndex(bestIndex);

            txtTendered.setText(String.format("%.2f", txn.total));
        } else {
            txtTendered.setText("0");
        }

        updateReservationTotals();

        btnSave.setText("Save Changes");
        btnClear.setText("Cancel Edit");
    }
    private void cancelReservation(){
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a reservation in the table first.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String id    = (String) tableModel.getValueAt(row, 0);
        String guest = (String) tableModel.getValueAt(row, 1);

        int choice = JOptionPane.showConfirmDialog(this,
                "Cancel reservation " + id + " for " + guest + "?\n" +
                "The linked transaction will be voided.",
                "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) return;

        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            if (t.belongsTo(id)) {
                if (t.isPaid()) {
                    t.voidTransaction();
                    for (int j = 0; j < txnTableModel.getRowCount(); j++) {
                        if (txnTableModel.getValueAt(j, 0).equals(t.txnId)) {
                            txnTableModel.setValueAt("Voided", j, 5);
                            break;
                        }
                    }
                    break;
                }
            }
        }

        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).hasId(id)) {
                bookings.remove(i);
                break;
            }
        }
        tableModel.removeRow(row);
        updateTotalSales();

        if (editingId != null && editingId.equals(id)) {
            clearForm();
        }
    }
    private void updateTotalSales(){
        double sum = 0;
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            if (t.isPaid()) {
                sum = sum + t.total;
            }
        }
        lblTotalSales.setText("Total Sales: " + peso(sum));
    }
    private JTextField makeField(String initialValue, boolean editable){
        
        JTextField f = new JTextField(initialValue);
        f.setEditable(editable);
        f.setBackground(Color.WHITE);
        f.setFont(FONT);
        return f;
    }
    private boolean isValidDate(String s){
        if (s == null) return false;
        if (s.length() != 10) return false;
        if (s.charAt(4) != '-') return false;
        if (s.charAt(7) != '-') return false;

        String yearStr  = s.substring(0, 4);
        String monthStr = s.substring(5, 7);
        String dayStr   = s.substring(8, 10);

        if (!isAllDigits(yearStr))  return false;
        if (!isAllDigits(monthStr)) return false;
        if (!isAllDigits(dayStr))   return false;

        int month = Integer.parseInt(monthStr);
        int day   = Integer.parseInt(dayStr);

        if (month < 1 || month > 12) return false;
        if (day   < 1 || day   > 31) return false;

        return true;
    }
    private void addEvent(){
        String date = fieldEventDate.getText().trim();

        if (!isValidDate(date)) {
            JOptionPane.showMessageDialog(this, "Please enter the date as YYYY-MM-DD (e.g. 2026-12-25).", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (isPast(date)) {
            JOptionPane.showMessageDialog(this, "You cannot add an event on a date that has already passed.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (isBooked(date, null)) {
            JOptionPane.showMessageDialog(this, "That date is already reserved. Reserved dates cannot be blocked.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String label = txtEvent.getText().trim();
        if (label.equals("")) label = "Blocked";

        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).isOnDate(date)) {
                events.remove(i);
                break;
            }
        }
        events.add(new Record(date, label));

        txtEvent.setText("");
        refreshBlockList();
    }
    private void unblockSelected(){
        int index = blockList.getSelectedIndex();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Please select a blocked date from the list first.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        events.remove(index);
        refreshBlockList();
    }
    private void refreshBlockList(){
        blockListModel.clear();
        for (int i = 0; i < events.size(); i++) {
            Record ev = events.get(i);
            blockListModel.addElement(ev.date + "     |     " + ev.label);
        }
    }
    private void clearForm(){
        editingId = null;

        txtName.setText("");
        txtMobile.setText("");
        spinGuests.setValue(1);
        comboPackage.setSelectedIndex(0);
        comboMethod.setSelectedIndex(0);
        comboDiscount.setSelectedIndex(0);
        txtTendered.setText("0");
        fieldBookingDate.setText(DATE_FMT.format(new Date()));
        updateReservationTotals();

        btnSave.setText("Save Reservation & Payment");
        btnClear.setText("Clear Form");
        table.clearSelection();
    }
    private boolean isAllDigits(String s){
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) return false;
        }
        return true;
    }
    private boolean isNumber(String s){
        if (s.equals("")) return false;
        boolean seenDot = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '.') {
                if (seenDot) return false;
                seenDot = true;
            } else if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
    private boolean isPast(String dates){
        String today = DATE_FMT.format(new Date());
        return dates.compareTo(today) < 0;
    }
    private boolean isBooked(String dates, String ignoreId){
        for (int i = 0; i < bookings.size(); i++) {
            Record b = bookings.get(i);
            if (b.isOnDate(dates)) {
                if (ignoreId == null || !b.hasId(ignoreId)) {
                    return true;
                }
            }
        }
        return false;
    }
    private boolean isBlocked(String dates){
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).isOnDate(dates)) {
                return true;
            }
        }
        return false;
    }
    private double getPackagePrice(String pkg){
        int idx = pkg.lastIndexOf("P");
        String num = pkg.substring(idx + 1).replace(",", "");
        return Double.parseDouble(num);
    }
    private double getDiscountRate(String option){
        if (option == null) return 0;
        int open  = option.lastIndexOf("(");
        int close = option.indexOf("%");
        if (open < 0 || close < 0 || close <= open) return 0;
        String num = option.substring(open + 1, close);
        if (!isNumber(num)) return 0;
        return Double.parseDouble(num);
    }
    private String peso(double amount){
        return "P" + String.format("%,.2f", amount);
    }
    private void addField(JPanel p, String label, JComponent field, int col, int row){
        GridBagConstraints g = new GridBagConstraints();
        g.gridx   = col;
        g.weightx = 1.0;
        g.fill    = GridBagConstraints.HORIZONTAL;
        g.insets  = new Insets(4, 8, 0, 8);

        JLabel l = new JLabel(label);
        l.setFont(BOLD);
        g.gridy = row * 2;
        p.add(l, g);

        field.setFont(FONT);
        g.gridy  = row * 2 + 1;
        g.insets = new Insets(2, 8, 8, 8);
        p.add(field, g);
    }
    private static JButton makeButton(String text, Color bg, Color fg) {
    JButton b = new JButton(text); 
    b.setFont(BOLD);
    b.setBackground(bg);
    b.setForeground(fg);
    b.setBorder(new EmptyBorder(8, 16, 8, 16));
    return b;
    }
    
    public static void main(String[] args) {
        BookingForm b = new BookingForm();
        b.setVisible(true);
    }
}