package com.polytech4A.pop3.server.core.state;

import com.polytech4A.pop3.messages.*;
import com.polytech4A.pop3.messages.ErrMessages.NoMailBoxErr;
import com.polytech4A.pop3.messages.ErrMessages.PermissionDeniedErr;
import com.polytech4A.pop3.messages.Exceptions.MalFormedMessageException;
import com.polytech4A.pop3.messages.OkMessages.OkApopMessage;
import com.polytech4A.pop3.server.core.Server;
import com.polytech4a.smtp.mailmanager.FacadeServer;
import com.polytech4a.smtp.mailmanager.exceptions.MailManagerException;
import com.polytech4a.smtp.mailmanager.exceptions.UnknownUserException;
import org.apache.log4j.Logger;

import java.util.ArrayList;

/**
 * Created by Adrien on 04/03/2015.
 *
 * @author Adrien
 * @version 1.0
 *          <p/>
 *          State of the server in which it is waiting for the client to provide authorization informations.
 */
public class StateAuth extends State {
    /**
     * Logger of the server.
     */
    private static Logger logger = Logger.getLogger(Server.class);

    /**
     * Number of times that the client tries to log in the server.
     */
    private int nbTry;

    /**
     * User name.
     */
    private String user;

    /**
     * User password.
     */
    private String password;

    /**
     * Maximum number of times that the client can try to log in the server.
     */
    private static final int MAX_TRY = 3;

    /**
     * Constructor of the Authorization state of the server.
     */
    public StateAuth() {
        super();
        this.nbTry = 0;
    }

    /**
     * Constructor of the Authorization state of the server.
     *
     * @param nbTry Number of times that the client tries to log in the server.
     */
    public StateAuth(int nbTry) {
        super();
        this.nbTry = nbTry;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean analyze(String message) {
        try {
            if (ApopMessage.matches(message)) { //If APOP message received ...
                ApopMessage apop = new ApopMessage(message);
                user = apop.getId();
                password = apop.getPassword();
                setNextState(new StateTransaction(user, password));
                int mailsSize = 0;
                ArrayList<String> mails = FacadeServer.getUserMails(user, password, Server.SERVER_DIRECTORY);
                for (String mail : mails) {
                    mailsSize += mail.length();
                }
                setMsgToSend(new OkApopMessage(user, mails.size(), mailsSize).toString());
                return true;
            } else if (UserMessage.matches(message)) { //If User name message received ...
                UserMessage usr = new UserMessage(message);
                user = usr.getId();
                setNextState(new StateAuth(nbTry));
                setMsgToSend(new OkMessage().toString());
                return true;
            } else if (PassMessage.matches(message)) { //If Password message received ...
                PassMessage pw = new PassMessage(message);
                password = pw.getPassword();
                setNextState(new StateTransaction(user, password));
                int mailsSize = 0;
                ArrayList<String> mails = FacadeServer.getUserMails(user, password, Server.SERVER_DIRECTORY);
                for (String mail : mails) {
                    mailsSize += mail.length();
                }
                setMsgToSend(new OkApopMessage(user, mails.size(), mailsSize).toString());
                return true;
            } else {
                return invalidAuthProcessing(new PassMessage(user).toString());
            }

        } catch (MailManagerException e) {
            setNextState(new StateInit());
            setMsgToSend(new QuitMessage().toString());
            return false;
        } catch (MalFormedMessageException e) {
            setNextState(new StateInit());
            setMsgToSend(new QuitMessage().toString());
            return false;
        } catch (UnknownUserException e) {
            return invalidAuthProcessing(new NoMailBoxErr(user).toString());
        }
    }

    /**
     * Tests the conditions when the Apopmessage is invalid.
     *
     * @return False when the number of tryings are greater than the max number of tryings.
     * True if the connection still runs.
     */
    private boolean invalidAuthProcessing(String message) {
        nbTry++;
        if (nbTry >= MAX_TRY) {
            setNextState(new StateInit());
            setMsgToSend(new PermissionDeniedErr().toString());
            return false;
        } else {
            setNextState(new StateAuth(nbTry));
            setMsgToSend(message);
            return true;
        }
    }

}
