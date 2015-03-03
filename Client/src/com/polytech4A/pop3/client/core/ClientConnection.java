package com.polytech4A.pop3.client.core;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Connection class for the client
 */
public class ClientConnection{
    private Socket socket;
    private int port;
    private InetAddress address;
    private InputStream inputStream;
    private OutputStream outputStream;
    private BufferedOutputStream bufferedOutputStream;
    private BufferedInputStream bufferedInputStream;

    public ClientConnection(){
    }

    public Socket getSocket() {
        return socket;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int _port) {
        this.port = _port;
    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress _address) {
        this.address = _address;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
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
    public void createConnection(InetAddress address, int port) {
        try {
            this.address = address;
            this.port = port;

            this.socket = new Socket(address, port);
            this.inputStream = this.getSocket().getInputStream();
            this.outputStream = this.getSocket().getOutputStream();

            this.bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
            this.bufferedInputStream = new BufferedInputStream(socket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
