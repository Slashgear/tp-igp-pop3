package com.polytech4A.pop3.messages;

import com.polytech4A.pop3.messages.Exceptions.MalFormedMessageException;

import java.util.regex.Pattern;

/**
 * Created by Antoine on 07/03/2015.
 *
 * @author Antoine
 * @version 1.0
 *          <p/>
 *          Password message in POP3 protocol in order to send your password to the server.
 */
public class PassMessage extends Message {
    /**
     * User password.
     */
    private String password;

    /**
     * Getter of password.
     *
     * @return passord.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Parse the String or create the message if text contain a name.
     *
     * @param text message to parse or password.
     */
    public PassMessage(String text) throws MalFormedMessageException {
        if (PassMessage.matches(text)) {
            this.password = text.split(" ")[1];
            construct();
        } else {
            if (Pattern.matches("\\S*", text)) {
                this.password = text;
                construct();
            } else {
                throw new MalFormedMessageException("Not a valid name for Passmessage expected : \\S*");
            }
        }
    }

    /**
     * Construct the message respecting the pattern.
     */
    private void construct() {
        this.message.append("PASS ");
        this.message.append(password);
    }

    /**
     * Static function for testing if a message have the structure of the PASSMessage.
     *
     * @param text Message to test.
     * @return true/false
     */
    public static boolean matches(String text) {
        String regex = "^PASS \\S*$";
        return Pattern.matches(regex, text);
    }
}
