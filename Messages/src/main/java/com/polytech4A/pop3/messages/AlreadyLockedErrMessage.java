package com.polytech4A.pop3.messages;

/**
 * Created by Antoine on 05/03/2015.
 *
 * @author Antoine
 * @version 1.0
 *          <p/>
 *          Error Message for Already locked mail repository.
 *          Signify the user is already connected to his mailbox.
 */
public class AlreadyLockedErrMessage extends ErrMessage {

    /**
     * Constructor of AlreadyLockMessage.
     */
    public AlreadyLockedErrMessage() {
        super(" Already locked mailbox");
    }

    /**
     * Static function for testing if a message have the structure of the AlreadyLockErrMessage.
     *
     * @param text Message to test.
     * @return true/false
     */
    public static boolean matches(String text) {
        return text.matches("^\\-ERR Already locked mailbox$");
    }
}
