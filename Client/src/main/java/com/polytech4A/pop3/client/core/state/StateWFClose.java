package com.polytech4A.pop3.client.core.state;

/**
 * Created by Pierre on 07/03/2015.
 */

/**
 * State who will wait the last message of the server, just before closing the connection
 */
public class StateWFClose extends State{
    public StateWFClose(){

    }

    @Override
    public boolean analyze(String message) {
        return false;
    }

    @Override
    public void action() {

    }
}
