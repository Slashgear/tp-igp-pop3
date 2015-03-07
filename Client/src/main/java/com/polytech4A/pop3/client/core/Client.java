package com.polytech4A.pop3.client.core;

import com.polytech4A.pop3.client.core.state.State;
import com.polytech4A.pop3.client.core.state.StateStarted;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Main class for the client
 */
public class Client extends Observable implements Runnable {

    /**
     * Timeout in seconds
     */
    private static final int TIMEOUT = 5;

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
            address = InetAddress.getByName(addressString);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        System.out.println("Etablissement de la connexion");
        this.connection = new ClientConnection(address, port);
        System.out.println("Connexion établie");
        this.currentState = new StateStarted();
        this.processing();
    }


    /**
     *
     * @param user
     * @param password
     */
    public void makeAuthentification(String user, String password){
        //Vérifier qu'on est bien dans l'état
        //Envoyer à notre état les informations qui lui sont nécessaires
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
            this.updateObservers();
        }
        else{
            //retry or send error
            System.out.println("Erreur");
        }
    }


    /**
     * Will wait for the response and send back the response with a string format
     */
    private String waitForResponse(){
        StringBuilder response = new StringBuilder();
        BufferedInputStream bi = this.connection.getBufferedInputStream();

        try {
            /* We are going to wait until the response arrived or the timeout expired*/
            final Boolean[] expired = {false};

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    expired[0] = true;
                }
            }, this.TIMEOUT * 1000);


            while (bi.available()==0 || !expired[0]){

            }

            while (bi.available() != 0) {
                response.append((char) bi.read());
            }

            if(expired[0]){
                System.out.println("Connexion expirée");
                this.showError("Connexion expirée");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.toString();
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
