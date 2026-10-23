package suitescape;

import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class LoginForm extends JFrame implements ActionListener {
    JLabel lblLogo = new JLabel("SUITESCAPE LOGIN");
    JLabel lblUsername = new JLabel("Username:");
    JLabel lblPassword = new JLabel("Password:");
    JTextField txtUsername = new JTextField();
    JPasswordField txtPassword = new JPasswordField();
    JButton btnLogin = new JButton("Log-in");
    JButton btnRegisterNav = new JButton("Register");
    
    public static ArrayList<Account> accounts = new ArrayList<>();
    
    public LoginForm() {
        this.setSize(400, 280);
        setTitle("Login Form");
        setLayout(null);
        
        this.add(lblLogo);
        lblLogo.setBounds(140, 20, 150, 20);
        
        this.add(lblUsername);
        lblUsername.setBounds(60, 70, 70, 20);
        this.add(txtUsername);
        txtUsername.setBounds(130, 65, 190, 30);
        
        this.add(lblPassword);
        lblPassword.setBounds(60, 110, 70, 20);
        this.add(txtPassword);
        txtPassword.setBounds(130, 105, 190, 30);
        
        this.add(btnLogin);
        btnLogin.setBounds(130, 170, 90, 30);
        btnLogin.addActionListener(this);
        
        this.add(btnRegisterNav);
        btnRegisterNav.setBounds(230, 170, 90, 30);
        btnRegisterNav.addActionListener(this);
        
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnLogin)) {
            String userInputName = txtUsername.getText().trim();
            String userPassword = new String(txtPassword.getPassword());
            boolean accountFound = false;
            
            for (Account acc : accounts) {
                if (acc.getUsername().equals(userInputName) && acc.getPassword().equals(userPassword)) {
                    accountFound = true;
                    break;
                }
            }
            
            if (accountFound) {
                JOptionPane.showMessageDialog(null, "Successfully Logged In", "Welcome Message", JOptionPane.INFORMATION_MESSAGE);
                BookingForm b = new BookingForm();
                b.setVisible(accountFound);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid Username or Password!", "Warning Message", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource().equals(btnRegisterNav)) {
            AccountCreation a = new AccountCreation();
            a.setVisible(true);
            this.dispose();
        }
    }
}