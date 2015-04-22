package com.polytech4a.smtp.mailmanager;

import com.polytech4a.smtp.mailmanager.exceptions.MailManagerException;
import com.polytech4a.smtp.mailmanager.exceptions.MalFormedMailException;
import com.polytech4a.smtp.mailmanager.user.UserClient;

import java.io.File;

/**
 * Created by Dimitri on 07/03/2015.
 *
 * @version 1.1
 *          <p/>
 *          Client Mail Manager for POP3.
 */
public class Client extends MailManager {

    private UserClient user;

    /**
     * Constructor of the ClientMailManager
     *
     * @param login : String login of the user
     * @param path  : String path of the user's directory
     */
    protected Client(String login, String path) throws MailManagerException {
        super(path);
        this.user = new UserClient(login, this.path);
    }

    protected Client(String login) {
        super();
        this.user = new UserClient(login);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initDirectory() throws MailManagerException {
        File userFolder = new File(path),
                userMailFolder = new File(path + "Client_mails/");
        try {
            if (userFolder.exists() && !userMailFolder.exists()) {
                if (!(userMailFolder.mkdir())) {
                    throw new MailManagerException("Client.initDirectory : Could not init directories at " + path);
                }
            } else if (!userFolder.exists()) {
                if (!(userFolder.mkdirs() && userMailFolder.mkdir())) {
                    throw new MailManagerException("Client.initDirectory : Could not init directories at " + path);
                }
            }
        } catch (SecurityException se) {
            throw new MailManagerException("Client.initDirectory : Could not create folders at " + path);
        }
        path += "Client_mails/";
    }

    /**
     * Create a pop3 mail to be send by the client
     *
     * @param receiver : String receiver of the mail
     * @param content  : String content of the mail
     * @param subject  : String content of the mail
     * @return String of the output of the created mail
     */
    protected String createMail(String receiver, String content, String subject) throws MalFormedMailException {
        return user.createMail(receiver, content, subject);
    }

    /**
     * Check if a string is a valid mail to
     * add it to the client's mails List
     * and save it in the output directory
     *
     * @param mail : String input to test
     */
    protected void addMail(String mail) throws MalFormedMailException, MailManagerException {
        user.addMail(mail);
        user.saveMails();
    }
}
