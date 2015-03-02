package com.polytech4A.pop3.messages;

import java.util.regex.Pattern;

/**
 * Created by Antoine on 01/03/15.
 *
 * @author Antoine
 * @version 1.0
 *          <p/>
 *          Basic Message for POP3 Exchange.
 */
public class Message {

    /**
     * Message.
     */
    protected StringBuffer message;

    /**
     * Regular expression for the message.
     */
    private static String regex = initializeRegex();

    /**
     * Static Block for initializing the regulalr expression.
     *
     * @return regex.
     */
    private static String initializeRegex() {
        return "";
    }

    /**
     * Construtor of the Message.
     */
    public Message() {
        this.message = new StringBuffer();
    }

    /**
     * Static function for testing if a message have the structure of the Message.
     *
     * @param text Message to test.
     * @return true/false
     */
    public static boolean matches(String text) {
        String regex = "";
        return Pattern.matches(regex, text);
    }


    @Override
    public String toString() {
        return message.toString();
    }


}
