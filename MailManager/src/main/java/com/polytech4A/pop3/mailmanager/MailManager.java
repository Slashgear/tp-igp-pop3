package com.polytech4A.pop3.mailmanager;

import com.polytech4A.pop3.mailmanager.Exceptions.MailManagerException;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Dimitri on 03/03/2015.
 * @version 1.1
 *          <p/>
 *          Mail Manager for POP3.
 */
public abstract class MailManager {

    /**
     * Path to the MailManager's directory
     */
    protected String path;

    /**
     * Initialize the MailManager's directory
     */
    protected void initDirectory() throws MailManagerException {
        File userFolder = new File(path),
                userMailFolder = new File(path+"Mails/"),
                userLoginsFile = new File(path+"Mails/logins.txt");

        if (!userFolder.exists()) try {
            if (!(userFolder.mkdir() && userMailFolder.mkdir() && userLoginsFile.createNewFile())) {
                MailManagerException ex = new MailManagerException("MailManager.initDirectory : Could not init directories at " + path);
                throw ex;
            }
        } catch (SecurityException se) {
            MailManagerException ex = new MailManagerException("MailManager.initDirectory : Could not create folders at " + path);
            throw ex;
        } catch (IOException e) {
            MailManagerException ex = new MailManagerException("MailManager.initDirectory : Could not create logins.txt at " + path + "Mails/");
            throw ex;
        }
        path+="Mails/";
    }

    /**
     * Constructor of the MailManager
     */
    protected MailManager (String path) throws MailManagerException{
        this.path = path;
        initDirectory();
    }
}
