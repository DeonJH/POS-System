package inventorymanagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import org.mindrot.jbcrypt.BCrypt;

public class Login_Page extends JFrame implements ActionListener {

    ConnectDatabase data = new ConnectDatabase();
    public static CurrentUser user;
    public static Main_Page mainMenu;
    private JPanel errorPanel = new JPanel();
    Connection con = data.getConnection();
    private JPasswordField passwordText;
    private JTextField userText;
    public static UIDefaults previousLF;
    JButton loginButton;
    
    Login_Page() {
        super("Login Menu");
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
        JPanel panel = new JPanel();
        panel.setBackground(new java.awt.Color(147, 134, 134));
        setSize(400, 350);
        add(panel);

        panel.setLayout(null);

        JLabel formTitle = new JLabel("Account Login");
        formTitle.setBounds(100, 50, 230, 35);
        formTitle.setFont(new java.awt.Font("Arial", Font.PLAIN, 30));
        formTitle.setForeground(new java.awt.Color(255, 255, 255));;
        formTitle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        panel.add(formTitle);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new java.awt.Font("Arial", Font.PLAIN, 16));
        userLabel.setForeground(new java.awt.Color(255, 255, 255));
        userLabel.setBounds(50, 110, 80, 25);
        panel.add(userLabel);

        userText = new JTextField(20);
        userText.setBounds(150, 110, 170, 30);
        userText.setFont(new java.awt.Font("Tahoma", Font.PLAIN, 14));
        panel.add(userText);
        userText.addKeyListener(new KeyAdapter(){
                @Override
                public void keyPressed(KeyEvent e){
                    if(e.getKeyCode() == KeyEvent.VK_ENTER){
                        loginButton.doClick();
                    }       
                }
        });
        
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new java.awt.Font("Arial", Font.PLAIN, 16));
        passwordLabel.setForeground(new java.awt.Color(255, 255, 255));
        passwordLabel.setBounds(50, 150, 80, 25);
        panel.add(passwordLabel);

        passwordText = new JPasswordField();
        passwordText.setBounds(150, 150, 170, 30);
        passwordText.setFont(new java.awt.Font("Tahoma", Font.PLAIN, 14));
        panel.add(passwordText);
        passwordText.addKeyListener(new KeyAdapter(){
                @Override
                public void keyPressed(KeyEvent e){
                    if(e.getKeyCode() == KeyEvent.VK_ENTER){
                        loginButton.doClick();
                    }       
                }
        });
        
        loginButton = new JButton("Login");
        loginButton.addActionListener((java.awt.event.ActionEvent evt) -> {
            loginButtonActionPerformed(evt);
        });
        loginButton.setBackground(new java.awt.Color(168, 106, 106));
        loginButton.setForeground(new java.awt.Color(245, 242, 242));
        loginButton.setFont(new java.awt.Font("Tahoma", Font.BOLD, 14));
        loginButton.setBounds(130, 210, 140, 40);
        panel.add(loginButton);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {
        boolean inDatabase = false;
        try {
            Statement stmt = con.createStatement();
            String selectAll = "select * FROM LoginInfo";
            ResultSet rs = stmt.executeQuery(selectAll);
            String password = new String(passwordText.getPassword());
            while (rs.next()) {
                if (rs.getString("username").equalsIgnoreCase(userText.getText())) {
                    Scanner s = new Scanner(rs.getBinaryStream("password")).useDelimiter("\\A");
                    String result = s.hasNext() ? s.next() : "";
                    if (BCrypt.checkpw(password, result)) {
                        inDatabase = true;
                        user = new CurrentUser(rs.getString("firstname"), rs.getString("lastname"),
                                rs.getString("username"), rs.getString("role"));
                        JOptionPane.showMessageDialog(errorPanel, user.getFirstName() + " " + user.getLastName() + " has logged in!", "Logged In", 1);
                        mainMenu = new Main_Page();
                        mainMenu.setVisible(true);
                        this.dispose();
                    } else {
                        throw new InvalidInputException("You entered the wrong password");
                    }
                }
            }
            if (!inDatabase) {
                throw new InvalidInputException("Username does not exist.");
            }
            stmt.close();
            rs.close();
        } catch (InvalidInputException | SQLException e) {
            JOptionPane.showMessageDialog(errorPanel, e.getMessage(), "Incorrect Input", 1);
        }
    }

    public static void main(String args[]) {

        EventQueue.invokeLater(() -> {
            Login_Page login = new Login_Page();
            login.setVisible(true);
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

}
