package com.polytech4A.pop3.messages;

import com.polytech4A.pop3.messages.Exceptions.MalFormedMessageException;

import java.util.regex.Pattern;

/**
 * Created by Antoine on 01/03/15.
 *
 * @author Antoine
 * @version 1.1
 *          <p/>
 *          Message of ApopMessage for POP3 exchange.
 */
public class ApopMessage extends Message {

    /**
     * Name of User
     */
    private String id;

    /**
     * Password of user.
     */
    private String password;

    /**
     * Getter of Id.
     *
     * @return Name String.
     */
    public String getId() {
        return id;
    }

    /**
     * Getter of Password.
     *
     * @return Password String.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Constructor who create a  APOP message and who parse informations in the text.
     *
     * @param text ApopMessage in String
     * @throws com.polytech4A.pop3.messages.Exceptions.MalFormedMessageException
     */
    public ApopMessage(String text) throws MalFormedMessageException {
        if (ApopMessage.matches(text)) {
            String[] array = text.split(" ");
            this.id = array[1];
            this.password = array[2];
            contructMessage();
        }else{
            throw new MalFormedMessageException("ApopMessage MalFormed : expected^APOP \\S* \\S*$)");
        }

    }

    /**
     * Constructor who create a ApopMessage from parameters.
     *
     * @param id       Name
     * @param password Password in String
     */
    public ApopMessage(String id, String password) {
        super();
        this.id = id;
        this.password = password;
        contructMessage();
    }

    /**
     * Procédure who create the message.
     */
    private void contructMessage() {
        this.message.append("APOP ");
        this.message.append(this.id);
        this.message.append(" ");
        this.message.append(this.password);
    }

    /**
     * Static function for testing if a message have the structure of the ApopMessage.
     *
     * @param text Text to test.
     * @return true/false
     */
    public static boolean matches(String text) {
        String regex = "^APOP \\S* \\S*$";
        return Pattern.matches(regex, text);
    }
}
