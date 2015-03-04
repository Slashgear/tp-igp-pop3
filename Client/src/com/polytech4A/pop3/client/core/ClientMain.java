package com.polytech4A.pop3.client.core;

import com.polytech4A.pop3.client.ui.MainForm;

public class ClientMain {
    public static void main(String[] args){
        Client client = new Client();
        MainForm window = new MainForm(client);
        window.setVisible(true);
        client.addObserver(window);
        
        client.establishConnection();
    }
}
