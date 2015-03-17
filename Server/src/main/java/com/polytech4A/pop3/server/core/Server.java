package com.polytech4A.pop3.server.core;

import org.apache.log4j.Logger;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
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
     * Path to server directory.
     */
    public static String SERVER_DIRECTORY = "./";

    /**
     * Server name to send to client.
     */
    public static String SERVER_NAME = "PolytechPOP3";

    /**
     * True if the server have to delete messages at the end of the session.
     */
    public static boolean DEL_MESSAGE;

    /**
     * Server's socket.
     */
    private SSLServerSocket socket;

    /**
     * Getter of server's socket.
     *
     * @return socket ServerSocket
     */
    public SSLServerSocket getSocket() {
        return socket;
    }

    /**
     * Server constructor. Start the server by creating a socket.
     *
     * @param port          Server will listen on this port.
     * @param nbConnections Maximum number of connections allowed on the server.
     */
    public Server(int port, int nbConnections, boolean delMessage) {
        this.DEL_MESSAGE = delMessage;
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
            ServerSocketFactory sslFactory = SSLServerSocketFactory.getDefault();
            this.socket = (SSLServerSocket) sslFactory.createServerSocket(port, nbConnections);
            logger.info("Starting server on port " + port + ".");
        } catch (IOException e) {
            logger.error("Impossible to start server, port may " + port + " be busy");
            logger.error(e.getMessage());
        }
    }

    /**
     * Socket waits for a connection and then starts a thread with the pro
     */
    public void openConnection(SSLSocket socket) {
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
        boolean listening = true;
        logger.info("LISTENING... ...");
        while (listening) {
            try {
                this.openConnection((SSLSocket) this.socket.accept());
            } catch (IOException e) {
                logger.error("Error during connection accepting");
                logger.error(e.getMessage());
                listening = false;
            }
        }
    }

}
