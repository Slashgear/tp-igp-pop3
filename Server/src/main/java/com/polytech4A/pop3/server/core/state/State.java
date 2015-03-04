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
     * @return nextState State.
     */
    public State getNextState() {
        return nextState;
    }

    /**
     * Setter of the next state.
     *
     * @param nextState State.
     */
    public void setNextState(State nextState) {
        this.nextState = nextState;
    }

    /**
     * Getter of the message to send.
     *
     * @return msgToSend String.
     */
    public String getMsgToSend() {
        return msgToSend;
    }

    /**
     * Setter of message to send.
     *
     * @param msgToSend String.
     */
    public void setMsgToSend(String msgToSend) {
        this.msgToSend = msgToSend;
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
    public abstract boolean analyze(String message);

    /**
     * Does the associated action of the State.
     *
     * @param message
     */
    public abstract void action(String message);
}
