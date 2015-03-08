package com.polytech4A.pop3.mailmanager;

import java.util.ArrayList;

/**
 * Created by Dimitri on 07/03/2015.
 *
 * @version 1.1
 *          <p/>
 *          Server Mail Manager for POP3.
 */
public class ServerMailManager extends MailManager {

    /**
     * Constructor of ServerMailManager
     */
    public ServerMailManager() {
        super();
        path = "Server/";
        initDirectory();
        users = getUsers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User initUser(String login, String password) {
        User user = new User(login, password, path);
        if (isLockedUser(user)) {
            user.lockUser();
            user.initMails();
            return user;
        }
        return null;
    }

    /**
     * Get the list of mails of a ServerMailManager's user
     *
     * @param user : User
     * @return List of mails
     */
    public ArrayList<Mail> getMails(User user) {
        if (isLockedUser(user)) {
            for (Mail mail : user.getMails()) {
                user.deleteMail(mail);
            }
        }
        return null;
    }

    /**
     * Check if a MailManager's user is locked
     *
     * @param user : User to test
     * @return true if the user is locked
     */
    public boolean isLockedUser(User user) {
        return users.contains(user) && !user.isLocked();
    }

    /**
     * Unlock a MailManager's user if possible
     *
     * @param user : User to unlock
     */
    public void unlockUser(User user) {
        if (users.contains(user) && user.isLocked()) {
            user.unlockUser();
        }
    }

    /**
     * Tests if the user exists and if the user is not already connected (lock).
     *
     * @param login    User login.
     * @param password User password.
     * @return True if user successfully log in.
     */
    public boolean isUserExists(String login, String password) {
        User user = new User(login, password, path);
        return isLockedUser(user);
    }
}
