package com.polytech4A.pop3.client.core;

import com.polytech4A.pop3.messages.Exceptions.MalFormedMessageException;
import com.polytech4A.pop3.messages.MailMessage;
import com.sun.xml.internal.ws.encoding.MtomCodec;
import org.apache.log4j.Logger;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Connection class for the client
 */
public class ClientConnection {
    private static Logger logger = Logger.getLogger(ClientMain.class);

    /**
     * Timeout in seconds
     */
    private static final int TIMEOUT = 5;

    private SSLSocket socket;
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
        SocketFactory factory = SSLSocketFactory.getDefault();

        this.socket = (SSLSocket) factory.createSocket(address, port);
        this.socket.setSoTimeout(this.TIMEOUT * 1000);
        String[] suites = {"SSL_DH_anon_WITH_RC4_128_MD5"};
        this.socket.setEnabledCipherSuites(suites);
        this.out = new BufferedOutputStream(this.getSocket().getOutputStream());
        this.in = new BufferedInputStream(this.getSocket().getInputStream());
    }

    /**
     * End the connection with the server by closing the socket
     */
    public void closeConnection() throws IOException {
        this.socket.close();
    }


    /**
     * Send the message to the server through the created streams
     */
    public void sendMessage(String message) throws IOException {
        logger.info("Client : " + message);
        this.out.write(message.getBytes());
        this.out.flush();
    }

    /**
     * Wait for the response from the server and send back the response with a string format
     */
    public String waitForResponse() throws IOException {
        StringBuilder response = new StringBuilder();
        response.append(((char) in.read()));
        while (in.available() != 0) {
            response.append(((char) in.read()));
        }
        logger.info("Server : " + response.toString());
        return response.toString();
    }

    /**
     * Wait for the response containing a mail from the server and send back the response with a string format.
     *
     * @return Response to send, in a String.
     * @throws IOException
     */
    public String waitForMailResponse() throws IOException, MalFormedMessageException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        StringBuilder response = new StringBuilder();
        response.append(reader.readLine());
        MailMessage mailMess = new MailMessage(response.toString());
        char[] buf = new char[mailMess.getSize()];
        reader.read(buf, 0, mailMess.getSize());
        response.append(buf);
        logger.info("Server : " + response.toString());
        return response.toString();

    }
}
