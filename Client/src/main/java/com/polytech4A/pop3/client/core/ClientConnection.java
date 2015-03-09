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
public class ClientConnection{
    private static Logger logger = Logger.getLogger(ClientMain.class);

    /**
     * Timeout in seconds
     */
    private static final int TIMEOUT = 5;

    private Socket socket;
    private BufferedOutputStream bufferedOutputStream;
    private BufferedInputStream bufferedInputStream;

    public ClientConnection(){
    }

    public ClientConnection(InetAddress address, int port) throws IOException {
        this.createConnection(address, port);
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedOutputStream getBufferedOutputStream() {
        return bufferedOutputStream;
    }

    public BufferedInputStream getBufferedInputStream() {
        return bufferedInputStream;
    }

    /**
     * Initialise the connection with the server and different objects with it, like the input and output streams
     *
     * @param port      Port of the server to reach
     * @param address   IP Address of the server to reach
     */
    private void createConnection(InetAddress address, int port) throws IOException {
        try {
            this.socket = new Socket(address, port);
            InputStream inputStream = this.getSocket().getInputStream();
            OutputStream outputStream = this.getSocket().getOutputStream();

            this.bufferedOutputStream = new BufferedOutputStream(outputStream);
            this.bufferedInputStream = new BufferedInputStream(inputStream);

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
            this.bufferedOutputStream.write(message.getBytes());
            this.bufferedOutputStream.flush();
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * Wait for the response from the server and send back the response with a string format
     */
    public String waitForResponse() throws Exception {
        System.out.println("Pouet");
        StringBuilder response = new StringBuilder();
        BufferedInputStream bi = this.bufferedInputStream;

        try {
            /* We are going to wait until the response arrived or the timeout expired*/
            final Boolean[] expired = {false};

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    expired[0] = true;
                }
            }, this.TIMEOUT * 1000);


            while (bi.available()==0 || !expired[0]){

            }

            while (bi.available() != 0) {
                response.append((char) bi.read());
            }

            if(expired[0]){
                this.logger.error("Connexion expired");
                throw new Exception("Connexion expired");
            }
        } catch (IOException e) {
            this.logger.error(e.getMessage());
            throw e;
        }

        this.logger.debug(response.toString());
        return response.toString();
    }
}
