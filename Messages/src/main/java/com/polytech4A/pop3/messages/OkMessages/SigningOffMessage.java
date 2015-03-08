package com.polytech4A.pop3.messages.OkMessages;

import com.polytech4A.pop3.messages.Exceptions.MalFormedMessageException;
import com.polytech4A.pop3.messages.OkMessage;

import java.util.regex.Pattern;

/**
 * Created by Antoine on 08/03/2015.
 *
 * @author Antoine
 * @version 1.0
 *          <p/>
 *          Signing off message launched by POP3 server in order to signal his signing off.
 */
public class SigningOffMessage extends OkMessage {

    /**
     * Name of the server.
     */
    private String serverName;

    /**
     * True if mailbox is Empty.
     */
    private boolean emptiness;

    /**
     * Constructor who parse the text in parameter to create SigningoffMessage.
     * @param text to parse.
     * @throws MalFormedMessageException
     */
    public SigningOffMessage(String text) throws MalFormedMessageException {
        if(SigningOffMessage.matches(text)){
            serverName = text.split(" ")[1];
            emptiness=true;
            if(text.contains("not")){
                emptiness=false;
            }
            construct();
        }else{
            throw new MalFormedMessageException("MalFormed SigningOff message expected: ^\\+OK \\S* POP3 server is signing off \\(maildrop \\S\\)$");
        }
    }

    /**
     * Constructor of a SigningOffMessage.
     * @param serverName
     * @param emptiness
     */
    public SigningOffMessage(String serverName, boolean emptiness) {
        this.serverName = serverName;
        this.emptiness = emptiness;
        construct();
    }

    /**
     * Construct the message.
     */
    private void construct(){
        this.message.append(" ");
        this.message.append(serverName);
        this.message.append(" POP3 server is signing off (maildrop ");
        if(emptiness){
            this.message.append("empty)");
        }else{
            this.message.append("not empty)");
        }
    }

    /**
     * Static function for testing if the message in parameter has the structure of the SigningOffMessage.
     *
     * @param text Message to test.
     * @return true/false.
     */
    public static boolean matches(String text) {
        String regex = "^\\+OK \\S* POP3 server is signing off \\(maildrop \\S\\)$";
        return Pattern.matches(regex, text);
    }
}
