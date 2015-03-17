package com.polytech4A.pop3.client.core.state;

import com.polytech4A.pop3.messages.ApopMessage;
import com.polytech4A.pop3.messages.ErrMessages.AlreadyLockedErrMessage;
import com.polytech4A.pop3.messages.ErrMessages.NoMailBoxErr;
import com.polytech4A.pop3.messages.ErrMessages.PermissionDeniedErr;
import com.polytech4A.pop3.messages.OkMessages.OkApopMessage;

/**
 * Created by Pierre on 04/03/2015.
 */

/**
 * State where we'll try to connect to the server with a mail address and a password
 */
public class StateAuthentication extends State {
    //private int numberOfTries;
    private String errorReceived;

    private String arpa;

    public StateAuthentication(String arpa){
        this.errorReceived = null;
        this.arpa = arpa;
    }

    public String getErrorReceived() {
        return errorReceived;
    }

    /**
     * Will set to the class the username and the string we'll send to the server
     * @param user Username which is a mail address
     * @param password Password
     */
    public void setAuthenticationMessage(String user, String password){
        ApopMessage message = new ApopMessage(user, password, arpa);
        this.setMsgToSend(message.toString());
    }

    @Override
    public boolean analyze(String message) {
        Boolean response = false;

        response = OkApopMessage.matches(message);
        if(response){
            return response;
        }
        else{
            /* Error management */
            if(NoMailBoxErr.matches(message)){
                this.errorReceived = "NoMailBoxErr";
            }
            if(PermissionDeniedErr.matches(message)){
                this.errorReceived = "PermissionDeniedErr";
                this.setNextState(null);
            }
            if(AlreadyLockedErrMessage.matches(message)){
                this.errorReceived = "AlreadyLockedErrMessage";
                this.setNextState(null);
            }
            return false;
        }
    }

    @Override
    public void action() {
        this.setNextState(new StateTransaction());
    }
}
