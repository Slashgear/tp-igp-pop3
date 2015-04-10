package com.polytech4A.pop3.mailmanager;

import com.polytech4A.pop3.mailmanager.Exceptions.MailManagerException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Dimitri on 07/03/2015.
 *
 * @version 1.1
 *          <p/>
 *          Client Mail Manager for POP3.
 */
public class ClientMailManager extends MailManager {

    /**
     * Constructor of the ClientMailManager
     *
     * @param login
     */
    public ClientMailManager(String login) {
        super();
        try {
            path = "./";
            
            initDirectory();
            initUser(login, "");
        } catch (MailManagerException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initDirectory() throws MailManagerException {
        File userFolder = new File(path),
                userMailFolder = new File(path+"Client_mails/");

        if(userFolder.exists() && !userMailFolder.exists()) {
            try {
                if (!(userMailFolder.mkdir())) {
                    throw new MailManagerException("MailManager.initDirectory : Could not init directories at " + path);
                }
            } catch (SecurityException se) {
                throw new MailManagerException("MailManager.initDirectory : Could not create folders at " + path);
            }
        } else if (!userFolder.exists() && !userMailFolder.exists()) try {
            if (!(userFolder.mkdir() && userMailFolder.mkdir())) {
                throw new MailManagerException("MailManager.initDirectory : Could not init directories at " + path);
            }
        } catch (SecurityException se) {
            throw new MailManagerException("MailManager.initDirectory : Could not create folders at " + path);
        }
        path+="Client_mails/";
    }

    /**
     * Create a pop3 mail to be send by the client
     *
     * @param receiver
     * @param content
     * @param subject
     * @return
     */
    public String createMail(String receiver, String content, String subject) {
        return users.get(0).createMail(receiver, content, subject);
    }

    /**
     * Save the client's mails
     */
    public void saveMails() {
        users.get(0).saveMails();
    }

    /**
     * Check if a string is a valid pop3 mail to
     * Add it to the client's mails List
     *
     * @param mail
     * @return
     */
    public boolean addMail(String text) {
        return users.get(0).addMail(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User initUser(String login, String password) {
        User user = new User(login, password, path);
        //if you uncomment the line below, the program will search for the registered user's mails
        //user.initMails();
        if(users == null){
            users=new ArrayList<User>();
        }
        users.add(user);
        return user;
    }
}
