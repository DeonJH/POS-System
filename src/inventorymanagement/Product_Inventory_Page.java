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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class Product_Inventory_Page extends JFrame implements ActionListener {

    private JPanel errorPanel = new JPanel();
    private int option;
    private JTextField modifyName = new JTextField(10);
    private JTextField modifyQuantity = new JTextField(10);
    private JTextField modifyPrice = new JTextField(10);
    private JTextArea modifyDescription = new JTextArea(5, 20);
    private JTextField addName = new JTextField(10);
    private JTextField addQuantity = new JTextField(10);
    private JTextField addPrice = new JTextField(10);
    private JTextArea addDescription = new JTextArea(5, 20);
    private JScrollPane addScroll = new JScrollPane(addDescription);
    private JScrollPane modifyScroll = new JScrollPane(modifyDescription);
    private TableRowSorter<DefaultTableModel> tr;
    private static JTable jTableProducts;
    private final JTextField searchText;
    private Font labelFont = new Font("Arial", Font.PLAIN, 14);
    private JButton addButton;

    Product_Inventory_Page() {
        super("Product Inventory");

        //Layout and size for the frame
        setLayout(new GridLayout(1, 1));
        setSize(1250, 660);

        //Main panel for the product page
        JPanel pane = new JPanel();
        pane.setBackground(new Color(147, 134, 134));
        pane.setLayout(null);

        //Add product panel with border
        JPanel addProduct = new JPanel();
        addProduct.setLayout(null);
        addProduct.setOpaque(false);
        Border border = BorderFactory.createLineBorder(new Color(160, 170, 160), 1, true);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(border, "Add Product ", TitledBorder.LEFT,
                TitledBorder.TOP, new Font("Arial", Font.BOLD, 10));
        titledBorder.setTitleColor(Color.WHITE);
        addProduct.setBorder(titledBorder);

        //Product name label
        JLabel nameLabel = new JLabel("Enter Name: ");
        nameLabel.setBounds(15, 40, 120, 30);
        nameLabel.setFont(labelFont);
        nameLabel.setForeground(Color.WHITE);
        addProduct.add(nameLabel);

        //Product name textfield
        addName.setBounds(140, 45, 160, 30);
        addProduct.add(addName);
        addName.requestFocus();
        //Quantity label
        JLabel quantityLabel = new JLabel("Enter Quantity: ");
        quantityLabel.setFont(labelFont);
        quantityLabel.setForeground(Color.WHITE);
        quantityLabel.setBounds(15, 110, 120, 30);
        addProduct.add(quantityLabel);

        //Quantity textfield
        addQuantity.setBounds(140, 115, 160, 30);
        addProduct.add(addQuantity);

        //Description label
        JLabel descriptionLabel = new JLabel("Enter Description: ");
        descriptionLabel.setFont(labelFont);
        descriptionLabel.setForeground(Color.WHITE);
        descriptionLabel.setBounds(15, 180, 120, 30);
        addProduct.add(descriptionLabel);

        //Description scrollpane
        addDescription.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERS‌​AL_KEYS, null);
        addDescription.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERS‌​AL_KEYS, null);
        addScroll.setBounds(140, 185, 160, 90);
        addProduct.add(addScroll);

        //Price label
        JLabel priceLabel = new JLabel("Enter Price: ");
        priceLabel.setBounds(15, 300, 120, 30);
        priceLabel.setFont(labelFont);
        priceLabel.setForeground(Color.WHITE);
        addProduct.add(priceLabel);

        //Price textfield
        addPrice.setBounds(140, 305, 160, 30);
        addProduct.add(addPrice);
        addPrice.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    addButton.doClick();
                } else if (e.getKeyCode() == KeyEvent.VK_TAB) {
                    addName.isFocusable();
                }
            }
        });

        //Add Panel to primary panel
        addProduct.setBounds(60, 110, 320, 420);
        pane.add(addProduct);

        //Page title
        JLabel formTitle = new JLabel("Product Inventory");
        formTitle.setBounds(90, 40, 300, 35);
        formTitle.setFont(new Font("Arial", Font.BOLD, 30));
        formTitle.setForeground(Color.WHITE);
        formTitle.setHorizontalTextPosition(SwingConstants.CENTER);
        pane.add(formTitle);

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

        //Add button for database/table
        addButton = new JButton("Add");
        addButton.addActionListener((java.awt.event.ActionEvent evt) -> {
            addButtonActionPerformed(evt);
        });
        addButton.setBackground(new Color(86, 99, 90));
        addButton.setFont(new Font("Arial", 0, 16));
        addButton.setForeground(new Color(255, 255, 255));
        addButton.setBounds(130, 470, 180, 30);
        addDescription.setLineWrap(true);
        addDescription.setWrapStyleWord(true);
        pane.add(addButton);

        //Modify button for database/table
        JButton modifyButton = new JButton("Modify");
        modifyButton.addActionListener((java.awt.event.ActionEvent evt) -> {
            modifyButtonActionPerformed(evt);
        });
        modifyDescription.setLineWrap(true);
        modifyDescription.setWrapStyleWord(true);
        modifyButton.setBackground(new Color(91, 94, 101));
        modifyButton.setFont(new Font("Arial", 0, 16));
        modifyButton.setForeground(new Color(255, 255, 255));
        modifyButton.setBounds(425, 560, 180, 30);
        pane.add(modifyButton);

        //Delete button for database/table
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener((java.awt.event.ActionEvent evt) -> {
            deleteButtonActionPerformed(evt);
        });
        deleteButton.setBackground(new Color(50, 46, 46));
        deleteButton.setFont(new Font("Tahoma", 0, 16));
        deleteButton.setForeground(new Color(255, 255, 255));
        deleteButton.setBounds(675, 560, 180, 30);
        pane.add(deleteButton);

        //Return to the main menu button
        JButton mainMenuButton = new JButton("Main Menu");
        mainMenuButton.addActionListener((java.awt.event.ActionEvent evt) -> {
            mainMenuButtonActionPerformed(evt);
        });
        mainMenuButton.setBackground(new Color(50, 46, 46));
        mainMenuButton.setFont(new Font("Tahoma", 0, 16));
        mainMenuButton.setForeground(new Color(255, 255, 255));
        mainMenuButton.setBounds(925, 560, 180, 30);
        pane.add(mainMenuButton);

        //Create table off of database values
        Object[][] data = {};
        String[] colNames = {"Name", "Quantity", "Price", "Description", "Status"};
        DefaultTableModel productModel = new DefaultTableModel(data, colNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        jTableProducts = new JTable(productModel);
        jTableProducts.setSelectionBackground(new Color(86, 99, 90));
        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(jTableProducts);
        scroll.getViewport().setBackground(new Color(200, 200, 200));
        scroll.setBounds(425, 110, 800, 420);
        UIManager.put("TableHeader:\"TableHeader.renderer\"[Enabled].backgroundPainter", getPainter(new Color(86, 99, 90)));
        UIManager.put("Table.alternateRowColor", new Color(160, 170, 160));
        pane.add(scroll);
        add(pane);
        //Updates the table with values when initialized
        updateTable();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    //Filters the table based on string from search bar
    public void filter(String query) {
        tr = new TableRowSorter<>((DefaultTableModel) jTableProducts.getModel());
        jTableProducts.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter("(?i)" + query));
    }

    //Updates the table when called with latest information from database
    public static void updateTable() {
        Connection con = ConnectDatabase.getConnection();
        try {
            ((DefaultTableModel) jTableProducts.getModel()).setRowCount(0);
            String sql = "select * from Products";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String dbname = rs.getString("name");
                int dbquantity = rs.getInt("quantity");
                double dbprice = rs.getDouble("price");
                String dbdescription = rs.getString("description");
                String dbstatus = rs.getString("status");
                Object[] newData = {dbname, dbquantity, dbprice, dbdescription, dbstatus};
                ((DefaultTableModel) jTableProducts.getModel()).addRow(newData);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Couldn't connect to SQL server.");
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
            }
        }
    }

    //Clears the text from the add text fields
    public void clearForm() {
        addName.setText("");
        addQuantity.setText("");
        addDescription.setText("");
        addPrice.setText("");
    }

    //This is used to get the painter object from the nimbus look and feel table column to change the color.
    public static Object getPainter(Color color) {
        NimbusLookAndFeel nimbusTmp = new NimbusLookAndFeel();
        Object nimbusBlueGreyOrg = UIManager.get("nimbusBlueGrey");
        UIManager.put("nimbusBlueGrey", color);
        try {
            UIManager.setLookAndFeel(nimbusTmp);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Product_Inventory_Page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        Object painter = UIManager.get("TableHeader:\"TableHeader.renderer\"[Enabled].backgroundPainter");

        UIManager.put("nimbusBlueGrey", nimbusBlueGreyOrg);
        UIManager.getLookAndFeel().uninitialize();
        return painter;
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

    //Adds new products to the database and jtable when user enters information and clicks button
    private void addButtonActionPerformed(ActionEvent evt) {
        Connection con = ConnectDatabase.getConnection();
        try {

            if (addName.getText().equals("")) {
                throw new InvalidInputException("Product field must not be empty.");
            } else if (addQuantity.getText().equals("")) {
                throw new InvalidInputException("Quantity field must not be empty.");
            } else if (addPrice.getText().equals("")) {
                throw new InvalidInputException("Price field must not be empty.");
            }
            String price = addPrice.getText();
            if (addPrice.getText().contains(",")) {
                price = addPrice.getText().replaceAll(",", "");
            }
            int quantity = Integer.parseInt(addQuantity.getText());
            double finalPrice = Double.parseDouble(price);
            if (finalPrice < 0) {
                throw new InvalidInputException("Can't have a number less than zero.");
            }
            String status = "IN STORE";
            if (quantity < 0) {
                throw new InvalidInputException("Can't have a number less than zero.");
            } else if (quantity == 0) {
                status = "OUT OF STOCK";
            }
            PreparedStatement stmt = con.prepareStatement("INSERT INTO Products(name, quantity, status, price, description) " + "VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, addName.getText());
            stmt.setInt(2, quantity);
            stmt.setString(3, status);
            stmt.setDouble(4, finalPrice);
            stmt.setString(5, addDescription.getText());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(errorPanel, addName.getText() + " has been added.", "Success", 1);
            stmt.close();
            clearForm();
        } catch (InvalidInputException | SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(errorPanel, e.getMessage(), "Warning", 2);
        } finally {
            try {
                con.close();
                updateTable();
            } catch (SQLException e) {
            }
        }
    }

    //Deletes values from jtable and database when item/items are selected on jtable and button is pressed
    private void deleteButtonActionPerformed(ActionEvent evt) {
        Connection con = ConnectDatabase.getConnection();
        int selectedRows[] = jTableProducts.getSelectedRows();
        try {
            if (selectedRows.length == 0) {
                throw new InvalidInputException("A table row is not selected.");
            }
            option = JOptionPane.showConfirmDialog(errorPanel, "Are you sure you want to delete " + selectedRows.length + " rows?", "Confirm", 1);
        } catch (InvalidInputException e) {
            JOptionPane.showMessageDialog(errorPanel, e.getMessage(), "Error", 1);
        }
        Object[] selected = new Object[selectedRows.length];
        for (int i = 0; i < selectedRows.length; i++) {
            if (this.tr == null) {
                selected[i] = ((DefaultTableModel) jTableProducts.getModel()).getValueAt(selectedRows[i], 0);
            } else {
                selected[i] = ((DefaultTableModel) jTableProducts.getModel()).getValueAt(tr.convertRowIndexToModel(selectedRows[i]), 0);
            }
        }
        if (option == 0) {
            for (Object select : selected) {
                try {
                    PreparedStatement st = con.prepareStatement("DELETE FROM Products WHERE name = ?");
                    st.setString(1, select.toString());
                    st.executeUpdate();

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(errorPanel, e.getMessage(), "Error", 1);
                }
            }
        }
        try {
            con.close();
            updateTable();
        } catch (SQLException e) {
        }
    }

    //Modifies item on jtable when item is selected and button is pressed.
    private void modifyButtonActionPerformed(java.awt.event.ActionEvent evt) {
        Object[] modifyOptions = {
            "Product name: ", modifyName,
            "Quantity: ", modifyQuantity,
            "Description: ", modifyScroll,
            "Price: ", modifyPrice
        };
        Connection con = ConnectDatabase.getConnection();
        int selectedRow = jTableProducts.getSelectedRow();

        try {
            if (selectedRow == -1) {
                throw new InvalidInputException("A row on the table is not selected to modify.");
            }
            Object selected;
            if (this.tr == null) {
                selected = ((DefaultTableModel) jTableProducts.getModel()).getValueAt(selectedRow, 0);
            } else {
                selected = ((DefaultTableModel) jTableProducts.getModel()).getValueAt(tr.convertRowIndexToModel(selectedRow), 0);
            }
            PreparedStatement st = con.prepareStatement("SELECT * FROM Products WHERE name = ?");
            st.setString(1, selected.toString());
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                if (rs.getString("name").equals(selected.toString())) {
                    modifyName.setText(rs.getString("name"));
                    modifyQuantity.setText(rs.getString("quantity"));
                    modifyPrice.setText(rs.getString("price"));
                    modifyDescription.setText(rs.getString("description"));
                    String[] options = {"Modify", "Cancel"};
                    option = JOptionPane.showOptionDialog(errorPanel, modifyOptions,
                            "Modify Product", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                    if (option == 0) {
                        String price = modifyPrice.getText();
                        if (modifyName.getText().equals("") | modifyQuantity.getText().equals("")
                                | modifyPrice.getText().equals("") | modifyDescription.getText().equals("")) {
                            throw new InvalidInputException("All fields must have a value before modifying.");
                        }
                        if (modifyPrice.getText().contains(",")) {
                            price = modifyPrice.getText().replaceAll(",", "");
                        }
                        double finalPrice = Double.parseDouble(price);
                        int quantity = Integer.parseInt(modifyQuantity.getText());
                        String status;
                        if (finalPrice < 0) {
                            throw new InvalidInputException("Can't have a number less than zero.");
                        }
                        if (quantity > 0) {
                            status = "IN STORE";
                        } else if (quantity < 0) {
                            throw new InvalidInputException("Can't have a number less than zero.");
                        } else {
                            status = "OUT OF STOCK";
                        }
                        PreparedStatement stmt = con.prepareStatement("UPDATE Products SET name = ?, quantity = ?, description = ?, price = ?, status = ? WHERE name = ?");
                        stmt.setString(1, modifyName.getText());
                        stmt.setInt(2, quantity);
                        stmt.setString(3, modifyDescription.getText());
                        stmt.setDouble(4, finalPrice);
                        stmt.setString(5, status);
                        stmt.setString(6, rs.getString("name"));
                        stmt.executeUpdate();
                    }
                }
            }
        } catch (InvalidInputException | SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(errorPanel, e.getMessage(), "Error", 1);
        } finally {
            try {
                con.close();
                updateTable();
            } catch (SQLException e) {
            }
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

    //When main menu button is pressed user is returned to the main menu of application.
    private void mainMenuButtonActionPerformed(ActionEvent evt) {
        this.dispose();
        Main_Page main = new Main_Page();
        main.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_TAB) {
            System.out.println(e.getModifiersEx());
            if (e.getModifiersEx() > 0) {
                addDescription.transferFocusBackward();
            } else {
                addDescription.transferFocus();
            }
            e.consume();
        }
    }

}
