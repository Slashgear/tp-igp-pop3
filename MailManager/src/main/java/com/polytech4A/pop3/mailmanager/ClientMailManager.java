package com.polytech4A.pop3.mailmanager;

import com.polytech4A.pop3.mailmanager.Exceptions.MailManagerException;

/**
 * Created by Dimitri on 07/03/2015.
 *
 * @version 1.1
 *          <p/>
 *          Client Mail Manager for POP3.
 */
public class ClientMailManager extends MailManager {

    UserClient user;

    /**
     * Constructor of the ClientMailManager
     *
     * @param login Client login
     * @param path Client directory path
     */
    public ClientMailManager(String login, String path) throws MailManagerException{
        super(path);
        user = new UserClient(login,path);
        user.initMails();
    }

    /**
     * Create a pop3 mail to be send by the client
     *
     * @param receiver Mail receiver
     * @param content Mail sender
     * @param subject Mail subject
     * @return
     */
    public String createMail(String receiver, String content, String subject) {
        return user.createMail(receiver, content, subject);
    }

    /**
     * Save the client's mails
     */
    public void saveMails() {
        user.saveMails();
    }

    /**
     * Check if a string is a valid pop3 mail to
     * Add it to the client's mails List
     *
     * @param mail : Mail to check
     * @return
     */
    public boolean addMail(String mail) {
        return user.addMail(mail);
    }
}
