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

/**
 * Main class for the client
 */
public class Client extends Observable{
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
        logger.debug("Asking for the update of the view");
        setChanged();
        notifyObservers();
    }


    /**
     * Make the connection with the server and call the constructor of the first state (Started)
     */
    public void establishConnection(String addressString, int port){
        InetAddress address = null;

        /*Verification of the validity of the inputs*/
       /* String IP_ADDRESS_PATTERN = "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)" +
                "\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        Matcher matcher = Pattern.compile(IP_ADDRESS_PATTERN).matcher(addressString);
        */
        if(port > 65535 || port < 1 /*|| !matcher.find()*/){
            logger.error("Ip address or port unreacheable");
            this.showError("L'adresse IP ou le port ne sont pas corrects");
        }
        else{
            try {
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
        logger.debug("Waiting for first message");
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
        logger.debug("Attempt to authenticate");
        if(this.currentState instanceof StateAuthentication){
            String response;
            try {
                ((StateAuthentication) this.currentState).setAuthenticationMessage(user, password);
                this.connection.sendMessage(this.currentState.getMsgToSend());
                try {
                    response = this.connection.waitForResponse();
                    if (this.currentState.analyze(response)) {
                    /* Addition of the mail manager */
                        this.mailManager = new ClientMailManager(user);
                        this.currentState.action();
                        this.currentState = this.currentState.getNextState();
                        this.receiveMessages(response);
                    } else {
                        String errorReceived = ((StateAuthentication) this.currentState).getErrorReceived();
                        if (errorReceived != null) {
                        /* There is an error from the server: we must know which one it is*/
                            if (errorReceived == "NoMailBoxErr") {
                                this.showError("Erreur dans l'adresse mail ou le mot de passe");
                                logger.error("Error from the server: NoMailBoxErr");
                            }
                            if (errorReceived == "PermissionDeniedErr") {
                                this.showError("Le nombre de tentative est dépassé");
                                logger.error("Error from the server: PermissionDeniedErr");
                                this.closeConnection();
                            }
                            if (errorReceived == "AlreadyLockedErrMessage") {
                                this.showError("L'utilisateur est déjà connecté");
                                logger.error("Error from the server: AlreadyLockedErrMessage");
                                this.closeConnection();
                            }
                        }
                    }
                }
                catch(IOException e){
                    logger.error("Cannot receive confirmation of the message of authentication");
                    this.showError("L'authentification n'a pas pu aboutir, veuillez vous reconnecter");
                    this.closeConnection();
                }
            } catch (IOException e) {
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
        logger.debug("Reception of the mails from the server");
        this.messageReceived = new ArrayList<String>();
        int numberOfMessages = ((StateTransaction)this.currentState).analyseNumberOfMessages(response);

        String toSend = this.currentState.getMsgToSend();
        while(toSend != null){
            try {
                this.connection.sendMessage(toSend);
                String messageReceived = null;
                try {
                    messageReceived = this.connection.waitForMailResponse();
                } catch (Exception e) {
                    logger.error("Cannot receive message from the server");
                    this.showError("Ne peut plus joindre le serveur");
                }

                /*Will cut the string because we receive two parts: OK number_of_octets + the mail*/
                int endFirstMessage = messageReceived.indexOf('\n');
                if(endFirstMessage > 0){
                    String mailReceived = messageReceived.substring(endFirstMessage, messageReceived.length());
                    this.mailManager.addMail(mailReceived);
                    this.newMessageToShow(mailReceived);
                    logger.debug("Reception of message " + i);
                    toSend = this.currentState.getMsgToSend();
                    i++;
                }
                else{
                    logger.error("Error in the message " + i + " from the server");
                    this.showError("Erreur lors de la transmission du mail " + i + " par le serveur");
                }
            }
            catch (IOException e) {
                logger.error("Cannot send RETR message to the server");
                this.showError("Ne peut plus joindre le serveur");
            }
        }
        this.askForCloseConnection();
        if(numberOfMessages == 0){
            this.showError("Vous n'avez pas de nouveaux messages, vous êtes déconnecté");
            this.askForCloseConnection();
        }else {
            this.mailManager.saveMails();
        }
    }


    /**
     * After the reception of the last mail, we send a last message to the server
     * we want to receive an OK message and we close the connection
     */
    private void askForCloseConnection(){
        logger.debug("Asking for closing the connection");

        this.currentState.action();
        this.currentState = this.currentState.getNextState();
        String toSend = this.currentState.getMsgToSend();

        try {
            this.connection.sendMessage(toSend);
            try{
                String response = this.connection.waitForResponse();
                if(!this.currentState.analyze(response)){
                    logger.error("No reception of OK message from the server after QUIT");
                }
            }
            catch(IOException e){
                logger.error("Timeout from the server");
                this.showError("Timeout from the server");
            }
        } catch (IOException e) {
            logger.error("Error for the sending of the QUIT message");
        }
        finally {
            this.currentState.getNextState();
            //this.closeConnection();
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
            this.mailManager = null;
            this.messageReceived = null;

            //Call to the garbage collector
            System.gc();

            this.updateObservers();
            logger.debug("Connection closed");
        } catch (IOException e) {
            logger.error("Error during the closing of the connection");
            this.showError("Erreur pendant la fermeture de la connexion");
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
