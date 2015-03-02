package com.polytech4A.pop3.server.core.state;

/**
 * Created by Adrien on 02/03/2015.
 *
 * @author Adrien
 * @version 1.0
 *          <p/>
 *          State that describes the Server state.
 */
public abstract class State {

    /**
     * Next state in which the object will be.
     */
    private State nextState;
    /**
     * Message to send before going in the next stage.
     */
    private String msgToSend;

    /**
     * Getter of the next state.
     *
     * @return State nextState.
     */
    public State getNextState() {
        return nextState;
    }

    /**
     * Getter of the message to send.
     *
     * @return Message msgToSend.
     */
    public String getMsgToSend() {
        return msgToSend;
    }

    /**
     * Blank constructor.
     */
    public State() {

    }

    /**
     * Analyzes the message and routes the action. Defines next state.
     *
     * @param message String
     */
    public abstract void analyze(String message);

    /**
     * Does the associated action of the State.
     *
     * @param message
     */
    public abstract void action(String message);
}