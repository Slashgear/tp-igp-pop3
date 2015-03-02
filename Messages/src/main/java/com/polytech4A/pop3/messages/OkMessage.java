package com.polytech4A.pop3.messages;

import java.util.regex.Pattern;

/**
 * Created by Antoine on 01/03/15.
 *
 * @author Antoine
 * @version 1.0
 *          <p/>
 *          OkMessage for POP3 exchange.
 */
public class OkMessage extends Message {

    /**
     * Contructor of OkMessage.
     */
    public OkMessage() {
        super();
        this.message.append("+OK");
    }

    /**
     * Static function for testing if a messsage have the OkMessage.
     *
     * @param text message to test.
     * @return true/false
     */
    public static boolean matches(String text) {
        String regex = "^\\+OK.*$";
        return Pattern.matches(regex, text);
    }
}
