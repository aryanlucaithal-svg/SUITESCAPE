package suitescape;

import java.awt.event.*;
import javax.swing.*;

public class AccountCreation extends JFrame implements ActionListener {
    JLabel lblLogo = new JLabel("SUITESCAPE REGISTRATION");
    JLabel lblUsername = new JLabel("Username:");
    JLabel lblPassword = new JLabel("Password:");
    JLabel lblConfirmPassword = new JLabel("Confirm Password:");
    JTextField txtUsername = new JTextField();
    JPasswordField txtPassword = new JPasswordField();
    JPasswordField txtConfirmPassword = new JPasswordField();
    JButton btnRegister = new JButton("Register");
    JButton btnCancel = new JButton("Cancel");
    
    public AccountCreation() {
        this.setSize(450, 350);
        setTitle("Account Creation");
        setLayout(null);
        
        this.add(lblLogo);
        lblLogo.setBounds(130, 20, 180, 20);

        this.add(lblUsername);
        lblUsername.setBounds(30, 70, 100, 20);
        this.add(txtUsername);
        txtUsername.setBounds(140, 65, 230, 30);
        
        this.add(lblPassword);
        lblPassword.setBounds(30, 110, 100, 20);
        this.add(txtPassword);
        txtPassword.setBounds(140, 105, 230, 30);
        
        this.add(lblConfirmPassword);
        lblConfirmPassword.setBounds(15, 150, 120, 20);
        this.add(txtConfirmPassword);
        txtConfirmPassword.setBounds(140, 145, 230, 30);

        this.add(btnRegister);
        btnRegister.setBounds(175, 200, 90, 30);
        btnRegister.addActionListener(this);
        
        this.add(btnCancel);
        btnCancel.setBounds(280, 200, 90, 30);
        btnCancel.addActionListener(this);
        
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnRegister)) {
            String newUser = txtUsername.getText().trim();
            String newPass = new String(txtPassword.getPassword());
            String confirmPass = new String(txtConfirmPassword.getPassword());
            
            if (newUser.isEmpty() || newPass.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Fields cannot be empty!", "Warning Message", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (newPass.equals(confirmPass)) {
                LoginForm.accounts.add(new Account(newUser, newPass));
                JOptionPane.showMessageDialog(null, "Account Registered Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                
                new LoginForm().setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Passwords don't match!", "Warning Message", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource().equals(btnCancel)) {
            int i = JOptionPane.showConfirmDialog(null, "Do you want to go back?", "Cancel Message", JOptionPane.YES_NO_OPTION);
            if (i == JOptionPane.YES_OPTION) {
                new LoginForm().setVisible(true);
                this.dispose();
            }
        }
    }
}