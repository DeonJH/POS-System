package inventorymanagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import org.mindrot.jbcrypt.BCrypt;

public class Main_Page extends JFrame implements ActionListener {

    private JMenuBar menuBar;
    private JMenu file, account;
    private JMenuItem exit, changePassword, logout, changeUsername;
    private JPanel errorPanel = new JPanel();
    private static CurrentUser user;
    private JPasswordField currentPassword = new JPasswordField(10);
    private JPasswordField newPassword = new JPasswordField(10);
    private JPasswordField confirmPassword = new JPasswordField(10);
    private JTextField newUsername = new JTextField(10);

    Main_Page() {
        //Creates a better feel to the application with Nimbus
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main_Page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //Sets the layout for the frame
        setLayout(new GridLayout(1, 1));
        JPanel backGroundPanel = new JPanel();
        backGroundPanel.setLayout(null);
        JLabel picLabel = new JLabel(new ImageIcon(getClass().getResource("/inventorymanagement/images/option2.png")));
        picLabel.setBounds(250, 0, 900, 750);
        backGroundPanel.add(picLabel);
        JPanel picPanel = new JPanel();
        picPanel.setBackground(new Color(147, 134, 134));
        picPanel.setBounds(0, 0, 900, 780);

        JButton productSelection = new JButton("PRODUCTS");
        productSelection.setBounds(20, 100, 250, 100);
        productSelection.addActionListener((ActionEvent evt) -> {
            productSelectionActionPerformed(evt);
        });
        productSelection.setFocusable(false);
        productSelection.setBackground(new Color(50, 56, 64));
        productSelection.setForeground(new Color(255, 255, 255));
        productSelection.setFont(new Font("Arial", Font.PLAIN, 16));

        JButton orderSelection = new JButton("ORDERS");
        orderSelection.setBounds(20, 250, 250, 100);
        orderSelection.addActionListener((java.awt.event.ActionEvent evt) -> {
            orderSelectionActionPerformed(evt);
        });
        orderSelection.setBackground(new Color(50, 56, 64));
        orderSelection.setFocusable(false);
        orderSelection.setForeground(new Color(255, 255, 255));
        orderSelection.setFont(new Font("Arial", Font.PLAIN, 16));

        JButton reportsSelection = new JButton("REPORTS");
        reportsSelection.setBounds(20, 400, 250, 100);
        reportsSelection.addActionListener((java.awt.event.ActionEvent evt) -> {
            reportsSelectionActionPerformed(evt);
        });
        reportsSelection.setFocusable(false);
        reportsSelection.setBackground(new Color(50, 56, 64));
        reportsSelection.setForeground(new Color(255, 255, 255));
        reportsSelection.setFont(new Font("Arial", Font.PLAIN, 16));

        JButton employeeSelection = new JButton("EMPLOYEE INFO");
        employeeSelection.setBounds(20, 550, 250, 100);
        employeeSelection.addActionListener((java.awt.event.ActionEvent evt) -> {
            employeeSelectionActionPerformed(evt);
        });
        employeeSelection.setFocusable(false);
        employeeSelection.setBackground(new Color(50, 56, 64));
        employeeSelection.setForeground(new Color(255, 255, 255));
        employeeSelection.setFont(new Font("Arial", Font.PLAIN, 16));

        backGroundPanel.add(productSelection);
        backGroundPanel.add(orderSelection);
        backGroundPanel.add(reportsSelection);
        backGroundPanel.add(employeeSelection);
        backGroundPanel.add(picPanel);

        user = Login_Page.user;
        menuBar = new JMenuBar();
        file = new JMenu();
        account = new JMenu();
        exit = new JMenuItem();
        changePassword = new JMenuItem();
        changeUsername = new JMenuItem();
        logout = new JMenuItem();
        file.setText("File");
        file.setFont(new Font("Arial", Font.PLAIN, 16));
        exit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_DOWN_MASK));
        exit.setText("Exit");
        exit.setFont(new Font("Arial", Font.PLAIN, 16));
        exit.addActionListener((java.awt.event.ActionEvent evt) -> {
            exitActionPerformed(evt);
        });
        file.add(exit);
        account.setFont(new Font("Arial", Font.PLAIN, 16));
        account.setText("Logged in: " + user.getUsername());
        account.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        account.setSize(10, 5);
        changePassword.setText("Change Password");
        changePassword.setFont(new Font("Arial", Font.PLAIN, 16));
        changePassword.addActionListener((java.awt.event.ActionEvent evt) -> {
            changePasswordActionPerformed(evt);
        });
        changeUsername.setText("Change Username");
        changeUsername.addActionListener((java.awt.event.ActionEvent evt) -> {
            changeUsernameActionPerformed(evt);
        });
        changeUsername.setFont(new Font("Arial", Font.PLAIN, 16));
        logout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.ALT_DOWN_MASK));
        logout.setText("Logout");
        logout.setFont(new Font("Arial", Font.PLAIN, 16));
        logout.addActionListener((java.awt.event.ActionEvent evt) -> {
            logoutActionPerformed(evt);
        });
        account.add(changePassword);
        account.add(changeUsername);
        account.add(logout);
        menuBar.add(file);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(account);
        setJMenuBar(menuBar);
        add(backGroundPanel);
        setSize(1135, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
        if (!user.getRole().equals("Manager")) {
            employeeSelection.setEnabled(false);
            reportsSelection.setEnabled(false);
        }
    }

    private void logoutActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            UIManager.getLookAndFeelDefaults().putAll(Login_Page.previousLF);
        } catch (Exception e) {
        }
        this.dispose();
        Login_Page login = new Login_Page();
        login.setVisible(true);
    }

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    private void changePasswordActionPerformed(java.awt.event.ActionEvent evt) {
        clearForm();
        Object[] changePasswordFields = {
            "Enter current password: ", currentPassword,
            "Enter new password: ", newPassword,
            "Confirm new password: ", confirmPassword
        };
        String[] options = {"Confirm", "Cancel"};
        int option = JOptionPane.showOptionDialog(errorPanel, changePasswordFields,
                "Change Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (option == 0) {
            String password = new String(currentPassword.getPassword());
            String newPass = new String(newPassword.getPassword());
            String confirmPass = new String(confirmPassword.getPassword());
            Connection con = ConnectDatabase.getConnection();
            try {
                PreparedStatement st = con.prepareStatement("SELECT password FROM LoginInfo WHERE username = ?");
                st.setString(1, user.getUsername());
                ResultSet rs = st.executeQuery();
                String result = "";
                if (rs.next()) {
                    Scanner s = new Scanner(rs.getBinaryStream("password")).useDelimiter("\\A");
                    result = s.hasNext() ? s.next() : "";
                }
                if (BCrypt.checkpw(password, result)) {
                    PreparedStatement stmt = con.prepareStatement("UPDATE LoginInfo SET password = ? WHERE username = ?");
                    String hash;
                    if (newPass.equals(confirmPass) && !newPass.equals("") && !confirmPass.equals("")) {
                        hash = BCrypt.hashpw(newPass, BCrypt.gensalt());
                    } else {
                        throw new InvalidInputException("Passwords do not match.");
                    }
                    stmt.setString(1, hash);
                    stmt.setString(2, user.getUsername());
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(errorPanel, "You changed your password!", "Success", 1);
                    st.close();
                    rs.close();
                    stmt.close();
                } else {
                    throw new InvalidInputException("You entered the wrong current password!");
                }
            } catch (SQLException | InvalidInputException e) {
                JOptionPane.showMessageDialog(errorPanel, e.getMessage(), "Warning", 2);
            } finally {
                try {
                    con.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    private void changeUsernameActionPerformed(java.awt.event.ActionEvent evt) {
        Object[] changeUsernameFields = {
            "Enter new username: ", newUsername,};
        String[] options = {"Confirm", "Cancel"};
        int option = JOptionPane.showOptionDialog(errorPanel, changeUsernameFields,
                "Change Username", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (option == 0) {
            Connection con = ConnectDatabase.getConnection();
            try {
                if (newUsername.getText().equals("")) {
                    throw new InvalidInputException("Please enter a username.");
                }
                String newUser = newUsername.getText();
                PreparedStatement st = con.prepareStatement("UPDATE LoginInfo set username = ? WHERE username = ?");
                st.setString(1, newUser);
                st.setString(2, user.getUsername());
                st.executeUpdate();
                JOptionPane.showMessageDialog(errorPanel, "You changed your username from " + user.getUsername() + " to " + newUser, "Success", 1);
                user.setUsername(newUser);
                account.setText("Logged in: " + user.getUsername());
                st.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(errorPanel, "Username already exists!", "Warning", 2);
            } catch (InvalidInputException e) {
                JOptionPane.showMessageDialog(errorPanel, e.getMessage(), "Warning", 2);
            } finally {
                try {
                    newUsername.setText("");
                    con.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    private void orderSelectionActionPerformed(ActionEvent evt) {
        this.dispose();
        Orders_Page order = new Orders_Page();
        order.setVisible(true);
        order.setLocationRelativeTo(null);
    }

    private void productSelectionActionPerformed(ActionEvent evt) {
        this.dispose();
        Product_Inventory_Page product = new Product_Inventory_Page();
        product.setVisible(true);
    }

    private void reportsSelectionActionPerformed(ActionEvent evt) {
        this.dispose();
        Reports_Page reports = new Reports_Page();
        reports.setVisible(true);
    }

    private void employeeSelectionActionPerformed(ActionEvent evt) {
        this.dispose();
        Employee_Info_Page employee = new Employee_Info_Page();
        employee.setVisible(true);
    }

    public void clearForm() {
        currentPassword.setText("");
        newPassword.setText("");
        confirmPassword.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

}
