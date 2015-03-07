package com.polytech4A.pop3.messages.ErrMessages;

import com.polytech4A.pop3.messages.ErrMessage;

/**
 * Created by Antoine on 07/03/2015.
 *
 * @author Antoine
 * @version 1.0
 *          <p/>
 *          Err Launched by server if no user was found in the Server.
 */
public class NoMailBoxErr extends ErrMessage {
    /**
     * Construct the NoMailBox Err
     *
     * @param user Username
     */
    public NoMailBoxErr(String user) {
        super("sorry, no mailbox for " + user + " here");
    }

    /**
     * Static function for testing if a message have the structure of the NoMailBoxErr.
     *
     * @param text Message to test.
     * @return true/false
     */
    public static boolean matches(String text) {
        return text.matches("^\\-ERR sorry, no mailbox for \\S* here$");
    }
}
