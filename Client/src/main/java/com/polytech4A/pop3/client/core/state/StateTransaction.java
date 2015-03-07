package com.polytech4A.pop3.client.core.state;

/**
 * Created by Pierre on 07/03/2015.
 */

/**
 * State when the client will receive the mail from the server and show them to the user
 */
public class StateTransaction extends State {
    private int numberOfMessages;

    public int getNumberOfMessages() {
        return numberOfMessages;
    }

    public void setNumberOfMessages(int numberOfMessages) {
        this.numberOfMessages = numberOfMessages;
    }

    public StateTransaction(){
    }


    @Override
    public boolean analyze(String message) {
        return false;
    }

    @Override
    public void action() {

    }
}