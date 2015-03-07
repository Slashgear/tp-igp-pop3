package com.polytech4A.pop3.messages.ErrMessages;

import com.polytech4A.pop3.messages.ErrMessage;

/**
 * Created by Antoine on 07/03/2015.
 *
 * @author Antoine
 * @version 1.0
 *          <p/>
 *          Invalid Password Error message for POP3 protocol.
 */
public class InvalidPassErr extends ErrMessage {
    /**
     * Constructor of InvalidPass with a message in parameter which is parse.
     */
    public InvalidPassErr() {
        super("invalid password");
    }

    /**
     * Static function for testing if a message have the structure of the InvalidPassErr
     *
     * @param text Message to test.
     * @return true/false
     */
    public static boolean matches(String text) {
        return text.matches("^\\-ERR invalid password");
    }
}
