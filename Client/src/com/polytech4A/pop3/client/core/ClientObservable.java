package com.polytech4A.pop3.client.core;

import java.util.Observable;

/**
 * Main class for the client
 */
public class ClientObservable extends Observable implements Runnable {
    private ClientConnection connection;

    public ClientObservable() {
        this.connection = new ClientConnection();
    }

    private void updateObservers() {
        setChanged();
        notifyObservers();
    }

    @Override
    public void run() {

    }
}
