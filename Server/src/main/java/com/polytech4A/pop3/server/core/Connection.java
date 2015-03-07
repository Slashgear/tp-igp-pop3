package com.polytech4A.pop3.server.core;

import com.polytech4A.pop3.server.core.state.State;
import com.polytech4A.pop3.server.core.state.StateInit;
import org.apache.log4j.Logger;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by Adrien on 02/03/2015.
 *
 * @author Adrien
 * @version 1.0
 *          <p/>
 *          Connection management between client and server.
 */
public class Connection implements Runnable {

    /**
     * Logger of the server.
     */
    private static Logger logger = Logger.getLogger(Server.class);

    /**
     * Socket when the connection was established.
     */
    private Socket socket;

    /**
     * Current state of the connection.
     */
    private State state;

    /**
     * Output stream towards client.
     */
    private BufferedOutputStream out;
    /**
     * Input stream incoming from the client.
     */
    private BufferedReader in;

    /**
     * Constructor of the connection. It will be in initialization state. Initialize the two streams (output and input).
     */
    public Connection(Socket socket) {
        try {
            this.socket = socket;
            this.state = new StateInit();
            this.out = new BufferedOutputStream(socket.getOutputStream());
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            logger.error("Error while creating the connection.");
            logger.error(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        processing();
    }

    public void processing() {
        boolean runConnection = true;
        //Server is in init state. We have to send the first message and then update in authorization state.
        sendMessage();
        updateState();
        while(runConnection) {
            String message = "";
            try {
                message = in.readLine();
                if (runConnection = state.analyze(message)) { //route the request to next state of the server
                    updateState();
                    sendMessage();
                }
            } catch (IOException e) {
                logger.error("Can't read incoming input from client.");
                logger.error(e.getMessage());
                runConnection = false;
            }
        }
        //TODO : Stop the connection
    }

    /**
     * Update the connection current state.
     */
    public void updateState() {
        if(state.getNextState() == null) {
            throw new NullPointerException("Next state is set to null");
        } else {
            this.state = state.getNextState();
        }
    }

    /**
     * Send the message to the client.
     */
    public void sendMessage(){
        try {
            out.write(state.getMsgToSend().getBytes());
        } catch (IOException e) {
            logger.error("Error during writing through server output\n" + e.getMessage());
        }
    }

}
