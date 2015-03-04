package com.polytech4A.pop3.client.core;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Connection class for the client
 */
public class ClientConnection{
    private Socket socket;
    private BufferedOutputStream bufferedOutputStream;
    private BufferedInputStream bufferedInputStream;

    public ClientConnection(){
    }

    public ClientConnection(InetAddress address, int port){
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
     * @param address   IP Adress of the server to reach
     */
    private void createConnection(InetAddress address, int port) {
        try {
            this.socket = new Socket(address, port);
            InputStream inputStream = this.getSocket().getInputStream();
            OutputStream outputStream = this.getSocket().getOutputStream();

            this.bufferedOutputStream = new BufferedOutputStream(outputStream);
            this.bufferedInputStream = new BufferedInputStream(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
