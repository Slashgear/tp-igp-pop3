package com.polytech4A.pop3.client.core;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Connection class for the client
 */
public class ClientConnection {
    private static Logger logger = Logger.getLogger(ClientMain.class);

    /**
     * Timeout in seconds
     */
    private static final int TIMEOUT = 5;

    private Socket socket;
    private BufferedOutputStream out;
    private BufferedInputStream in;

    public ClientConnection() {
    }

    public ClientConnection(InetAddress address, int port) throws IOException {
        this.createConnection(address, port);
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedOutputStream getOut() {
        return out;
    }

    public BufferedInputStream getIn() {
        return in;
    }

    /**
     * Initialise the connection with the server and different objects with it, like the input and output streams
     *
     * @param port    Port of the server to reach
     * @param address IP Address of the server to reach
     */
    private void createConnection(InetAddress address, int port) throws IOException {
        try {
            this.socket = new Socket(address, port);
            this.out = new BufferedOutputStream(this.getSocket().getOutputStream());
            this.in = new BufferedInputStream(this.getSocket().getInputStream());

        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * End the connection with the server by closing the socket
     */
    public void closeConnection() throws IOException {
        try {
            this.socket.close();
        } catch (IOException e) {
            throw e;
        }
    }


    /**
     * Send the message to the server through the created streams
     */
    public void sendMessage(String message) throws IOException {
        try {
            logger.info("Client : "+message);
            this.out.write(message.getBytes());
            this.out.flush();
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * Wait for the response from the server and send back the response with a string format
     */
    public String waitForResponse() throws Exception {
        StringBuilder response = new StringBuilder();
        try {
            /* We are going to wait until the response arrived or the timeout expired*/
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {

                }
            }, this.TIMEOUT * 1000);


            response.append(((char) in.read()));
            while (in.available() != 0) {
                response.append(((char) in.read()));
            }

            logger.info("Server : "+response.toString());
        } catch (IOException e) {
            this.logger.error(e.getMessage());
            throw e;
        }
        return response.toString();
    }
}
