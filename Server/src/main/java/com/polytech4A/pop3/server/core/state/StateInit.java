package com.polytech4A.pop3.server.core.state;

import com.polytech4A.pop3.messages.OkMessages.ServerReadyMessage;

/**
 * Created by Adrien on 02/03/2015.
 *
 * @author Adrien
 * @version 1.0
 *          <p/>
 *          Initial state of the server.
 */
public class StateInit extends State {
    /**
     * Initialization state constructor.
     */
    public StateInit() {
        super();
        this.setNextState(new StateAuth());
        //TODO: handle server name by getting a const set in an other class.
        this.setMsgToSend(new ServerReadyMessage("serverName").toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean analyze(String message) {
        return true;
    }

}
