package com.polytech4A.pop3.client.ui;

import com.polytech4A.pop3.client.core.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

/**
 * Class for the main window of the client application
 */
public class MainForm extends javax.swing.JFrame implements Observer {
    private JPanel panel1;
    private JPanel panel2;
    private JTextField AddressTextInput;
    private JTextField PortTextInput;
    private JButton validerButton;

    private Client client;

    /**
     * Creates new form
     */
    public MainForm(Client _client) {
        this.client = _client;
        this.initComponents();

        validerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                validateButtonActionPerformed(evt);
            }
        });
    }


    /**
     * Function will initialize the different components of the window
     */
    public void initComponents(){
        this.setSize(400, 400);
        this.panel1.setVisible(true);
        this.panel2.setVisible(true);
        this.add(panel2);
    };

    /**
     * When a click on the button occured, we call a connection request of the client
     * @param evt
     */
    private void validateButtonActionPerformed(java.awt.event.ActionEvent evt){
        String address = this.AddressTextInput.getText();
        int port = Integer.parseInt(this.PortTextInput.getText());

        this.client.establishConnection(address, port);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
