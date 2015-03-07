package com.polytech4A.pop3.client.core.state;

/**
 * Created by Pierre on 04/03/2015.
 */

public class StateAuthentication extends State {
    private int numberOfTries;

    public StateAuthentication(){
        this.numberOfTries = 0;
    }

    public void setAuthenticationMessage(String user, String password){
        //Appel au package de création du message
        //this.setMsgToSend();

    }

    @Override
    public boolean analyze(String message) {
        return false;
    }

    @Override
    public void action() {

    }
}
