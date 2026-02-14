package com.cinema.view.auth;

import com.cinema.controller.auth.LoginController;
import com.cinema.util.constants.DimensionConstants;
import com.cinema.util.constants.TextConstants;
import com.cinema.view.CinemaUIHandler;
import com.cinema.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginFrame extends JFrame implements ActionListener {
    private final JTextField emailField;
    private final JPasswordField pswField;
    private final JButton loginBtn;

    public LoginFrame() {
        setTitle(TextConstants.TITLE + " - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        emailField = new JTextField(15);
        pswField = new JPasswordField(15);
        loginBtn = new JButton("Login");

        loginBtn.addActionListener(this);
        getRootPane().setDefaultButton(loginBtn);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setPreferredSize(DimensionConstants.MAIN_FRAME_DIMENSION);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Email: "), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password: "), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(pswField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(loginBtn, gbc);

        add(panel);
        pack();
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginBtn) {
            String email = emailField.getText();
            String psw = new String(pswField.getPassword());

            try {
                LoginController loginController = LoginController.getInstance();
                User user = loginController.login(email, psw);

                if(user == null) {
                    JOptionPane.showMessageDialog(this, "Invalid Username or Password!", "Login Error", JOptionPane.ERROR_MESSAGE);
                    pswField.setText("");
                } else {
                    this.dispose();
                    openMainWindow(user);
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private void openMainWindow(User user) {
        new CinemaUIHandler(user);
    }
}
