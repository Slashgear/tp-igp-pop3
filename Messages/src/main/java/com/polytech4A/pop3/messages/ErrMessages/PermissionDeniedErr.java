package com.polytech4A.pop3.messages.ErrMessages;

import com.polytech4A.pop3.messages.ErrMessage;

/**
 * Created by Antoine on 07/03/2015.
 *
 * @author Antoine
 * @version 1.0
 *          <p/>
 *          Permission Denied Error for POP3 protocol.
 */
public class PermissionDeniedErr extends ErrMessage {
    /**
     * Constructor of PermissionDeniedErr with a message in parameter which is parse.
     */
    public PermissionDeniedErr() {
        super("permission denied");
    }

    /**
     * Static function for testing if a message have the structure of the PermissionDeniedErr.
     *
     * @param text Message to test.
     * @return true/false
     */
    public static boolean matches(String text) {
        return text.matches("^\\-ERR permission denied$");
    }
}
