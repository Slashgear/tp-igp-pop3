package com.polytech4A.pop3.messages;

import com.polytech4A.pop3.messages.Exceptions.MalFormedMessageException;

/**
 * Created by Antoine on 01/03/15.
 *
 * @author Antoine
 * @version 1.0
 *          <p/>
 *          Mail message for POP3 exchange.
 */
public class MailMessage extends OkMessage {

    /**
     * Size of the message in Bytes.
     */
    private Integer size;

    /**
     * Getter of the size in Bytes.
     * @return
     */
    public Integer getSize() {
        return size;
    }

    /**
     * Constructor who Parse the message not null.
     * @param text not null message.
     * @throws MalFormedMessageException
     */
    public MailMessage(String text) throws MalFormedMessageException {
        if (MailMessage.matches(text)) {
            this.size = Integer.parseInt(text.split(" ")[1]);
            this.construct();
        } else {
            throw new MalFormedMessageException("Mail Message malformed expected : \\+OK \\d* octets.* ");
        }
    }

    /**
     * Constructor of Mail Message Response to a RETR message in POP3 protocol.
     * @param size
     */
    public MailMessage( Integer size) {
        super();
        this.size = size;
        construct();
    }

    /**
     * Construct the message structure.
     */
    private void construct() {
        this.message.append(" ");
        this.message.append(size);
        this.message.append(" octets");
    }

    /**
     * Static function for testing if a message have the structure of the MailMessage.
     *
     * @param text Message to test not null.
     * @return true/false
     */
    public static boolean matches(String text) {
        return text.matches("\\+OK \\d* octets.*");
    }
}
