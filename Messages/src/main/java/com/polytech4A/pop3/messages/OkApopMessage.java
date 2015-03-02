package com.polytech4A.pop3.messages;

import com.polytech4A.pop3.messages.Exceptions.MalFormedMessageException;

import java.util.regex.Pattern;

/**
 * Created by Antoine on 01/03/15.
 *
 * @author Antoine
 * @version 1.0
 *          <p/>
 *          Message for a OkApopMessage.
 */
public class OkApopMessage extends OkMessage {

    /**
     * Id of User.
     */
    private String id;

    /**
     * Number of messages.
     */
    private Integer nbMessage;

    /**
     * Size of all messages.
     */
    private Integer messageSize;

    /**
     * Getter of Id.
     *
     * @return id String
     */
    public String getId() {
        return id;
    }

    /**
     * Getter of NbMessage.
     *
     * @return nbMessage Integer
     */
    public Integer getNbMessage() {
        return nbMessage;
    }

    /**
     * Getter of MessageSize.
     *
     * @return MessageSize Integer
     */
    public Integer getMessageSize() {
        return messageSize;
    }

    /**
     * Constructor who create a Ok for APOP message and who parse informations in the text.
     *
     * @param text OkApopMessage
     * @throws com.polytech4A.pop3.messages.Exceptions.MalFormedMessageException
     */
    public OkApopMessage(String text) throws MalFormedMessageException {
        super();
        if (OkApopMessage.matches(text)) {
            this.id = text.split(" ")[1].split("'")[0];
            String nbMessage = text.split("has ")[1].split(" ")[0];
            this.nbMessage = Integer.parseInt(nbMessage);
            String messageSize = text.split("\\(")[1].split(" octets\\)")[0];
            this.messageSize = Integer.parseInt(messageSize);
            construct();
        } else {
            throw new MalFormedMessageException("Ok for Apop Message  Malformed : Expected ^\\+OK \\S*'s maildrop has \\d* messages \\(\\d* octets\\)$");
        }
    }

    /**
     * Constructor who create ok OK Apop Message from informations in parameters.
     *
     * @param id          Name of User.
     * @param nbMessage   Number of Messages.
     * @param messageSize Size of Messages.
     */
    public OkApopMessage(String id, Integer nbMessage, Integer messageSize) {
        super();
        this.id = id;
        this.nbMessage = nbMessage;
        this.messageSize = messageSize;
        construct();
    }

    /**
     * Procédure who create the OkApopMessage.
     */
    private void construct() {
        this.message.append(" ");
        this.message.append(id);
        this.message.append("'s maildrop has ");
        this.message.append(nbMessage);
        this.message.append(" messages (");
        this.message.append(messageSize);
        this.message.append(" octets)");
    }

    /**
     * Static function for testing if a message have the structure of the OkApopMessage.
     *
     * @param text message to test.
     * @return true/false.
     */
    public static boolean matches(String text) {
        String regex = "^\\+OK \\S*'s maildrop has \\d* messages \\(\\d* octets\\)$";
        return Pattern.matches(regex, text);
    }
}
