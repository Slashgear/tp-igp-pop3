package com.polytech4A.pop3.client.core.state;

import com.polytech4A.pop3.messages.ApopMessage;
import com.polytech4A.pop3.messages.OkMessages.OkApopMessage;

/**
 * Created by Pierre on 04/03/2015.
 */

public class StateAuthentication extends State {
    private int numberOfTries;

    public StateAuthentication(){
        this.numberOfTries = 0;
    }

    public void setAuthenticationMessage(String user, String password){
        ApopMessage message = new ApopMessage(user, password);
        this.setMsgToSend(message.toString());
    }

    public int getNumberOfTries(){
        return this.numberOfTries;
    }

    @Override
    public boolean analyze(String message) {
        Boolean response = false;
        response = OkApopMessage.matches(message);
        return response;
    }

    @Override
    public void action() {
        this.setNextState(new StateTransaction());
    }
}
