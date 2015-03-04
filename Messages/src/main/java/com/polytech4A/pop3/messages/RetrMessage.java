package com.polytech4A.pop3.messages;

import com.polytech4A.pop3.messages.Exceptions.MalFormedMessageException;

import java.util.regex.Pattern;


/**
 * Created by Antoine on 01/03/15.
 *
 * @author Antoine
 * @version 1.1
 *          <p/>
 *          RETR message for POP3 exchange.
 */
public class RetrMessage extends Message {

    /**
     * Numero of the message.
     */
    private Integer noMessages;

    /**
     * Getter of NoMessage.
     *
     * @return noMessage
     */
    public Integer getNoMessages() {
        return noMessages;
    }

    /**
     * Constructor of Retr Message with numero of message.
     *
     * @param noMessages
     */
    public RetrMessage(Integer noMessages) {
        this.noMessages = noMessages;
        construct();
    }

    /**
     * Constructor of Retr message with a String to parse in parameter.
     *
     * @param text message to parse.
     * @throws com.polytech4A.pop3.messages.Exceptions.MalFormedMessageException
     */
    public RetrMessage(String text) throws MalFormedMessageException {
        if (RetrMessage.matches(text)) {
            String s = text.split(" ")[1];
            this.noMessages = Integer.parseInt(s);
            construct();
        } else {
            throw new MalFormedMessageException("RETR message Malformed : expected ^RETR \\d.*$");
        }
    }

    /**
     * Procedur of construction of Retr message.
     */
    private void construct() {
        this.message.append("RETR ");
        this.message.append(noMessages.toString());
    }

    /**
     * Static function for testing if a message have the structure of Retr message.
     *
     * @param text message to test.
     * @return true/false
     */
    public static boolean matches(String text) {
        String regex = "^RETR \\d.*$";
        return Pattern.matches(regex, text);
    }
}
