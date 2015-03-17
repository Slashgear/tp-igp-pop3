package com.polytech4A.pop3.client.core.state;

/**
 * Created by Pierre on 04/03/2015.
 */
import com.polytech4A.pop3.messages.Exceptions.MalFormedMessageException;
import com.polytech4A.pop3.messages.OkMessages.ServerReadyMessage;
import org.apache.log4j.Logger;


/**
 * State connected of the client: waiting for the Welcome message of the server and will goo to next state (Authentication)
 */
public class StateStarted extends State{
    public StateStarted(){

    }

    private static Logger logger = org.apache.log4j.Logger.getLogger(StateStarted.class);

    private ServerReadyMessage readyMessage;

    @Override
    public boolean analyze(String message) {
        Boolean response = false;
        response = ServerReadyMessage.matches(message);
        if(response) {
            try {
                readyMessage = new ServerReadyMessage(message);
            } catch (MalFormedMessageException e) {
                logger.error("Error while treating Server's first message " + e.getMessage());
            }
        }
        return response;
    }

    @Override
    public void action() {
        this.setNextState(new StateAuthentication(readyMessage.getArpa()));
    }
}
