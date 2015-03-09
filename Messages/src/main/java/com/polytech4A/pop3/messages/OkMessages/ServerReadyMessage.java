package com.polytech4A.pop3.messages.OkMessages;

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
     * @param text message to parse.
     */
    public ServerReadyMessage(String text) {
        super();
        String[] array;
        if (ServerReadyMessage.matches(text)) {
             array= text.split(" ");
            this.serverName =array[1];
            construct();
        } else {
            array= text.split(" ");
            this.serverName = array[0];
            construct();

        }
    }

    /**
     * Procedure for the construction of the ServerReadyMessage.
     */
    private void construct() {
        this.message.append(" ");
        this.message.append(serverName);
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
