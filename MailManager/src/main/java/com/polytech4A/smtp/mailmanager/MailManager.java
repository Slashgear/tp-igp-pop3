package com.polytech4a.smtp.mailmanager;

import com.polytech4a.smtp.mailmanager.exceptions.MailManagerException;

/**
 * Created by Dimitri on 03/03/2015.
 *
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
    protected abstract void initDirectory() throws MailManagerException;

    /**
     * Constructor of the MailManager
     */
    protected MailManager(String path) throws MailManagerException {
        this.path = path;
        initDirectory();
    }

    /**
     * Constructor of the MailManager without initialization of directories
     */
    protected MailManager() {
        this.path = "";
    }
}
