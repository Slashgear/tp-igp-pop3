package com.polytech4A.pop3.mailmanager;

import com.polytech4A.pop3.mailmanager.Exceptions.MailManagerException;

import java.io.File;
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
    public ServerMailManager(String path) {
        super();
        try {
            this.path = path;
            initDirectory();
            getUsers();
            for (User user : users) {
                File f = new File(this.path + user.getLogin());
                if (!f.exists()) {
                    f.mkdir();
                }
            }
        } catch (MailManagerException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User initUser(String login, String password) {
        for (User u : users) {
            if (u.getLogin().equals(login)) {
                User user = u;
                return user;
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
        return (users.contains(user) && user.isLocked());
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
        for (User u : users) {
            if (u.getLogin().equals(login) && u.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}
