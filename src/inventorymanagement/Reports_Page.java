package inventorymanagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;

public class Reports_Page extends JFrame implements ActionListener {

    private JComboBox categoryList;

    Reports_Page() {
        super("Reports Page");
        setLayout(new GridLayout(1, 1));
        JPanel panel = new JPanel();
        panel.setBackground(new Color(147, 134, 134));
        setSize(400, 540);
        add(panel);

        panel.setLayout(null);

        JLabel formTitle = new JLabel("Generate Reports");
        formTitle.setBounds(100, 50, 230, 35);
        formTitle.setForeground(new Color(255, 255, 255));
        formTitle.setFont(new Font("Arial", Font.PLAIN, 25));
        formTitle.setHorizontalTextPosition(SwingConstants.CENTER);
        panel.add(formTitle);

        JLabel userLabel = new JLabel("Report Type:");
        userLabel.setForeground(new Color(255, 255, 255));
        userLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        userLabel.setBounds(20, 140, 90, 25);
        panel.add(userLabel);

        String[] categoryChoiceList = {"Product Inventory Value", "Total Employee Cost", "Total Employee and Inventory Amount", "Products Sold Total Value", "Total Profit Made From Products"}; //"Total Profit Made", "Total Employee Salary Cost (Monthly)", "Total Revenue Made"};
        categoryList = new JComboBox(categoryChoiceList);
        categoryList.setBounds(120, 140, 240, 30);
        panel.add(categoryList);

        JButton cancelButton = new JButton("Main Menu");
        cancelButton.addActionListener((java.awt.event.ActionEvent evt) -> {
            cancelButtonActionPerformed(evt);
        });
        cancelButton.setBackground(new Color(50, 46, 46));
        cancelButton.setForeground(new Color(245, 242, 242));
        cancelButton.setFont(new Font("Tahoma", 0, 14));
        cancelButton.setBounds(50, 380, 130, 40);
        panel.add(cancelButton);

        JButton runButton = new JButton("Run Report");
        runButton.setBackground(new Color(86, 99, 90));
        runButton.addActionListener((java.awt.event.ActionEvent evt) -> {
            runButtonActionPerformed(evt);
        });
        runButton.setForeground(new Color(245, 242, 242));
        runButton.setFont(new Font("Tahoma", 0, 14));
        runButton.setBounds(200, 380, 130, 40);
        panel.add(runButton);
        add(panel);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void runButtonActionPerformed(ActionEvent evt) {
        Connection con = ConnectDatabase.getConnection();
        switch (categoryList.getSelectedIndex()) {
            case 0:
                try {
                Statement stmt = con.createStatement();
                String selectAll = "select quantity, price FROM Products";
                ResultSet rs = stmt.executeQuery(selectAll);
                double totalAmount = 0;
                while (rs.next()) {
                    BigDecimal value = rs.getBigDecimal("price");
                    int quantity = rs.getInt("quantity");
                    Double price = value.doubleValue();
                    totalAmount += quantity * price;
                }
                DecimalFormat df = new DecimalFormat("###,###.##");
                JOptionPane.showMessageDialog(null, "Total: $"
                        + df.format(totalAmount), "Product Inventory Value", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Warning", 2);
            }
            break;

            case 1:
                try {
                Statement stmt = con.createStatement();
                String selectAll = "select salary FROM Employees";
                ResultSet rs = stmt.executeQuery(selectAll);
                double totalAmount = 0;
                while (rs.next()) {
                    int salary = rs.getInt("salary");
                    totalAmount += salary;
                }
                stmt.close();
                rs.close();
                DecimalFormat df = new DecimalFormat("###,###.##");
                JOptionPane.showMessageDialog(null, "Total: $"
                        + df.format(totalAmount), "Cost of all Employees", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Warning", 2);
            }
            break;

            case 2:
                try {
                Statement stmt = con.createStatement();
                Statement stmt2 = con.createStatement();
                String selectProducts = "select quantity, price FROM Products";
                String selectEmployees = "select salary FROM Employees";
                ResultSet rs = stmt.executeQuery(selectProducts);
                ResultSet rs2 = stmt2.executeQuery(selectEmployees);

                double totalProducts = 0;
                double totalEmployees = 0;

                while (rs2.next()) {
                    int salary = rs2.getInt("salary");
                    totalProducts += salary;
                }

                while (rs.next()) {
                    BigDecimal value = rs.getBigDecimal("price");
                    int quantity = rs.getInt("quantity");
                    Double price = value.doubleValue();
                    totalEmployees += quantity * price;
                }
                rs.close();
                rs2.close();
                stmt.close();
                stmt2.close();

                DecimalFormat df = new DecimalFormat("###,###.##");
                JOptionPane.showMessageDialog(null, "Total: $"
                        + df.format(totalProducts + totalEmployees), "Total Employee and Inventory Amount", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Warning", 2);
            }
            try {
                con.close();
            } catch (SQLException e) {
            }
            break;

            case 3:
                try {
                Statement st = con.createStatement();
                String sql = "SELECT price, quantity FROM ProductSold";
                ResultSet rs = st.executeQuery(sql);
                double totalAmount = 0;
                while (rs.next()) {
                    BigDecimal value = rs.getBigDecimal("price");
                    int quantity = rs.getInt("quantity");
                    Double price = value.doubleValue();
                    totalAmount += quantity * price;
                }
                DecimalFormat df = new DecimalFormat("###,###.##");
                JOptionPane.showMessageDialog(null, "Total: $"
                        + df.format(totalAmount), "Products Sold Total Value", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Warning", 2);
            }
            try {
                con.close();
            } catch (SQLException e) {
            }
            break;

            case 4:
                try {
                Statement st = con.createStatement();
                String sql = "SELECT productname, price, quantity FROM ProductSold";
                ResultSet rs = st.executeQuery(sql);
                double totalAmount = 0;
                while (rs.next()) {
                    PreparedStatement st1 = con.prepareStatement("SELECT price FROM Products WHERE name = ?");
                    st1.setString(1, rs.getString("productname"));
                    ResultSet rs1 = st1.executeQuery();
                    if (rs1.next()) {
                        double originalPrice = rs1.getDouble("price");
                        double soldPrice = rs.getDouble("price");
                        totalAmount += (soldPrice - originalPrice) * rs.getInt("quantity");
                    }
                }
                DecimalFormat df = new DecimalFormat("###,###.##");
                JOptionPane.showMessageDialog(null, "Total: $"
                        + df.format(totalAmount), "Total Profit Made From Products", JOptionPane.INFORMATION_MESSAGE);                

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Warning", 2);
            }
            try {
                con.close();
            } catch (SQLException e) {
            }
            break;
        }
    }

    private void cancelButtonActionPerformed(ActionEvent evt) {
        this.dispose();
        Main_Page main = new Main_Page();
        main.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
