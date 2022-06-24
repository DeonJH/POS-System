package inventorymanagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
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
import org.mindrot.jbcrypt.BCrypt;

public class Employee_Info_Page extends JFrame implements ActionListener {

    private JPanel errorPanel = new JPanel();
    private int option;
    private JTextField modifyFirstName = new JTextField(10);
    private JTextField modifyLastName = new JTextField(10);
    private JTextField modifyMiddleInitial = new JTextField(10);
    private JTextField modifySalary = new JTextField(10);
    private JTextField modifyJobTitle = new JTextField(10);
    private JTextField modifyEmail = new JTextField(10);
    private JTextField addFirstName = new JTextField(10);
    private JTextField addLastName = new JTextField(10);
    private JTextField addMiddleInitial = new JTextField(10);
    private JTextField addSalary = new JTextField(10);
    private JTextField addJobTitle = new JTextField(10);
    private JTextField addEmail = new JTextField(10);
    private JTextField createUsername = new JTextField(10);
    private JPasswordField createPassword = new JPasswordField(10);
    private JPasswordField verifyPassword = new JPasswordField(10);
    private TableRowSorter<DefaultTableModel> tr;
    private static JTable jTableEmployees;
    private final JTextField searchText;
    private Font labelFont = new Font("Arial", Font.PLAIN, 14);
    private JButton addButton;
    private String roles[] = {"Employee", "Manager"};
    private JComboBox<String> modifyRole = new JComboBox(roles);
    private JComboBox<String> jComboBox1 = new JComboBox(roles);

    Employee_Info_Page() {
        super("Employee Management");
        //Creates a better look and feel for the java application using Numbus
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login_Page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //Layout and size for the frame
        setLayout(new GridLayout(1, 1));
        setSize(1250, 660);
        getRootPane().setDefaultButton(addButton);

        //Main panel for the employee page
        JPanel pane = new JPanel();
        pane.setBackground(new Color(147, 134, 134));
        pane.setLayout(null);

        //Add employee panel with border
        JPanel addEmployee = new JPanel();
        addEmployee.setLayout(null);
        addEmployee.setOpaque(false);
        Border border = BorderFactory.createLineBorder(new Color(160, 170, 160), 1, true);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(border, "Add Employee", TitledBorder.LEFT,
                TitledBorder.TOP, new Font("Arial", Font.BOLD, 10));
        titledBorder.setTitleColor(Color.WHITE);
        addEmployee.setBorder(titledBorder);

        //Employee first name label
        JLabel firstNameLabel = new JLabel("First Name: ");
        firstNameLabel.setBounds(15, 40, 120, 30);
        firstNameLabel.setFont(labelFont);
        firstNameLabel.setForeground(Color.WHITE);
        addEmployee.add(firstNameLabel);

        //Employee first name textfield
        addFirstName.setBounds(140, 45, 160, 30);
        addEmployee.add(addFirstName);

        //Employee last name label
        JLabel lastNameLabel = new JLabel("Last Name: ");
        lastNameLabel.setBounds(15, 80, 120, 30);
        lastNameLabel.setFont(labelFont);
        lastNameLabel.setForeground(Color.WHITE);
        addEmployee.add(lastNameLabel);

        //Employee last name textfield
        addLastName.setBounds(140, 85, 160, 30);
        addEmployee.add(addLastName);

        //Employee Middle Initial label
        JLabel middleInitialLabel = new JLabel("Middle Initial: ");
        middleInitialLabel.setBounds(15, 120, 120, 30);
        middleInitialLabel.setFont(labelFont);
        middleInitialLabel.setForeground(Color.WHITE);
        addEmployee.add(middleInitialLabel);

        //Employee Middle Initial textfield
        addMiddleInitial.setBounds(140, 125, 160, 30);
        addEmployee.add(addMiddleInitial);

        //Salary label
        JLabel salaryLabel = new JLabel("Salary: ");
        salaryLabel.setBounds(15, 160, 120, 30);
        salaryLabel.setFont(labelFont);
        salaryLabel.setForeground(Color.WHITE);
        addEmployee.add(salaryLabel);

        //Salary textfield
        addSalary.setBounds(140, 165, 160, 30);
        addEmployee.add(addSalary);

        //Job Title label
        JLabel jobTitleLabel = new JLabel("Job Title: ");
        jobTitleLabel.setBounds(15, 200, 120, 30);
        jobTitleLabel.setFont(labelFont);
        jobTitleLabel.setForeground(Color.WHITE);
        addEmployee.add(jobTitleLabel);

        //Job title textfield
        addJobTitle.setBounds(140, 205, 160, 30);
        addEmployee.add(addJobTitle);

        //Employee Email
        JLabel emailLabel = new JLabel("Email: ");
        emailLabel.setBounds(15, 240, 120, 30);
        emailLabel.setFont(labelFont);
        emailLabel.setForeground(Color.WHITE);
        addEmployee.add(emailLabel);

        addEmail.setBounds(140, 245, 160, 30);
        addEmployee.add(addEmail);

        //Role Label
        JLabel roleLabel = new JLabel("Role: ");
        roleLabel.setBounds(15, 280, 120, 30);
        roleLabel.setFont(labelFont);
        roleLabel.setForeground(Color.WHITE);
        addEmployee.add(roleLabel);

        jComboBox1.setBounds(140, 285, 160, 30);
        addEmployee.add(jComboBox1);

        //Add Panel to primary panel
        addEmployee.setBounds(60, 110, 320, 420);
        pane.add(addEmployee);

        //Page title
        JLabel formTitle = new JLabel("Employee Management");
        formTitle.setBounds(50, 40, 350, 35);
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

        //searchButton.setOpaque(true);
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
        searchText.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                searchTextFocusGained(evt);
            }

