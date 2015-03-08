package com.polytech4A.pop3.client.core.state;

/**
 * Created by Pierre on 07/03/2015.
 */

import com.polytech4A.pop3.messages.Exceptions.MalFormedMessageException;
import com.polytech4A.pop3.messages.OkMessages.OkApopMessage;
import com.polytech4A.pop3.messages.RetrMessage;

/**
 * State when the client will receive the mail from the server and show them to the user
 */
public class StateTransaction extends State {
    private int numberOfMessages;
    private int lastSentMessage;

    public int getNumberOfMessages() {
        return numberOfMessages;
    }

    public StateTransaction(){
        this.numberOfMessages = 0;
        this.lastSentMessage = 1;
    }

    public String getMsgToSend(){
        if(this.lastSentMessage <= this.numberOfMessages){
            String toSend = new RetrMessage(this.lastSentMessage).toString();
            this.lastSentMessage ++;
            return toSend;
        }
        else{
            return null;
        }
    }

    public int analyseNumberOfMessages(String message){
        try {
            OkApopMessage apopMessage = new OkApopMessage(message);
            this.numberOfMessages = apopMessage.getNbMessage();
        } catch (MalFormedMessageException e) {
            //TODO Handle the error
        }
        return this.numberOfMessages;
    }

    @Override
    public boolean analyze(String message) {
        return false;
    }

    @Override
    public void action() {
        this.setNextState(new StateWFClose());
    }
}