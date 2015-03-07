package com.polytech4A.pop3.client.ui;

import com.polytech4A.pop3.client.core.Client;
import com.polytech4A.pop3.client.core.state.State;
import com.polytech4A.pop3.client.core.state.StateAuthentification;

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
    private JTextField passwordTextInput;
    private JButton validerButton1;
    private JTextField userTextInput;

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

        validerButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                validateButton1ActionPerformed(evt);
            }
        });
    }


    /**
     * Function will initialize the different components of the window
     */
    public void initComponents(){
        this.setSize(400, 400);
        this.panel1.setVisible(true);
        this.add(panel1);
    }


    /**
     * When a click on the button occured, we call a connection request of the client
     * @param evt
     */
    private void validateButtonActionPerformed(java.awt.event.ActionEvent evt){
        String address = this.AddressTextInput.getText();
        int port = Integer.parseInt(this.PortTextInput.getText());

        this.client.establishConnection(address, port);
    }


    /**
     * When a click on the button 1 occured, we call a connection request of the client
     * @param evt
     */
    private void validateButton1ActionPerformed(java.awt.event.ActionEvent evt){
        String user = this.userTextInput.getText();
        String password = this.passwordTextInput.getText();

        this.client.makeAuthentification(user, password);
    }


    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof Client){
            State currentState = ((Client) o).getCurrentState();

            if(currentState instanceof StateAuthentification){
                this.panel1.setVisible(false);
                this.remove(panel1);
                this.panel2.setVisible(true);
                this.add(panel2);
            }
        }
    }
}
