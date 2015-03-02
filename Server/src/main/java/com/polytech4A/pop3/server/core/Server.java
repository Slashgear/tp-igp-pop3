package com.polytech4A.pop3.server.core;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;

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
    //TODO : Develop a local logger in file. And then in html page.
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
     * Starts the server's socket on the port with nbConnections allowed.
     *
     * @param port          Server port number.
     * @param nbConnections Number of connections allowed on the server.
     */
    public void start(int port, int nbConnections) {
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
    public void openConnection() {
        try {
            this.socket.accept();
        } catch (IOException e) {
            logger.error("Impossible to open a connection");
            logger.error(e.getMessage());
        }

    }

    /**
     * Close the socket.
     */
    public void close() {
        try {
            this.socket.close();
        } catch (IOException e) {
            logger.error("Impossible to close server");
            logger.error(e.getMessage());
        }
    }

}
