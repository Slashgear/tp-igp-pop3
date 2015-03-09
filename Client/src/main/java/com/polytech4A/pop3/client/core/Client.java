package com.polytech4A.pop3.client.core;

import com.polytech4A.pop3.client.core.state.State;
import com.polytech4A.pop3.client.core.state.StateAuthentication;
import com.polytech4A.pop3.client.core.state.StateStarted;
import com.polytech4A.pop3.client.core.state.StateTransaction;
import com.polytech4A.pop3.mailmanager.ClientMailManager;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Observable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Main class for the client
 */
public class Client extends Observable implements Runnable {

    private ClientConnection connection;
    private State currentState;
    private Boolean errorOccurred = false;
    private String lastErrorMessage;
    private ArrayList<String> messageReceived;
    private ClientMailManager mailManager;

    public Client() {
    }

    public State getCurrentState() {
        return currentState;
    }

    public ArrayList<String> getMessageReceived() {
        return messageReceived;
    }

    /**
     * it will send true if an error must be shown to the user,
     * but will also set the value to false in case it is recalled
     * @return If an error occurred
     */
    public Boolean getErrorOccurred() {
        if(this.errorOccurred){
            this.errorOccurred = false;
            return true;
        }
        else{
            return false;
        }
    }


    public String getLastErrorMessage() {
        return lastErrorMessage;
    }


    /**
     * Call the update of what must be show in the view
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

        /*Verification of the validity of the inputs*/
        String IP_ADDRESS_PATTERN = "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)" +
                "\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        Matcher matcher = Pattern.compile(IP_ADDRESS_PATTERN).matcher(addressString);

        if(port > 65535 || port < 1 || !matcher.find()){
            this.showError("L'adresse IP ou le port ne sont pas corrects");
        }
        else{
            try {
                // TODO Delete les system.out.println
                address = InetAddress.getByName(addressString);
                System.out.println("Etablissement de la connexion");
                System.out.println(address);
                this.connection = new ClientConnection(address, port);
                System.out.println("Connexion établie");
                this.currentState = new StateStarted();
                this.waitFirstMessage();
                //TODO add something
                this.updateObservers();
            } catch (Exception e) {
                this.showError(e.getMessage());
            }
        }
    }


    /**
     * Wait for the first message of the server
     */
    public void waitFirstMessage(){
        System.out.println("Waiting for first message");
        try {
            String response = this.connection.waitForResponse();
            if(this.currentState.analyze(response)){
                this.currentState.action();
                this.currentState = this.currentState.getNextState();
                this.updateObservers();
            }
        } catch (Exception e) {
            this.showError(e.getMessage());
        }
    }


    /**
     * Call by the view to make the authentication of a client, will send request to the server, wait the response
     * and react to it
     * @param user String that will contain the mail address of the user
     * @param password String that will contain the password of the user
     */
    public void makeAuthentication(String user, String password){
        if(this.currentState instanceof StateAuthentication){
            String response;
            try {
                ((StateAuthentication) this.currentState).setAuthenticationMessage(user, password);
                this.connection.sendMessage(this.currentState.getMsgToSend());
                response = this.connection.waitForResponse();
                if(this.currentState.analyze(response)){
                    this.mailManager = new ClientMailManager(user);
                    this.connection.sendMessage(this.currentState.getMsgToSend());
                    this.currentState.action();
                    this.currentState = this.currentState.getNextState();
                    this.receiveMessages(response);
                }
                else{
                    if(((StateAuthentication) this.currentState).getNumberOfTries() > 2){
                        this.showError("Nombre de tentatives dépassées");
                        this.closeConnection();
                    }
                    else{
                        this.showError("Erreur dans l'adresse mail ou le mot de passe");
                    }
                }
            } catch (Exception e) {
                this.showError(e.getMessage());
            }
        }
    }


    /**
     * Function which manage the reception of the messages from the server
     * @param response Apop message which different parameters which need to be parse
     */
    private void receiveMessages(String response){
        ((StateTransaction)this.currentState).analyseNumberOfMessages(response);
        String toSend = this.currentState.getMsgToSend();
        while(toSend != null){
            try {
                this.connection.sendMessage(toSend);
                String messageReceived = this.connection.waitForResponse();
                //TODO Treatment to do on this received message
                this.newMessageToShow(messageReceived);
                toSend = this.currentState.getMsgToSend();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.currentState.action();
        this.currentState = this.currentState.getNextState();
        this.askForCloseConnection();
    }


    /**
     * After the reception of the last message, we send a last message to the server
     * We close the connection after that
     */
    private void askForCloseConnection(){
        String toSend = this.currentState.getMsgToSend();

        try {
            this.connection.sendMessage(toSend);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            this.currentState.getNextState();
            this.closeConnection();
            this.updateObservers();
        }
    }


    /**
     * Will call the closing of the connection (call at the end or when an important error occurred)
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
     * Notify the view that we received a new message to show
     * @param message The new message we get, it is added to our list
     */
    private void newMessageToShow(String message){
        this.messageReceived.add(message);
        this.updateObservers();
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
