package inventorymanagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class Orders_Page extends JFrame implements ActionListener {

    private static JTable jTableOrders;
    private JComboBox payType;
    private JTextField emailText;
    private JTextField nameText;
    private JTextField productText;
    private JTextField quantityText;
    private JTextField priceText;
    private JButton addButton;
    private JButton setCustomerButton;
    private int currentOrder;
    private JTextField searchText;
    private JButton endOrderButton;
    private TableRowSorter<DefaultTableModel> tr;
    LocalDateTime now;

    Orders_Page() {
        super("Order Information");
        JPanel pane = new JPanel();
        setLayout(new GridLayout(1, 1));
        setSize(1300, 750);
        pane.setBackground(new Color(147, 134, 134));
        pane.setLayout(null);

        //Add product panel with border
        JPanel setCustomer = new JPanel();
        setCustomer.setLayout(null);
        setCustomer.setOpaque(false);
        Border border = BorderFactory.createLineBorder(new Color(160, 170, 160), 1, true);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(border, "Start Order ", TitledBorder.LEFT,
                TitledBorder.TOP, new Font("Arial", Font.BOLD, 10));
        titledBorder.setTitleColor(Color.WHITE);
        setCustomer.setBorder(titledBorder);

        JPanel addOrder = new JPanel();
        addOrder.setLayout(null);
        addOrder.setOpaque(false);
        Border border1 = BorderFactory.createLineBorder(new Color(160, 170, 160), 1, true);
        TitledBorder titledBorder1 = BorderFactory.createTitledBorder(border1, "Add Products ", TitledBorder.LEFT,
                TitledBorder.TOP, new Font("Arial", Font.BOLD, 10));
        titledBorder1.setTitleColor(Color.WHITE);
        addOrder.setBorder(titledBorder1);

        JLabel formTitle = new JLabel("Order Information Sheet");
        formTitle.setBounds(540, 20, 400, 35);
        formTitle.setFont(new Font("Arial", Font.PLAIN, 30));
        formTitle.setForeground(new Color(255, 255, 255));
        formTitle.setHorizontalTextPosition(SwingConstants.CENTER);
        pane.add(formTitle);

        JLabel nameLabel = new JLabel("Customer Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        nameLabel.setForeground(new Color(255, 255, 255));
        nameLabel.setBounds(20, 40, 150, 30);
        setCustomer.add(nameLabel);

        nameText = new JTextField();
        nameText.setBounds(170, 40, 150, 30);
        nameText.setFont(new Font("Tahoma", Font.PLAIN, 14));
        setCustomer.add(nameText);

        JLabel emailLabel = new JLabel("Email Address:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        emailLabel.setForeground(new Color(255, 255, 255));
        emailLabel.setBounds(20, 90, 150, 30);
        setCustomer.add(emailLabel);

        emailText = new JTextField();
        emailText.setBounds(170, 90, 150, 30);
        emailText.setFont(new Font("Tahoma", Font.PLAIN, 14));
        setCustomer.add(emailText);

        JLabel paymentLabel = new JLabel("Payment Type:");
        paymentLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        paymentLabel.setForeground(new Color(255, 255, 255));
        paymentLabel.setBounds(20, 140, 150, 30);
        setCustomer.add(paymentLabel);

        String[] categoryChoiceList = {"Cash", "Credit", "Debit"};
        payType = new JComboBox(categoryChoiceList);
        payType.setBounds(170, 140, 150, 30);
        setCustomer.add(payType);

        JLabel productLabel = new JLabel("Product Name");
        productLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        productLabel.setForeground(new Color(255, 255, 255));
        productLabel.setBounds(20, 40, 150, 30);
        addOrder.add(productLabel);

        productText = new JTextField();
        productText.setBounds(170, 40, 150, 30);
        productText.setFont(new Font("Tahoma", Font.PLAIN, 14));
        addOrder.add(productText);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        quantityLabel.setForeground(new Color(255, 255, 255));
        quantityLabel.setBounds(20, 90, 150, 30);
        addOrder.add(quantityLabel);

        quantityText = new JTextField();
        quantityText.setBounds(170, 90, 150, 30);
        quantityText.setFont(new Font("Tahoma", Font.PLAIN, 14));
        addOrder.add(quantityText);

        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        priceLabel.setForeground(new Color(255, 255, 255));
        priceLabel.setBounds(20, 140, 150, 30);
        addOrder.add(priceLabel);

        priceText = new JTextField();
        priceText.setBounds(170, 140, 150, 30);
        priceText.setFont(new Font("Tahoma", Font.PLAIN, 14));
        addOrder.add(priceText);

        addButton = new JButton("Add Product");
        addButton.setBackground(new Color(86, 99, 90));
        addButton.setFont(new Font("Tahoma", 0, 16));
        addButton.addActionListener((ActionEvent evt) -> {
            addButtonActionPerformed(evt);
        });
        addButton.setForeground(new Color(255, 255, 255));
        addButton.setBounds(160, 570, 140, 30);
        pane.add(addButton);

        setCustomerButton = new JButton("Start Order");
        setCustomerButton.setBackground(new Color(86, 99, 90));
        setCustomerButton.setFont(new Font("Tahoma", 0, 16));
        setCustomerButton.addActionListener((ActionEvent evt) -> {
            setCustomerButtonActionPerformed(evt);
        });
        setCustomerButton.setForeground(new Color(255, 255, 255));
        setCustomerButton.setBounds(80, 300, 140, 30);
        pane.add(setCustomerButton);

        //Search button for table
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener((java.awt.event.ActionEvent evt) -> {
            searchButtonActionPerformed(evt);
        });
        searchButton.setBackground(new Color(91, 94, 101));
        searchButton.setForeground(new Color(255, 255, 255));
        searchButton.setFont(new Font("Arial", Font.PLAIN, 16));
        searchButton.setBounds(1000, 65, 100, 30);
        pane.add(searchButton);

        //Search text field for table
        searchText = new JTextField(20);
        //Press enter in text field to press button
        searchText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchButton.doClick();
                }
            }
        });
        //Set search text if focus is lost and remove if focus is gained
        searchText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                searchTextFocusGained(evt);
            }

            @Override
            public void focusLost(FocusEvent evt) {
                searchTextFocusLost(evt);
            }
        });
        searchText.setBounds(425, 65, 550, 30);
        searchText.setFont(new Font("Arial", Font.PLAIN, 14));
        pane.add(searchText);

        //Reset button for table
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener((ActionEvent evt) -> {
            resetButtonActionPerformed(evt);
        });
        resetButton.setBackground(new Color(91, 94, 101));
        resetButton.setForeground(new Color(255, 255, 255));
        resetButton.setFont(new Font("Arial", Font.PLAIN, 16));
        resetButton.setBounds(1120, 65, 100, 30);
        pane.add(resetButton);

        endOrderButton = new JButton("End Order");
        endOrderButton.setBackground(new Color(86, 99, 90));
        endOrderButton.setFont(new Font("Tahoma", 0, 16));
        endOrderButton.addActionListener((ActionEvent evt) -> {
            endOrderButtonActionPerformed(evt);
        });
        endOrderButton.setForeground(new Color(255, 255, 255));
        endOrderButton.setBounds(240, 300, 140, 30);
        pane.add(endOrderButton);
        /*
        JButton modifyButton = new JButton("Modify Order");
        modifyButton.setBackground(new Color(91, 94, 101));
        modifyButton.setFont(new Font("Tahoma", 0, 16));
        modifyButton.setForeground(new Color(255, 255, 255));
        modifyButton.setBounds(425, 640, 140, 30);
        pane.add(modifyButton);
        */
        JButton deleteButton = new JButton("Delete Order");
        deleteButton.setFont(new Font("Tahoma", 0, 16));
        deleteButton.addActionListener((ActionEvent evt) -> {
            deleteButtonActionPerformed(evt);
        });
        deleteButton.setBackground(new Color(50, 46, 46));
        deleteButton.setFont(new Font("Tahoma", 0, 16));
        deleteButton.setForeground(new Color(255, 255, 255));
        deleteButton.setBounds(425, 640, 140, 35);
        pane.add(deleteButton);

        JButton mainButton = new JButton("Main Menu");
        mainButton.setFont(new Font("Tahoma", 0, 16));
        mainButton.setBackground(new Color(50, 46, 46));
        mainButton.setFont(new Font("Tahoma", 0, 16));
        mainButton.addActionListener((ActionEvent evt) -> {
            mainButtonActionPerformed(evt);
        });
        mainButton.setForeground(new Color(255, 255, 255));
        mainButton.setBounds(625, 640, 140, 35);
        pane.add(mainButton);

        setCustomer.setBounds(50, 110, 350, 240);
        addOrder.setBounds(50, 370, 350, 280);
        pane.add(setCustomer);
        pane.add(addOrder);
        
        Object[][] data = {};
        String[] colNames = {"Customer Name", "Customer Email", "Payment", "Order Number", "Product Name", "Quantity", "Sale Price", "Date"};

        DefaultTableModel orderModel = new DefaultTableModel(data, colNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        jTableOrders = new JTable(orderModel);
        jTableOrders.setSelectionBackground(new Color(86, 99, 90));
        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(jTableOrders);
        scroll.getViewport().setBackground(new Color(200, 200, 200));
        scroll.setBounds(425, 110, 800, 500);
        UIManager.put("TableHeader:\"TableHeader.renderer\"[Enabled].backgroundPainter", Product_Inventory_Page.getPainter(new Color(86, 99, 90)));
        UIManager.put("Table.alternateRowColor", new Color(160, 170, 160));
        pane.add(scroll);
        add(pane);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        updateTable();
        addButton.setEnabled(false);
        endOrderButton.setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    private void setCustomerButtonActionPerformed(ActionEvent evt) {
        Connection con = ConnectDatabase.getConnection();
        try {
            if (nameText.getText().equals("")) {
                throw new InvalidInputException("Please enter a customer name before starting order.");
            }
            PreparedStatement st = con.prepareStatement("Select * FROM Customer WHERE name = ?");
            st.setString(1, nameText.getText());
            ResultSet rs = st.executeQuery();
            int customerid = 0;
            String customeremail = "";
            if (rs.next()) {
                if (rs.getString("name").equals(nameText.getText())) {
                    customerid = rs.getInt("customerid");
                    customeremail = rs.getString("email");
                }
            }
            if (customerid > 0) {
                int option = JOptionPane.showConfirmDialog(null, "Current Customer Email: " + customeremail + 
                        "\nWould you like to update customer email?", "Confirm", 0, JOptionPane.PLAIN_MESSAGE);
                if (option == 0) {
                    PreparedStatement stmt = con.prepareStatement("UPDATE Customer SET email = ? WHERE customerid = ?");
                    stmt.setString(1, emailText.getText());
                    stmt.setInt(2, customerid);
                    stmt.executeUpdate();
                    stmt.close();
                    updateTable();
                }
            } else {
                PreparedStatement stmt = con.prepareStatement("INSERT INTO Customer(name, email) " + "VALUES (?, ?)");
                stmt.setString(1, nameText.getText());
                stmt.setString(2, emailText.getText());
                stmt.executeUpdate();
                stmt.close();

                PreparedStatement st1 = con.prepareStatement("SELECT * FROM Customer WHERE name = ?");
                st1.setString(1, nameText.getText());
                ResultSet rs1 = st1.executeQuery();

                if (rs1.next()) {
                    customerid = rs1.getInt("customerid");
                }
                rs1.close();
            }
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            now = LocalDateTime.now();
            PreparedStatement st2 = con.prepareStatement("INSERT INTO OrderInfo(customer, payment, date) " + "VALUES(?, ?, ?)");
            st2.setInt(1, customerid);
            st2.setString(2, (String) payType.getSelectedItem());
            st2.setString(3, dtf.format(now));
            st2.executeUpdate();

            PreparedStatement st3 = con.prepareStatement("SELECT * FROM OrderInfo WHERE customer = ?");
            st3.setInt(1, customerid);
            ResultSet rs1 = st3.executeQuery();
            while (rs1.next()) {
                if (rs1.getString("date").equals(dtf.format(now))) {
                    currentOrder = rs1.getInt("orderid");
                }

            }

            st.close();
            st2.close();
            st3.close();
            rs.close();
            rs1.close();
            nameText.setEditable(false);
            emailText.setEditable(false);
            payType.setEnabled(false);
            setCustomerButton.setEnabled(false);
            addButton.setEnabled(true);
            endOrderButton.setEnabled(true);

        } catch (SQLException | InvalidInputException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Warning", 2);
        }
    }

    private void addButtonActionPerformed(ActionEvent evt) {
        Connection con = ConnectDatabase.getConnection();
        try {
            if (productText.getText().equals("") || quantityText.getText().equals("") || priceText.getText().equals("")) {
                throw new InvalidInputException("Please insert values into all fields before adding product to order.");
            }
            if (setCustomerButton.isEnabled()) {
                throw new InvalidInputException("Please start an order before attempting to add a product");
            }

            PreparedStatement st = con.prepareStatement("SELECT * FROM Products WHERE name = ?");
            st.setString(1, productText.getText());
            ResultSet rs2 = st.executeQuery();
            if (rs2.next()) {
                PreparedStatement stmt = con.prepareStatement("UPDATE Products SET quantity = ?, status = ? WHERE name = ?");
                stmt.setString(3, productText.getText());
                int quantity = rs2.getInt("quantity") - Integer.parseInt(quantityText.getText());
                if (quantity == 0) {
                    stmt.setInt(1, quantity);
                    stmt.setString(2, "OUT OF STOCK");
                    stmt.executeUpdate();
                } else if (quantity < 0) {
                    throw new InvalidInputException("There is not enough product in inventory to fufill order.");
                } else {
                    stmt.setInt(1, quantity);
                    stmt.setString(2, "IN STORE");
                    stmt.executeUpdate();
                }
                JOptionPane.showMessageDialog(null, productText.getText()
                        + " has been added to order number " + currentOrder + ".", "Product Added!", 1);
                stmt.close();
            } else {
                throw new InvalidInputException("Product is not in inventory!");
            }
            rs2.close();
            String price = priceText.getText();
            if (price.contains(",")) {
                price = price.replaceAll(",", "");
            }
            double finalPrice = Double.parseDouble(price);
            int finalQuantity = Integer.parseInt(quantityText.getText());
            if (finalPrice < 0 || finalQuantity < 0) {
                throw new InvalidInputException("Number cannot be less than zero.");
            }
            PreparedStatement st3 = con.prepareStatement("INSERT INTO ProductSold(productname, price, quantity, ordernumber) " + "VALUES(?, ?, ?, ?)");
            st3.setString(1, productText.getText());
            st3.setDouble(2, finalPrice);
            st3.setInt(3, finalQuantity);
            st3.setInt(4, currentOrder);
            st3.executeUpdate();
            st3.close();

            productText.setText("");
            quantityText.setText("");
            priceText.setText("");

        } catch (NullPointerException | SQLException | NumberFormatException | InvalidInputException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Warning", 2);
        } finally {
            try {
                con.close();
                updateTable();
            } catch (SQLException e) {
            }
        }

    }

    private void endOrderButtonActionPerformed(ActionEvent evt) {
        Connection con = ConnectDatabase.getConnection();
        try {
            if (setCustomerButton.isEnabled()) {
                throw new InvalidInputException("Start an order before ending the order!");
            }
            payType.setSelectedIndex(0);
            nameText.setEditable(true);
            emailText.setEditable(true);
            payType.setEnabled(true);
            setCustomerButton.setEnabled(true);
            PreparedStatement st = con.prepareStatement("SELECT ordernumber, name, productname, quantity, price, date FROM ProductSold INNER JOIN OrderInfo ON ordernumber=orderid INNER JOIN Customer ON customer=customerid");
            ResultSet rs = st.executeQuery();
            double totalPrice = 0;
            while (rs.next()) {
                if (rs.getInt("ordernumber") == currentOrder) {
                    int quantity = rs.getInt("quantity");
                    double price = rs.getDouble("price");
                    totalPrice += quantity * price;
                }
            }
            st.close();
            rs.close();
            if (totalPrice == 0) {
                PreparedStatement st1 = con.prepareStatement("DELETE FROM OrderInfo WHERE orderid = ?");
                st1.setInt(1, currentOrder);
                st1.executeUpdate();
                st1.close();
                throw new InvalidInputException("No products were added to order");
            }
            DecimalFormat df = new DecimalFormat("###,###.##");
            JOptionPane.showMessageDialog(null, "Total Order Amount: $" + df.format(totalPrice), "Order " + currentOrder, JOptionPane.PLAIN_MESSAGE);
        } catch (InvalidInputException | SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Warning", 2);
        } finally {
            try {
                con.close();
                updateTable();
            } catch (SQLException e) {
            }
        }
        nameText.setText("");
        emailText.setText("");
        addButton.setEnabled(false);
        endOrderButton.setEnabled(false);
    }

    private void mainButtonActionPerformed(ActionEvent evt) {
        this.dispose();
        Main_Page main = new Main_Page();
        main.setLocationRelativeTo(null);
        main.setVisible(true);
    }

    private static void updateTable() {
        Connection con = ConnectDatabase.getConnection();
        try {
            ((DefaultTableModel) jTableOrders.getModel()).setRowCount(0);
            PreparedStatement st = con.prepareStatement("SELECT name, email, payment, ordernumber, productname, quantity, price, date FROM ProductSold INNER JOIN OrderInfo ON ordernumber=orderid INNER JOIN Customer ON customer=customerid");
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String payment = rs.getString("payment");
                int orderNumber = rs.getInt("ordernumber");
                String productName = rs.getString("productname");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                String date = rs.getString("date");

                Object[] newData = {name, email, payment, orderNumber, productName, quantity, price, date};
                ((DefaultTableModel) jTableOrders.getModel()).addRow(newData);
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("Couldn't connect to SQL server.");
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
            }
        }

    }

    private void deleteButtonActionPerformed(ActionEvent evt) {
        Connection con = ConnectDatabase.getConnection();
        int selectedRows[] = jTableOrders.getSelectedRows();
        int option = -1;
        try {
            if (selectedRows.length == 0) {
                throw new InvalidInputException("A table row is not selected.");
            }
            option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete selected order?", "Confirm", 1);
        } catch (InvalidInputException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", 1);
        }
        Object[] selectedOrder = new Object[selectedRows.length];
        for (int i = 0; i < selectedRows.length; i++) {
            if (this.tr == null) {
                selectedOrder[i] = ((DefaultTableModel) jTableOrders.getModel()).getValueAt(selectedRows[i], 3);
            } else {
                selectedOrder[i] = ((DefaultTableModel) jTableOrders.getModel()).getValueAt(tr.convertRowIndexToModel(selectedRows[i]), 3);
            }
        }
        if (option == 0) {
            int count = 0;
            for (Object select : selectedOrder) {
                try {
                    PreparedStatement st = con.prepareStatement("SELECT * FROM ProductSold WHERE ordernumber = ?");
                    st.setInt(1, Integer.parseInt(select.toString()));
                    ResultSet rs = st.executeQuery();
                    while (rs.next()) {
                        PreparedStatement stmt1 = con.prepareStatement("SELECT * FROM Products WHERE name = ?");
                        stmt1.setString(1, rs.getString("productname"));
                        ResultSet rs2 = stmt1.executeQuery();
                        if (rs2.next()) {
                            PreparedStatement stmt = con.prepareStatement("UPDATE Products SET quantity = ?, status = ? WHERE name = ?");
                            stmt.setString(3, rs.getString("productname"));
                            int quantity = rs.getInt("quantity") + rs2.getInt("quantity");
                            stmt.setInt(1, quantity);
                            stmt.setString(2, "IN STORE");
                            stmt.executeUpdate();
                            stmt.close();
                            stmt1.close();
                            rs2.close();
                        }
                    }
                    rs.close();
                    PreparedStatement st2 = con.prepareStatement("DELETE FROM ProductSold WHERE ordernumber = ?");
                    st2.setInt(1, Integer.parseInt(select.toString()));
                    st2.executeUpdate();
                    st2.close();
                    PreparedStatement st1 = con.prepareStatement("DELETE FROM OrderInfo WHERE orderid = ?");
                    st1.setInt(1, Integer.parseInt(select.toString()));
                    st1.executeUpdate();
                    st1.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error", 1);
                }
                count++;
            }
        }
        try {
            con.close();
            updateTable();
        } catch (SQLException e) {
        }
    }

    //Deletes gray text 'search' from the search bar when focus is gained on search bar.
    private void searchTextFocusGained(java.awt.event.FocusEvent evt) {
        if (searchText.getText().equals("Search")) {
            searchText.setText("");
            searchText.setForeground(Color.BLACK);
        }
    }

    //Sets a gray text 'search' when focus is lost on search bar
    private void searchTextFocusLost(java.awt.event.FocusEvent evt) {
        if (searchText.getText().isEmpty()) {
            searchText.setForeground(Color.GRAY);
            searchText.setText("Search");
        }
    }

    public void filter(String query) {
        tr = new TableRowSorter<>((DefaultTableModel) jTableOrders.getModel());
        jTableOrders.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter("(?i)" + query));
    }

    //Used to filter the jtable of values when button is pressed
    private void searchButtonActionPerformed(ActionEvent evt) {
        String query = "";
        if (!searchText.getText().equals("Search")) {
            query = searchText.getText();
        }
        filter(query);
    }

    //Used to reset the table to default view when button is pressed
    private void resetButtonActionPerformed(ActionEvent evt) {
        String query = "";
        searchText.setText("Search");
        searchText.setForeground(Color.GRAY);
        filter(query);
    }
}
