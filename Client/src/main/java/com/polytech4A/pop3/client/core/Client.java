package com.polytech4A.pop3.client.core;

import com.polytech4A.pop3.client.core.state.State;
import com.polytech4A.pop3.client.core.state.StateAuthentication;
import com.polytech4A.pop3.client.core.state.StateStarted;
import com.polytech4A.pop3.client.core.state.StateTransaction;
import com.polytech4A.pop3.mailmanager.ClientMailManager;
import org.apache.log4j.Logger;

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
    private static Logger logger = Logger.getLogger(ClientMain.class);

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
        this.logger.debug("Asking for the update of the view");
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

                logger.info("Trying to connect");
                this.connection = new ClientConnection(address, port);
                logger.info("Connection established");
                this.currentState = new StateStarted();
                this.waitFirstMessage();
                this.updateObservers();
            } catch (Exception e) {
                logger.error("Connection refused");
                this.showError("Connexion refusée, vérifiez vos paramètres");
            }
        }
    }


    /**
     * Wait for the first message of the server
     */
    public void waitFirstMessage(){
        this.logger.debug("Waiting for first message");
        try {
            String response = this.connection.waitForResponse();
            if(this.currentState.analyze(response)){
                this.currentState.action();
                this.currentState = this.currentState.getNextState();
                this.updateObservers();
            }
        } catch (Exception e) {
            logger.error("Cannot receive the first message from the server");
            this.showError("Ne reçoit pas la réponse du serveur");
            this.closeConnection();
        }
    }


    /**
     * Call by the view to make the authentication of a client, will send request to the server, wait the response
     * and react to it
     * @param user String that will contain the mail address of the user
     * @param password String that will contain the password of the user
     */
    public void makeAuthentication(String user, String password){
        this.logger.debug("Attempt to authenticate");
        if(this.currentState instanceof StateAuthentication){
            String response;
            try {
                ((StateAuthentication) this.currentState).setAuthenticationMessage(user, password);
                this.connection.sendMessage(this.currentState.getMsgToSend());
                response = this.connection.waitForResponse();
                if(this.currentState.analyze(response)){
                    /* Addition of the mail manager */
                    this.mailManager = new ClientMailManager(user);
                    this.connection.sendMessage(this.currentState.getMsgToSend());
                    this.currentState.action();
                    this.currentState = this.currentState.getNextState();
                    this.receiveMessages(response);
                }
                else{
                    String errorReceived = ((StateAuthentication) this.currentState).getErrorReceived();
                    if(errorReceived != null){
                        /* There is an error we must know which one */
                        if(errorReceived == "NoMailBoxErr"){
                            this.showError("Erreur dans l'adresse mail ou le mot de passe");
                            logger.error("Error from the server: NoMailBoxErr");
                        }
                        if(errorReceived == "PermissionDeniedErr"){
                            this.showError("Le nombre de tentative est dépassé");
                            logger.error("Error from the server: PermissionDeniedErr");
                            this.closeConnection();
                        }
                        if(errorReceived == "AlreadyLockedErrMessage"){
                            this.showError("L'utilisateur est déjà connecté");
                            logger.error("Error from the server: AlreadyLockedErrMessage");
                            this.closeConnection();
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Cannot send message of authentication");
                this.showError("L'authentification n'a pas pu aboutir, veuillez vous reconnecter");
                this.closeConnection();
            }
        }
    }


    /**
     * Function which manage the reception of the messages from the server
     * @param response Apop message which different parameters which need to be parse
     */
    private void receiveMessages(String response){
        int i = 1;
        this.logger.debug("Reception of the mails from the server");
        ((StateTransaction)this.currentState).analyseNumberOfMessages(response);
        String toSend = this.currentState.getMsgToSend();
        while(toSend != null){
            try {
                this.connection.sendMessage(toSend);
                String messageReceived = this.connection.waitForResponse();

                /*Will cut the string because we receive two parts: OK number_of_octets + the mail*/
                int endFirstMessage = messageReceived.indexOf('\n');
                String mailReceived = messageReceived.substring(0, endFirstMessage);

                /* TODO ajouter les mails à l'utilisateur */

                this.newMessageToShow(mailReceived);
                this.logger.debug("Reception of message " + i);
                toSend = this.currentState.getMsgToSend();
                i++;
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
        this.logger.debug("Asking for closing the connection");

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

            this.updateObservers();
            this.logger.debug("Connection closed");
        } catch (IOException e) {
            logger.error("Error during the closing of the connection");
            this.showError("Erreur pendant la fermeture de la connexion");
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
        //TODO Remove that because it's useless
        /*String response = null;
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
            logger.error("");
        }*/
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
