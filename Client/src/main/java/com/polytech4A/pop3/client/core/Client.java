package com.polytech4A.pop3.client.core;

import com.polytech4A.pop3.client.core.state.State;
import com.polytech4A.pop3.client.core.state.StateAuthentication;
import com.polytech4A.pop3.client.core.state.StateStarted;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Observable;

/**
 * Main class for the client
 */
public class Client extends Observable implements Runnable {

    private ClientConnection connection;
    private State currentState;
    private Boolean errorOccurred = false;
    private String lastErrorMessage;

    public Client() {
    }

    public State getCurrentState() {
        return currentState;
    }

    public Boolean getErrorOccurred() {
        return errorOccurred;
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
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
    public void establishConnection(String addressString, int port){
        InetAddress address = null;
        try {
            // TODO Delete les system.out.println
            address = InetAddress.getByName(addressString);
            System.out.println("Etablissement de la connexion");
            this.connection = new ClientConnection(address, port);
            System.out.println("Connexion établie");
            this.currentState = new StateStarted();
            this.processing();
        } catch (Exception e) {
            this.showError(e.getMessage());
        }
    }


    /**
     *
     * @param user String that will contain the mail address of the user
     * @param password String that will contain the password of the user
     */
    public void makeAuthentication(String user, String password){
        if(this.currentState instanceof StateAuthentication){
            //Envoyer à notre état les informations qui lui sont nécessaires
            // que l'état envoie son message
            try {
                this.connection.sendMessage(this.currentState.getMsgToSend());
                this.connection.waitForResponse();

            } catch (Exception e) {
                this.showError(e.getMessage());
            }
        }
    }


    /**
     * Will call the closing of the connection
     */
    public void closeConnection(){
        try {
            this.connection.closeConnection();
            this.connection = null;
            this.currentState = null;
            this.errorOccurred = false;
            this.lastErrorMessage = null;

            //Call to the garbage collector
            System.gc();
        } catch (IOException e) {
            this.showError(e.getMessage());
        }
    }


    @Override
    public void run() {
        this.processing();
    }


    /**
     * Allow the transition between states if the required conditions are OK
     */
    private void processing(){
        String response = null;
        try {
            response = this.connection.waitForResponse();
        } catch (Exception e) {
            this.showError(e.getMessage());
        }

        if(this.currentState.analyze(response)){
            if(!this.currentState.getWaitForTheUser()){
                this.currentState.action();
                this.currentState = this.currentState.getNextState();
                this.updateObservers();
            }
        }
        else{
            //retry or send error
            System.out.println("Erreur");
        }
    }


    /**
     * Will send the error to the view
     * @param message Message to show to the user
     */
    private void showError(String message){
        this.errorOccurred = true;
        this.lastErrorMessage = message;
        this.updateObservers();
    }
}
