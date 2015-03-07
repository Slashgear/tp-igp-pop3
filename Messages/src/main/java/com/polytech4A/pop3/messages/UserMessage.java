package com.polytech4A.pop3.messages;

import com.polytech4A.pop3.messages.Exceptions.MalFormedMessageException;

import java.util.regex.Pattern;

/**
 * Created by Antoine on 07/03/2015.
 *
 * @author Antoine
 * @version 1.0
 *          <p/>
 *          User message in POP3 protocol in order to send your username to the server.
 */
public class UserMessage extends Message {


    /**
     * User name.
     */
    private String id;

    /**
     * Getter of the user name.
     *
     * @return user id.
     */
    public String getId() {
        return id;
    }

    /**
     * Parse the String or create the message if text contain a name.
     *
     * @param text
     */
    public UserMessage(String text) throws MalFormedMessageException {
        if (UserMessage.matches(text)) {
            this.id = text.split(" ")[1];
            construct();
        } else {
            if (Pattern.matches("\\S*", text)) {
                this.id = text;
                construct();
            } else {
                throw new MalFormedMessageException("Not a valid name for Usermessage expected : \\S*");
            }
        }
    }

    /**
     * Construct the message respecting the pattern.
     */
    private void construct() {
        this.message.append("USER ");
        this.message.append(id);
    }

    /**
     * Static function for testing if a message have the structure of the UserMessage.
     *
     * @param text Message to test.
     * @return true/false
     */
    public static boolean matches(String text) {
        String regex = "^USER \\S*$";
        return Pattern.matches(regex, text);
    }
}
