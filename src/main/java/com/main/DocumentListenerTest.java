/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.main;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class DocumentListenerTest extends JFrame {

    private JTextField searchField;

    public DocumentListenerTest() {
        setTitle("DocumentListener Test");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        searchField = new JTextField();
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                System.out.println("insertUpdate called");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                System.out.println("removeUpdate called");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                System.out.println("changedUpdate called");
            }
        });

        // Panel setup
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(searchField, BorderLayout.CENTER);

        // Add components to the frame
        add(panel, BorderLayout.NORTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DocumentListenerTest();
        });
    }
}
