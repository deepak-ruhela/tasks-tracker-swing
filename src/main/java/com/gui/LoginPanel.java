package com.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.util.AuthService;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;

public class LoginPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField nameTextField;
    private JPasswordField passwordTextField;
    private MyApp myApp;

    public LoginPanel(MyApp myApp) {
        this.myApp = myApp;
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout()); // Use BorderLayout for overall structure

        // Panel for inputs (center)
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS)); // Vertical BoxLayout

        Font font = new Font("Arial", Font.BOLD, 20);

        // Name panel
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel nameLabel = new JLabel("Enter Name:");
        nameLabel.setFont(font);
        nameTextField = new JTextField(20); // Increased columns for more space
        nameTextField.setFont(font);
        namePanel.add(nameLabel);
        namePanel.add(nameTextField);

        // Password panel
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel passwordLabel = new JLabel("Enter Password:");
        passwordLabel.setFont(font);
        passwordTextField = new JPasswordField(20); // Increased columns for more space
        passwordTextField.setFont(font);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordTextField);

        // Panel for buttons (south)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // FlowLayout for buttons
        JButton loginButton = new JButton("Login");
        loginButton.setFont(font);
        loginButton.addActionListener(e -> handleLoginButton());

        JButton resetButton = new JButton("Reset");
//        resetButton.setFont(font);
        resetButton.addActionListener(e -> handleResetButton());

        JButton registerButton = new JButton("Register");
//        registerButton.setFont(font);
        registerButton.addActionListener(e -> handleRegisterButton());

        JButton forgetPasswordButton = new JButton("Forget!");
//        forgetPasswordButton.setFont(font);
        forgetPasswordButton.addActionListener(e -> handleForgetPasswordButton());

        // Create a panel for the row of buttons
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        rowPanel.add(resetButton);
        rowPanel.add(registerButton);
        rowPanel.add(forgetPasswordButton);

//        input
        inputPanel.add(namePanel);
        inputPanel.add(passwordPanel);

        buttonPanel.add(loginButton);
//        buttonPanel.add(resetButton);
//        buttonPanel.add(registerButton);
//        buttonPanel.add(forgetPasswordButton);
        // Add components to the main panel

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(buttonPanel);
        mainPanel.add(rowPanel);

        add(inputPanel, BorderLayout.CENTER);
//        add(buttonPanel, BorderLayout.NORTH); // Place loginButton at the top
//        add(rowPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.SOUTH);
    }

    private void handleLoginButton() {
        String name = nameTextField.getText();
        String password = new String(passwordTextField.getPassword());

        if (AuthService.verifyUser(name, password)) {
            myApp.showHomePanel();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleResetButton() {
        nameTextField.setText("");
        passwordTextField.setText("");
    }

    private void handleRegisterButton() {
        String name = nameTextField.getText();
        String password = new String(passwordTextField.getPassword());

        if (AuthService.isThisUserExist(name)) {
            JOptionPane.showMessageDialog(this, "This User is already registered in the application.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (AuthService.isAnyUserExist()) {
            JOptionPane.showMessageDialog(this, "A User is already registered in the application.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            AuthService.registerUser(name, password);
            JOptionPane.showMessageDialog(this, "User registered successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handleForgetPasswordButton() {
        JOptionPane.showMessageDialog(this, "Please contact your admin", "Forget Password", JOptionPane.INFORMATION_MESSAGE);
    }

//    @Override
//    public Dimension getPreferredSize() {
//        return new Dimension(350, 350); // Set preferred size of the panel
//    }
}
