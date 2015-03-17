package com.polytech4A.pop3.messages.OkMessages;

import com.polytech4A.pop3.messages.Exceptions.MalFormedMessageException;
import com.polytech4A.pop3.messages.OkMessage;

import java.util.regex.Pattern;

/**
 * Created by Antoine on 01/03/15.
 *
 * @author Antoine
 * @version 1.1
 *          <p/>
 *          Server ready message for POP3 exchange.
 */
public class ServerReadyMessage extends OkMessage {

    /**
     * Server name.
     */
    private String serverName;

    /**
     * Server Arpa internet Text Message defined in the RFC 822.
     */
    private String arpa;

    /**
     * Getter of Arpa.
     *
     * @return String Arpa.
     */
    public String getArpa() {
        return arpa;
    }

    /**
     * Getter of the server name.
     *
     * @return servername String
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * Constructor of a ServerReadyMessage with a message in parameter to parse.
     *
     * @param message to parse.
     */
    public ServerReadyMessage(String message) throws MalFormedMessageException {
        super();
        String[] array;
        if (ServerReadyMessage.matches(message)) {
            array = message.split(" ");
            this.serverName = array[1];
            this.arpa = message.split(serverName+" ")[1];
            construct();
        } else{
            throw new MalFormedMessageException("ServerReadyMessage is not correct expected: \"^\\+OK \\S* \\S*");
        }
    }


    public ServerReadyMessage(String serverName, String arpa) {
        this.serverName = serverName;
        this.arpa = arpa;
        construct();
    }

    /**
     * Procedure for the construction of the ServerReadyMessage.
     */
    private void construct() {
        this.message.append(" ");
        this.message.append(serverName);
        this.message.append(" ");
        this.message.append(arpa);
    }

    /**
     * Static function for testing if the message in parameter has the structure of the ServerReadyMessage.
     *
     * @param text Message to test.
     * @return true/false.
     */
    public static boolean matches(String text) {
        String regex = "^\\+OK \\S* .*$";
        return Pattern.matches(regex, text);
    }
}
