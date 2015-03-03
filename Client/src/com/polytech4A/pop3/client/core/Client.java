package com.polytech4A.pop3.client.core;

import java.util.Observable;

/**
 * Main class for the client
 */
public class Client extends Observable implements Runnable {
    private ClientConnection connection;

    public Client() {
        this.connection = new ClientConnection();
    }

    @Override
    public void run() {

    }
}
