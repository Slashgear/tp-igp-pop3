package com.polytech4A.pop3.client.core;

import com.polytech4A.pop3.client.core.state.State;
import com.polytech4A.pop3.client.core.state.StateStarted;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;

/**
 * Main class for the client
 */
public class Client extends Observable implements Runnable {
    private ClientConnection connection;
    private State currentState;

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
     * Make the connection with the server and call the constructor of the first state (Started)
     */
    public void establishConnection(){
        // TODO : Change adress and port to get them with the view
        InetAddress address = null;
        int port = 500;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        this.connection = new ClientConnection(address, port);
        this.currentState = new StateStarted();
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

    /**
     * Allow the transition between states if the required conditions are OK
     */
    private void processing(){
        String response = this.waitForResponse();

        if(this.currentState.analyze(response)){
            this.currentState.action();
            this.currentState = this.currentState.getNextState();
        }
        else{
            //retry or send error
        }
    }

    /**
     * Will wait for the response and send back the response with a string format
     */
    private String waitForResponse(){
        StringBuilder response = new StringBuilder();
        BufferedInputStream bi = this.connection.getBufferedInputStream();

        try {
            /* We are going to wait until the response arrived */
            /* TODO : Timeout here */
            while (bi.available()==0){

            }
            while (bi.available() != 0) {
                response.append((char) bi.read());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.toString();
    }


}
