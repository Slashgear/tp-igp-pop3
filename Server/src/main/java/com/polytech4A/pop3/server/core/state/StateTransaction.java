package com.polytech4A.pop3.server.core.state;

import com.polytech4A.pop3.messages.ErrMessages.PermissionDeniedErr;
import com.polytech4A.pop3.messages.Exceptions.MalFormedMessageException;
import com.polytech4A.pop3.messages.MailMessage;
import com.polytech4A.pop3.messages.OkMessages.SigningOffMessage;
import com.polytech4A.pop3.messages.QuitMessage;
import com.polytech4A.pop3.messages.RetrMessage;
import com.polytech4A.pop3.server.core.Server;
import com.polytech4a.smtp.mailmanager.FacadeServer;
import com.polytech4a.smtp.mailmanager.exceptions.MailManagerException;
import com.polytech4a.smtp.mailmanager.exceptions.UnknownUserException;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;

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
     * Logger of the server.
     */
    private static Logger logger = Logger.getLogger(StateTransaction.class);

    private String login;

    private String password;

    /**
     * Constructor of Transaction state of the server.
     */
    public StateTransaction(String user, String password) {
        super();
        this.deleteMsg = Server.DEL_MESSAGE;
        this.login=user;
        this.password=password;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean analyze(String message) {
        try {
            if (RetrMessage.matches(message)) {
                RetrMessage retr = new RetrMessage(message);
                String mail = FacadeServer.getUserMails(login,password,Server.SERVER_DIRECTORY).get(retr.getNoMessages() - 1);
                StringBuffer buf = new StringBuffer();
                buf.append(new MailMessage(mail.getBytes().length));
                buf.append("\n");
                buf.append(mail);
                setMsgToSend(buf.toString());
                setNextState(new StateTransaction(login, password));
                return true;
            } else if (QuitMessage.matches(message)) {
                setNextState(new StateInit());
                setMsgToSend(new SigningOffMessage(Server.SERVER_NAME, deleteMsg).toString());
                return false; //Return false to end connection.
            }
        } catch (MalFormedMessageException e) {
            logger.error("Error during creation of messages\n" + e.getMessage());
            this.setNextState(new StateInit());
            this.setMsgToSend(new PermissionDeniedErr().toString());
        }catch (MailManagerException e) {
            logger.error("Error during creation of messages\n" + e.getMessage());
            this.setNextState(new StateInit());
            this.setMsgToSend(new PermissionDeniedErr().toString());
        } catch (UnknownUserException e) {
            logger.error("Error during creation of messages\n" + e.getMessage());
            this.setNextState(new StateInit());
            this.setMsgToSend(new PermissionDeniedErr().toString());
        }
        return false;
    }
}
