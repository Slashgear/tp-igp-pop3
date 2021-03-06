package com.polytech4A.pop3.client.core.state;

/**
 * Created by Pierre on 04/03/2015.
 */
import com.polytech4A.pop3.messages.OkMessage;


/**
 * State connected of the client: waiting for the Welcome message of the server and will goo to next state (Authentication)
 */
public class StateStarted extends State{
    public StateStarted(){

    }


    @Override
    public boolean analyze(String message) {
        Boolean response = false;
        response = OkMessage.matches(message);
        return response;
    }

    @Override
    public void action() {
        this.setNextState(new StateAuthentication());
    }
}
