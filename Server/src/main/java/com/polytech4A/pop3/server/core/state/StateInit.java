package com.polytech4A.pop3.server.core.state;

import com.polytech4A.pop3.mailmanager.ServerMailManager;
import com.polytech4A.pop3.messages.Exceptions.MalFormedMessageException;
import com.polytech4A.pop3.messages.OkMessages.ServerReadyMessage;
import com.polytech4A.pop3.server.core.Server;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Adrien on 02/03/2015.
 *
 * @author Adrien
 * @version 1.0
 *          <p/>
 *          Initial state of the server.
 */
public class StateInit extends State {

    /**
     * Logger of the server.
     */
    private static Logger logger = Logger.getLogger(StateInit.class);

    /**
     * Initialization state constructor.
     */
    public StateInit() {
        super();
        this.setNextState(new StateAuth());
        String arpa = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        System.out.println(arpa);
        this.setMsgToSend(new ServerReadyMessage(Server.SERVER_NAME, arpa).toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean analyze(String message, ServerMailManager manager) {
        return true;
    }

}
