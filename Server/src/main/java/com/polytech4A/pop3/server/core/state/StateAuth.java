package com.polytech4A.pop3.server.core.state;

import com.polytech4A.pop3.mailmanager.Mail;
import com.polytech4A.pop3.mailmanager.ServerMailManager;
import com.polytech4A.pop3.mailmanager.User;
import com.polytech4A.pop3.messages.ApopMessage;
import com.polytech4A.pop3.messages.Exceptions.MalFormedMessageException;
import com.polytech4A.pop3.messages.PassMessage;
import com.polytech4A.pop3.messages.UserMessage;

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
    public boolean analyze(String message, ServerMailManager manager) {
        try {
            if (ApopMessage.matches(message)) {
                ApopMessage apop = new ApopMessage(message);
                user = apop.getId();
                password = apop.getPassword();
                if (manager.isUserExists(user, password)) {
                    User usr = manager.initUser(user, password);
                    setNextState(new StateTransaction());
                    buildSuccessLoginMessage(usr);
                    return true;
                } else {
                    return invalidAuthProcessing();
                }
            } else if (UserMessage.matches(message)) {
                UserMessage usr = new UserMessage(message);
                user = usr.getId();
                setNextState(new StateAuth(nbTry));
                setMsgToSend(null);
                return true;
            } else if (PassMessage.matches(message)) {
                PassMessage pw = new PassMessage(message);
                password = pw.getPassword();
                if (manager.isUserExists(user, password)) {
                    User usr = manager.initUser(user, password);
                    setNextState(new StateTransaction());
                    buildSuccessLoginMessage(usr);
                    return true;
                } else {
                    return invalidAuthProcessing();
                }
            } else {
                return invalidAuthProcessing();
            }
        } catch (MalFormedMessageException e) {
            return invalidAuthProcessing();
        }
    }

    /**
     * Tests the conditions when the Apopmessage is invalid.
     *
     * @return False when the number of tryings are greater than the max number of tryings.
     * True if the connection still runs.
     */
    private boolean invalidAuthProcessing() {
        nbTry++;
        setMsgToSend("-ERR permission denied");
        if (nbTry >= MAX_TRY) {
            setNextState(new StateInit());
            return false;
        } else {
            setNextState(new StateTransaction());
            return true;
        }
    }

    private void buildSuccessLoginMessage(User user) {
        int mailsSize = 0;
        for(Mail mail : user.getMails()) {
            mailsSize += mail.getOutput().toString().length();
        }
        StringBuffer buf = new StringBuffer("+OK ");
        buf.append(user);
        buf.append("'s maildrop has ");
        buf.append(user.getMails().size());
        buf.append(" messages (");
        buf.append(mailsSize);
        buf.append(")");
        setMsgToSend(buf.toString());
    }

}
