package com.polytech4A.pop3.server.core.state;

import com.polytech4A.pop3.mailmanager.Mail;
import com.polytech4A.pop3.mailmanager.ServerMailManager;
import com.polytech4A.pop3.mailmanager.User;
import com.polytech4A.pop3.messages.Exceptions.MalFormedMessageException;
import com.polytech4A.pop3.messages.OkMessages.SigningOffMessage;
import com.polytech4A.pop3.messages.QuitMessage;
import com.polytech4A.pop3.messages.RetrMessage;
import com.polytech4A.pop3.server.core.Server;

/**
 * Created by Adrien on 05/03/2015.
 *
 * @author Adrien
 * @version 1.0
 *          <p/>
 *          State of the server in which it is waiting for client request and execute that request.
 */
public class StateTransaction extends State {

    /**
     * True : Server deletes message(s). False : Server does not delete message(s).
     */
    //TODO : remove it.
    private boolean deleteMsg;

    /**
     * Current authenticated user.
     */
    private User user;

    /**
     * Constructor of Transaction state of the server.
     */
    public StateTransaction() {
        super();
        this.deleteMsg = Server.DEL_MESSAGE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean analyze(String message, ServerMailManager manager) {
        try {
            if (RetrMessage.matches(message)) {
                RetrMessage retr = new RetrMessage(message);
                Mail mail = user.getMails().get(retr.getNoMessages());
                StringBuffer buf = new StringBuffer("+OK ");
                buf.append(mail.getOutput().toString().getBytes().length);
                buf.append("/n");
                buf.append(mail.getOutput().toString());
                setMsgToSend(buf.toString());
                setNextState(new StateTransaction());
                return true;
            } else if (QuitMessage.matches(message)) {
                setNextState(new StateInit());
                if(deleteMsg) {
                    for(Mail mail : user.getMails()) {
                        user.deleteMail(mail);
                    }
                }
                setMsgToSend(new SigningOffMessage(Server.SERVER_NAME, deleteMsg).toString());
                return false; //Return false to end connection.
            }
        } catch (MalFormedMessageException e) {
            //TODO : Better exception handling.
            System.out.println("Error during creation of messages\n" + e.getMessage());
        }
        return false;
    }
}
