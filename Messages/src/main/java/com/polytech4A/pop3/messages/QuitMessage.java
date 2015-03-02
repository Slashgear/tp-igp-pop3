package com.polytech4A.pop3.messages;

import java.util.regex.Pattern;

/**
 * Created by Antoine on 01/03/15.
 *
 * @author Antoine
 * @version 1.0
 *          <p/>
 *          QuitMessage for POP3 exchange.
 */
public class QuitMessage extends Message {

    /**
     * Constructor for a Quit Message.
     */
    public QuitMessage() {
        super();
        this.message.append("QUIT");
    }

    /**
     * Static function for testing if a message is a QUIT message.
     *
     * @param text Message to test.
     * @return true/false.
     */
    public static boolean matches(String text) {
        String regex = "^QUIT$";
        return Pattern.matches(regex, text);
    }
}