            public void focusLost(FocusEvent evt) {
                searchTextFocusLost(evt);
            }
        });
        searchText.setBounds(425, 65, 550, 30);
        searchText.setFont(new Font("Arial", Font.PLAIN, 14));
        pane.add(searchText);

        //Add button for database/table
        addButton = new JButton("Add Employee");
        addButton.addActionListener((java.awt.event.ActionEvent evt) -> {
            addButtonActionPerformed(evt);
        });
        addButton.setBackground(new Color(86, 99, 90));
        addButton.setFont(new Font("Arial", 0, 16));
        addButton.setForeground(new Color(255, 255, 255));
        addButton.setBounds(130, 460, 180, 30);
        pane.add(addButton);

        //Modify button for database/table
        JButton modifyButton = new JButton("Modify Employee");
        modifyButton.addActionListener((java.awt.event.ActionEvent evt) -> {
            modifyButtonActionPerformed(evt);
        });
        modifyButton.setBackground(new Color(91, 94, 101));
        modifyButton.setFont(new Font("Arial", 0, 16));
        modifyButton.setForeground(new Color(255, 255, 255));
        modifyButton.setBounds(60, 560, 180, 30);
        pane.add(modifyButton);

        //Delete button for database/table
        JButton deleteButton = new JButton("Remove Employee");
        deleteButton.addActionListener((java.awt.event.ActionEvent evt) -> {
            deleteButtonActionPerformed(evt);
        });
        deleteButton.setBackground(new Color(50, 46, 46));
        deleteButton.setFont(new Font("Tahoma", 0, 16));
        deleteButton.setForeground(new Color(255, 255, 255));
        deleteButton.setBounds(305, 560, 180, 30);
        pane.add(deleteButton);

        //Register button for database/table
        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.addActionListener((java.awt.event.ActionEvent evt) -> {
            createAccountButtonActionPerformed(evt);
        });
        createAccountButton.setBackground(new Color(50, 46, 46));
        createAccountButton.setFont(new Font("Tahoma", 0, 16));
        createAccountButton.setForeground(new Color(255, 255, 255));
        createAccountButton.setBounds(550, 560, 180, 30);
        pane.add(createAccountButton);

        JButton deleteAccountButton = new JButton("Delete Account");
        deleteAccountButton.addActionListener((java.awt.event.ActionEvent evt) -> {
            deleteAccountButtonActionPerformed(evt);
        });
        deleteAccountButton.setBackground(new Color(50, 46, 46));
        deleteAccountButton.setFont(new Font("Tahoma", 0, 16));
        deleteAccountButton.setForeground(new Color(255, 255, 255));
        deleteAccountButton.setBounds(795, 560, 180, 30);
        pane.add(deleteAccountButton);

        //Return to the main menu button
        JButton mainMenuButton = new JButton("Main Menu");
        mainMenuButton.addActionListener((java.awt.event.ActionEvent evt) -> {
            mainMenuButtonActionPerformed(evt);
        });
        mainMenuButton.setBackground(new Color(50, 46, 46));
        mainMenuButton.setFont(new Font("Tahoma", 0, 16));
        mainMenuButton.setForeground(new Color(255, 255, 255));
        mainMenuButton.setBounds(1040, 560, 180, 30);
        pane.add(mainMenuButton);

        //Create table off of database values
        Object[][] data = {};
        String[] colNames = {"ID", "First Name", "Last Name", "Middle Initial", "Salary", "Job Title", "Email", "Role"};
        DefaultTableModel employeeModel = new DefaultTableModel(data, colNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        jTableEmployees = new JTable(employeeModel);
        jTableEmployees.setSelectionBackground(new Color(86, 99, 90));
        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(jTableEmployees);
        scroll.getViewport().setBackground(new Color(200, 200, 200));
        scroll.setBounds(425, 110, 800, 420);
        UIManager.put("TableHeader:\"TableHeader.renderer\"[Enabled].backgroundPainter", getPainter(new Color(86, 99, 90)));
        UIManager.put("Table.alternateRowColor", new Color(160, 170, 160));
        pane.add(scroll);
        add(pane);
        //Updates the table with values when initialized
        updateTable();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    //Filters the table based on string from search bar
    public void filter(String query) {
        tr = new TableRowSorter<>((DefaultTableModel) jTableEmployees.getModel());
        jTableEmployees.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter("(?i)" + query));
    }

    //Updates the table when called with latest information from database
    public static void updateTable() {
        Connection con = ConnectDatabase.getConnection();
        try {
            ((DefaultTableModel) jTableEmployees.getModel()).setRowCount(0);
            String sql = "select * from Employees";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                int dbid = rs.getInt("id");
                String dbfirstname = rs.getString("firstname");
                String dblastname = rs.getString("lastname");
                String dbmiddleinitial = rs.getString("middleinitial");
                double dbSalary = rs.getDouble("salary");
                String dbjobtitle = rs.getString("jobtitle");
                String dbemail = rs.getString("email");
                String dbrole = rs.getString("role");

                Object[] newData = {dbid, dbfirstname, dblastname, dbmiddleinitial, dbSalary, dbjobtitle, dbemail, dbrole};
                ((DefaultTableModel) jTableEmployees.getModel()).addRow(newData);
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
        addFirstName.setText("");
        addLastName.setText("");
        addMiddleInitial.setText("");
        addSalary.setText("");
        addJobTitle.setText("");
        createPassword.setText("");
        verifyPassword.setText("");
        createUsername.setText("");
        addEmail.setText("");
        jComboBox1.setSelectedIndex(0);
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

            if (addFirstName.getText().equals("")) {
                throw new NullPointerException("First Name must not be empty.");
            }
            String salary = addSalary.getText();
            if (addSalary.getText().contains(",")) {
                salary = addSalary.getText().replaceAll(",", "");
            }
            double finalSalary = Double.parseDouble(salary);
            if (finalSalary < 0) {
                throw new InvalidInputException("Salary cannot be less than zero");
            }
            PreparedStatement stmt = con.prepareStatement("INSERT INTO Employees(firstname, lastname, middleinitial, salary, jobtitle, email, role) " + "VALUES (?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, addFirstName.getText());
            stmt.setString(2, addLastName.getText());
            stmt.setString(3, addMiddleInitial.getText());
            stmt.setDouble(4, finalSalary);
            stmt.setString(5, addJobTitle.getText());
            stmt.setString(6, addEmail.getText());
            stmt.setString(7, roles[jComboBox1.getSelectedIndex()]);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(errorPanel, addFirstName.getText() + " has been added.", "Success", 1);
            stmt.close();

        } catch (NullPointerException | SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(errorPanel, e.getMessage(), "Warning", 2);
        } finally {
            try {
                con.close();
                clearForm();
                updateTable();
            } catch (SQLException e) {
            }
        }
    }

    //Deletes values from jtable and database when item/items are selected on jtable and button is pressed
    private void deleteButtonActionPerformed(ActionEvent evt) {
        Connection con = ConnectDatabase.getConnection();
        int selectedRows[] = jTableEmployees.getSelectedRows();
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
                selected[i] = ((DefaultTableModel) jTableEmployees.getModel()).getValueAt(selectedRows[i], 0);
            } else {
                selected[i] = ((DefaultTableModel) jTableEmployees.getModel()).getValueAt(tr.convertRowIndexToModel(selectedRows[i]), 0);
            }
        }
        if (option == 0) {
            for (Object select : selected) {
                try {
                    PreparedStatement st1 = con.prepareStatement("SELECT * FROM Employees WHERE id = ?");
                    st1.setString(1, select.toString());
                    ResultSet rs = st1.executeQuery();
                    if (rs.next()) {
                        PreparedStatement st2 = con.prepareStatement("DELETE FROM LoginInfo WHERE firstname = ? AND lastname = ? AND email = ?");
                        st2.setString(1, rs.getString("firstname"));
                        st2.setString(2, rs.getString("lastname"));
                        st2.setString(3, rs.getString("email"));
                        st2.executeUpdate();
                        st2.close();
                        PreparedStatement st = con.prepareStatement("DELETE FROM Employees WHERE id = ?");
                        st.setString(1, select.toString());
                        st.executeUpdate();
                        st.close();
                    }

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
            "First name: ", modifyFirstName,
            "Last name: ", modifyLastName,
            "Middle Initial: ", modifyMiddleInitial,
            "Salary: ", modifySalary,
            "Job Title: ", modifyJobTitle,
            "Email: ", modifyEmail,
            "Role: ", modifyRole
        };
        Connection con = ConnectDatabase.getConnection();
        int selectedRow = jTableEmployees.getSelectedRow();

        try {
            if (selectedRow == -1) {
                throw new InvalidInputException("A row on the table is not selected to modify.");
            }
            Object selected;
            if (this.tr == null) {
                selected = ((DefaultTableModel) jTableEmployees.getModel()).getValueAt(selectedRow, 0);
            } else {
                selected = ((DefaultTableModel) jTableEmployees.getModel()).getValueAt(tr.convertRowIndexToModel(selectedRow), 0);
            }
            PreparedStatement st = con.prepareStatement("SELECT * FROM Employees WHERE id = ?");
            st.setString(1, selected.toString());
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                if (rs.getString("id").equals(selected.toString())) {
                    modifyFirstName.setText(rs.getString("firstname"));
                    modifyLastName.setText(rs.getString("lastname"));
                    modifyMiddleInitial.setText(rs.getString("middleinitial"));
                    modifySalary.setText(rs.getString("salary"));
                    modifyJobTitle.setText(rs.getString("jobtitle"));
                    modifyEmail.setText(rs.getString("email"));
                    modifyRole.setSelectedItem(rs.getString("role"));
                    String[] options = {"Modify", "Cancel"};
                    option = JOptionPane.showOptionDialog(errorPanel, modifyOptions,
                            "Modify Employee", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                    if (option == 0) {
                        String salary = modifySalary.getText();
                        if (modifyFirstName.getText().equals("") | modifyLastName.getText().equals("")
                                | modifyMiddleInitial.getText().equals("") | modifySalary.getText().equals("") | modifyJobTitle.getText().equals("")) {
                            throw new InvalidInputException("All fields must have a value before modifying.");
                        }
                        if (modifySalary.getText().contains(",")) {
                            salary = modifySalary.getText().replaceAll(",", "");
                        }
                        double finalSalary = Double.parseDouble(salary);
                        if (finalSalary < 0) {
                            throw new InvalidInputException("Salary cannot be less than zero");
                        }
                        PreparedStatement stmt = con.prepareStatement("UPDATE Employees SET firstname = ?, lastname = ?, middleinitial = ?, salary = ?, jobtitle = ?, email = ?, role = ? WHERE firstname = ?");
                        stmt.setString(1, modifyFirstName.getText());
                        stmt.setString(2, modifyLastName.getText());
                        stmt.setString(3, modifyMiddleInitial.getText());
                        stmt.setDouble(4, finalSalary);
                        stmt.setString(5, modifyJobTitle.getText());
                        stmt.setString(6, modifyEmail.getText());
                        stmt.setString(7, roles[modifyRole.getSelectedIndex()]);
                        stmt.setString(8, rs.getString("firstname"));
                        stmt.executeUpdate();
                        stmt.close();
                    }
                }
            }
            rs.close();
            st.close();
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

    //Registers user on jtable when user is selected and button is pressed.
    private void createAccountButtonActionPerformed(java.awt.event.ActionEvent evt) {
        Object[] registerOptions = {
            "Enter Username: ", createUsername,
            "Enter Password: ", createPassword,
            "Confirm Password: ", verifyPassword
        };
        Connection con = ConnectDatabase.getConnection();
        int selectedRow = jTableEmployees.getSelectedRow();

        try {
            if (selectedRow == -1) {
                throw new InvalidInputException("Select an employee on the table to create there account.");
            }
            Object selected;
            if (this.tr == null) {
                selected = ((DefaultTableModel) jTableEmployees.getModel()).getValueAt(selectedRow, 0);
            } else {
                selected = ((DefaultTableModel) jTableEmployees.getModel()).getValueAt(tr.convertRowIndexToModel(selectedRow), 0);
            }
            PreparedStatement st = con.prepareStatement("SELECT * FROM Employees WHERE id = ?");
            st.setString(1, selected.toString());
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                if (rs.getString("id").equals(selected.toString())) {
                    PreparedStatement st2 = con.prepareStatement("SELECT * FROM LoginInfo WHERE firstname = ? AND lastname = ? AND email = ?");
                    st2.setString(1, rs.getString("firstname"));
                    st2.setString(2, rs.getString("lastname"));
                    st2.setString(3, rs.getString("email"));
                    ResultSet rs2 = st2.executeQuery();
                    if (rs2.next()) {
                        throw new InvalidInputException("Employee already has an account.");
                    }
                    String[] options = {"Create", "Cancel"};
                    option = JOptionPane.showOptionDialog(errorPanel, registerOptions,
                            "Create Account", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                    if (option == 0) {
                        String newPass = new String(createPassword.getPassword());
                        String confirmPass = new String(verifyPassword.getPassword());
                        if (createUsername.getText().equals("") | newPass.equals("")
                                | confirmPass.equals("")) {
                            throw new InvalidInputException("All fields must have a value before registration.");
                        }
                        if (!newPass.equals(confirmPass)) {
                            throw new InvalidInputException("Passwords do not match.");
                        }

                        PreparedStatement stmt = con.prepareStatement("INSERT INTO LoginInfo(username, firstname, lastname, email, role, password) " + "VALUES (?, ?, ?, ?, ?, ?)");
                        stmt.setString(1, createUsername.getText());
                        stmt.setString(2, rs.getString("firstname"));
                        stmt.setString(3, rs.getString("lastname"));
                        stmt.setString(4, rs.getString("email"));
                        stmt.setString(5, rs.getString("role"));
                        stmt.setString(6, BCrypt.hashpw(newPass, BCrypt.gensalt()));
                        stmt.executeUpdate();
                        stmt.close();
                        JOptionPane.showMessageDialog(errorPanel, "Account has been created!", "Success", 1);
                    }

                }
                st.close();
                rs.close();
            }
        } catch (InvalidInputException | SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(errorPanel, e.getMessage(), "Error", 1);
            clearForm();
        } finally {
            try {
                clearForm();
                con.close();
                updateTable();
            } catch (SQLException e) {
            }
        }
    }

    private void deleteAccountButtonActionPerformed(java.awt.event.ActionEvent evt) {
        Object[] deleteOptions = {
            "Enter Username: ", createUsername
        };
        Connection con = ConnectDatabase.getConnection();
        String[] options = {"Delete", "Cancel"};
        option = JOptionPane.showOptionDialog(errorPanel, deleteOptions,
                "Delete User", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (option == 0) {
            try {
                boolean inDatabase = false;
                PreparedStatement stmt = con.prepareStatement("SELECT * FROM LoginInfo");
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    if (rs.getString("username").equals(createUsername.getText())) {
                        inDatabase = true;
                    }
                }
                if (inDatabase) {
                    PreparedStatement st = con.prepareStatement("DELETE FROM LoginInfo WHERE username = ?");
                    st.setString(1, createUsername.getText());
                    st.executeUpdate();
                    JOptionPane.showMessageDialog(errorPanel, "Account has been deleted!", "Success", 1);
                } else {
                    throw new InvalidInputException("Username is not in database!");
                }
                rs.close();
                stmt.close();
                clearForm();
            } catch (SQLException | InvalidInputException e) {
                JOptionPane.showMessageDialog(errorPanel, e.getMessage(), "Error", 1);
            } finally {
                try {
                    con.close();
                } catch (SQLException e) {
                }
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
        main.setLocationRelativeTo(null);
        main.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

}
