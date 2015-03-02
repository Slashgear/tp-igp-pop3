package com.polytech4A.pop3.messages;

import java.util.regex.Pattern;

/**
 * Created by Antoine on 01/03/15.
 *
 * @author Antoine
 * @version 1.1
 *          <p/>
 *          Error Message for POP3 exchange.
 */
public class ErrMessage extends Message {

    /**
     * Error message.
     */
    private String errorMessage;

    /**
     * Getter of error message.
     *
     * @return errorMessage.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Constructor of ErrorMessage with a message in parameter which is parse.
     *
     * @param text ErrorMessage to parse.
     */
    public ErrMessage(String text) {
        super();
        if (ErrMessage.matches(text)) {
            this.errorMessage = text.substring(text.indexOf(" ") + 1);
        } else {
            this.errorMessage = text;
        }
        construct();
    }

    /**
     * Procedure which construct the error Messsage.
     */
    private void construct() {
        this.message.append("-ERR ");
        this.message.append(errorMessage);
    }

    /**
     * Static function for testing if a message have the structure of the ErrMessage.
     *
     * @param text Message to test.
     * @return true/false
     */
    public static boolean matches(String text) {
        String regex = "^\\-ERR .*";
        return Pattern.matches(regex, text);
    }
}
