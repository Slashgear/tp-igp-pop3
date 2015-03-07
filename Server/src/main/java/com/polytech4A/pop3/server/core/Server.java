package com.polytech4A.pop3.server.core;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Adrien on 02/03/2015.
 *
 * @author Adrien
 * @version 1.0
 *          <p/>
 *          Application server management.
 */
public class Server {

    /**
     * Logger of the server.
     */
    private static Logger logger = Logger.getLogger(Server.class);

    /**
     * Server's socket.
     */
    private ServerSocket socket;

    /**
     * Getter of server's socket.
     *
     * @return socket ServerSocket
     */
    public ServerSocket getSocket() {
        return socket;
    }

    /**
     * Server constructor. Start the server by creating a socket.
     *
     * @param port          Server will listen on this port.
     * @param nbConnections Maximum number of connections allowed on the server.
     */
    public Server(int port, int nbConnections) {
        this.start(port, nbConnections);
    }

    /**
     * Starts the server's socket on the port with nbConnections allowed.
     *
     * @param port          Server will listen on this port.
     * @param nbConnections Maximum number of connections allowed on the server.
     */
    private void start(int port, int nbConnections) {
        try {
            this.socket = new ServerSocket(port, nbConnections);
            logger.info("Starting serveur on port " + port + ".");
        } catch (IOException e) {
            logger.error("Impossible to start server, port may " + port + " be busy");
            logger.error(e.getMessage());
        }
    }

    /**
     * Socket waits for a connection and then starts a thread with the pro
     */
    public void openConnection(Socket socket) {
        Thread connectionThread = new Thread(new Connection(socket));
        connectionThread.start();
    }

    /**
     * Closes the socket.
     */
    public void close() {
        try {
            this.socket.close();
        } catch (IOException e) {
            logger.error("Impossible to close server");
            logger.error(e.getMessage());
        }
    }

    /**
     * Starts the server process and open a connection when socket accepts a connection.
     */
    public void listen() {
        //TODO : Handle a better exit condition.
        boolean listening = true;
        while (listening) {
            try {
                this.openConnection(this.socket.accept());
            } catch (IOException e) {
                logger.error("Error during connection accepting");
                logger.error(e.getMessage());
                listening = false;
            }
        }
    }

}
