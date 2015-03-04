package com.polytech4A.pop3.client.core.state;

/**
 * Created by Pierre on 04/03/2015.
 */

/*
 * State connected of the client: waiting for the Welcome message of the server and will goo to next state (Authentication)
 */
public class StateStarted extends State{
    //if message = "OK $nomServer POP3 server ready"
    //go to next state

    public StateStarted(){
    }


    @Override
    public boolean analyze(String message) {
        return false;
    }

    @Override
    public void action() {

    }
}
