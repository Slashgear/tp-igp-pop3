package com.polytech4A.pop3.client.ui;

import com.polytech4A.pop3.client.core.Client;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

/**
 * Class for the main window of the client application
 */
public class MainForm extends javax.swing.JFrame implements Observer {
    private JPanel panel1;
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

        validerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
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
    };

    @Override
    public void update(Observable o, Object arg) {

    }
}
