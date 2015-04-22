package com.polytech4A.pop3.server.core.state;

import com.polytech4A.pop3.mailmanager.Mail;
import com.polytech4A.pop3.mailmanager.ServerMailManager;
import com.polytech4A.pop3.mailmanager.User;
import com.polytech4A.pop3.messages.ErrMessages.PermissionDeniedErr;
import com.polytech4A.pop3.messages.Exceptions.MalFormedMessageException;
import com.polytech4A.pop3.messages.MailMessage;
import com.polytech4A.pop3.messages.OkMessages.SigningOffMessage;
import com.polytech4A.pop3.messages.QuitMessage;
import com.polytech4A.pop3.messages.RetrMessage;
import com.polytech4A.pop3.server.core.Server;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

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
    private boolean deleteMsg;

    /**
     * Current authenticated user.
     */
    private User user;

    /**
     * Logger of the server.
     */
    private static Logger logger = Logger.getLogger(StateTransaction.class);

    /**
     * Constructor of Transaction state of the server.
     */
    public StateTransaction(User user) {
        super();
        this.deleteMsg = Server.DEL_MESSAGE;
        this.user = user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean analyze(String message, ServerMailManager manager) {
        try {
            if (RetrMessage.matches(message)) {
                RetrMessage retr = new RetrMessage(message);
                Mail mail = user.getMails().get(retr.getNoMessages() - 1);
                StringBuffer buf = new StringBuffer();
                buf.append(new MailMessage(mail.getOutput().toString().getBytes().length));
                buf.append("\n");
                buf.append(mail.getOutput().toString());
                setMsgToSend(buf.toString());
                setNextState(new StateTransaction(user));
                return true;
            } else if (QuitMessage.matches(message)) {
                setNextState(new StateInit());
                if (deleteMsg) {
                    user.deleteMail();
                }
                setMsgToSend(new SigningOffMessage(Server.SERVER_NAME, deleteMsg).toString());
                user.unlockUser();
                return false; //Return false to end connection.
            }
        } catch (MalFormedMessageException e) {
            logger.error("Error during creation of messages\n" + e.getMessage());
            this.setNextState(new StateInit());
            this.setMsgToSend(new PermissionDeniedErr().toString());
        } catch (FileNotFoundException e) {
            logger.error("DELETE of messages Failed" + e.getMessage());
        }
        return false;
    }
}
