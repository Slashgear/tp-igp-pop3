package com.polytech4A.pop3.client.ui;

import com.polytech4A.pop3.client.core.Client;
import com.polytech4A.pop3.client.core.state.State;
import com.polytech4A.pop3.client.core.state.StateAuthentication;
import com.polytech4A.pop3.client.core.state.StateTransaction;
import com.polytech4A.pop3.client.core.state.StateWFClose;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Class for the main window of the client application
 */
public class MainForm extends javax.swing.JFrame implements Observer {
    private JPanel panelStart;
    private JPanel panelAuthenticate;
    private JTextField AddressTextInput;
    private JTextField PortTextInput;
    private JButton validerStartButton;
    private JTextField passwordTextInput;
    private JButton validerAuthenticateButton;
    private JTextField userTextInput;
    private JPanel panelMail;
    private JEditorPane resultPanel;
    private JButton closeConnectionButton;

    private JPanel currentPannel;

    private Client client;

    /**
     * Creates new form
     */
    public MainForm(Client _client) {
        this.client = _client;
        this.initComponents();

        validerStartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                validateButtonStartActionPerformed(evt);
            }
        });

        validerAuthenticateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                validateButtonAuthenticateActionPerformed(evt);
            }
        });

        closeConnectionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeConnectionButtonActionPerformed(evt);
            }
        });
    }


    /**
     * Function will initialize the different components of the window
     */
    public void initComponents(){
        this.setSize(400, 400);
        this.panelStart.setVisible(true);
        this.add(panelStart);
        this.currentPannel = panelStart;
        this.AddressTextInput.setText("127.0.0.1");
        this.PortTextInput.setText("1010");
    }


    /**
     * When a click on the button occurred, we call a connection request of the client
     * @param evt
     */
    private void validateButtonStartActionPerformed(java.awt.event.ActionEvent evt){
        if(!this.AddressTextInput.getText().equals("") && !this.PortTextInput.getText().equals("")){
            String address = this.AddressTextInput.getText();
            int port = Integer.parseInt(this.PortTextInput.getText());

            this.client.establishConnection(address, port);
        }
    }


    /**
     * When a click on the button 1 occurred, we call an authentication request on the client
     * @param evt
     */
    private void validateButtonAuthenticateActionPerformed(java.awt.event.ActionEvent evt){
        if(!this.userTextInput.getText().equals("") && !this.passwordTextInput.getText().equals("")){
            String user = this.userTextInput.getText();
            String password = this.passwordTextInput.getText();

            this.client.makeAuthentication(user, password);
        }
    }


    /**
     * Will close the connection, delete the different objects instantiate in the client,
     * and go back to the first view
     * @param evt
     */
    private void closeConnectionButtonActionPerformed(java.awt.event.ActionEvent evt){
        this.client.askForCloseConnection();
        this.panelMail.setVisible(false);
        this.remove(panelMail);
        this.panelStart.setVisible(true);
        this.add(panelStart);
        this.currentPannel = panelStart;
    }


    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof Client){
            //Will show errors if they occurred
            if(((Client) o).getErrorOccurred()){
                JOptionPane.showMessageDialog(null, ((Client) o).getLastErrorMessage(), "Erreur au sein de l'application", JOptionPane.ERROR_MESSAGE);
            }

            State currentState = ((Client) o).getCurrentState();

            if(currentState == null || currentState instanceof StateWFClose){
                this.currentPannel.setVisible(false);
                this.remove(this.currentPannel);
                this.panelStart.setVisible(true);
                this.add(panelStart);
                this.currentPannel = panelStart;
            }

            if(currentState instanceof StateAuthentication){
                this.panelStart.setVisible(false);
                this.remove(panelStart);
                this.panelAuthenticate.setVisible(true);
                this.add(panelAuthenticate);
                this.currentPannel = panelAuthenticate;
            }

            if(currentState instanceof StateTransaction){
                this.panelAuthenticate.setVisible(false);
                this.remove(panelAuthenticate);
                this.panelMail.setVisible(true);
                this.add(panelMail);
                this.currentPannel = panelMail;

                //TODO Show all the messages received
                String finalString = "";
                ArrayList<String> listOfMessage = this.client.getMessageReceived();

                for(int i = 0; i < listOfMessage.size(); i++){
                    finalString += listOfMessage.get(i);
                    finalString += "--------------------------------------";
                }
                this.resultPanel.setText(finalString);
            }
        }
    }
}
