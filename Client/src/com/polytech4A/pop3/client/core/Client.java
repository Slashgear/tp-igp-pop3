package com.polytech4A.pop3.client.core;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;

/**
 * Main class for the client
 */
public class Client extends Observable implements Runnable {
    private ClientConnection connection;

    public Client() {
    }

    /**
     * Call the update of what must be show int the view
     */
    private void updateObservers() {
        setChanged();
        notifyObservers();
    }

    /**
     * Make the connection with the server
     */
    public void establishConnection(){
        //TO DO Change adress and port to get them with the view
        InetAddress address = null;
        int port = 500;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        this.connection = new ClientConnection(address, port);
    }

    /**
     * Will call the closing of the conection
     */
    public void closeConnection(){
        this.connection.closeConnection();
    }


    @Override
    public void run() {
        this.processing();
    }

    private void processing(){

    }
}
