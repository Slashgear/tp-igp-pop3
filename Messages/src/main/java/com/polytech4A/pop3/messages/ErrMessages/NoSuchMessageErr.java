package com.polytech4A.pop3.messages.ErrMessages;

import com.polytech4A.pop3.messages.ErrMessage;

/**
 * Created by Antoine on 07/03/2015.
 * @author Antoine
 * @version 1.0
 *
 * POP3 err when No message was found for a RETR or LIST request.
 */
public class NoSuchMessageErr extends ErrMessage {

    /**
     * Constructor of NoSuchMessageErr with a message in parameter which is parse.
     */
    public NoSuchMessageErr() {
        super("no such message");
    }

    /**
     * Static function for testing if a message have the structure of the NoSuchMessageErr.
     *
     * @param text Message to test.
     * @return true/false
     */
    public static boolean matches(String text) {
        return text.matches("^\\-ERR no such message$");
    }
}
