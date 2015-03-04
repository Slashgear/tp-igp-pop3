package com.polytech4A.pop3.client.core;

import com.polytech4A.pop3.client.ui.MainForm;

public class ClientMain {
    public static void main(String[] args){
        ClientObservable client = new ClientObservable();
        MainForm window = new MainForm(client);
        client.addObserver(window);
    }
}
