package com.polytech4A.pop3.client.ui;

import com.polytech4A.pop3.client.core.Client;
import com.polytech4A.pop3.client.core.state.State;
import com.polytech4A.pop3.client.core.state.StateAuthentication;
import com.polytech4A.pop3.client.core.state.StateTransaction;

import javax.swing.*;
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
    private JPanel panel3;
    private JEditorPane resultPanel;
    private JButton closeButton;

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

        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
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
     * When a click on the button occurred, we call a connection request of the client
     * @param evt
     */
    private void validateButtonActionPerformed(java.awt.event.ActionEvent evt){
        String address = this.AddressTextInput.getText();
        int port = Integer.parseInt(this.PortTextInput.getText());

        this.client.establishConnection(address, port);
    }


    /**
     * When a click on the button 1 occurred, we call an authentication request on the client
     * @param evt
     */
    private void validateButton1ActionPerformed(java.awt.event.ActionEvent evt){
        String user = this.userTextInput.getText();
        String password = this.passwordTextInput.getText();

        this.client.makeAuthentication(user, password);
    }


    /**
     * Will close the connection, delete the different objects instantiate in the client,
     * and go back to the first view
     * @param evt
     */
    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt){
        this.client.closeConnection();
        this.panel3.setVisible(false);
        this.remove(panel3);
        this.panel1.setVisible(true);
        this.add(panel1);
    }


    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof Client){
            //Will show errors if they occurred
            if(((Client) o).getErrorOccurred()){
                JOptionPane.showMessageDialog(null, ((Client) o).getLastErrorMessage(), "Erreur au sein de l'application", JOptionPane.ERROR_MESSAGE);
                //TODO Close the connection if needed: new parameter?
            }

            State currentState = ((Client) o).getCurrentState();

            if(currentState instanceof StateAuthentication){
                this.panel1.setVisible(false);
                this.remove(panel1);
                this.panel2.setVisible(true);
                this.add(panel2);
            }

            if(currentState instanceof StateTransaction){
                this.panel2.setVisible(false);
                this.remove(panel2);
                this.panel3.setVisible(true);
                this.add(panel3);

                this.resultPanel.setText("");
            }
        }
    }
}
