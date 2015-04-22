package com.polytech4a.smtp.mailmanager.user;

import com.polytech4a.smtp.mailmanager.exceptions.MailManagerException;

import java.io.File;
import java.io.IOException;

/**
 * Created by Dimitri on 09/03/2015.
 */
public class UserServer extends User {
    public UserServer(String login, String password, String path) throws MailManagerException {
        super(login, password, path);
    }

    /**
     * Check if the User is locked
     *
     * @return true if the User is locked
     */
    public boolean isLocked() {
        File f = new File(path + "/lock.txt");
        return f.exists() && !f.isDirectory();
    }

    /**
     * Lock the User's directory to prevent multi-access if possible
     *
     * @return true the user can be locked
     */
    public boolean lockUser() {
        File f = new File(path + "/lock.txt");
        if (!f.exists() && !f.isDirectory()) {
            try {
                return f.createNewFile();
            } catch (IOException e) {
                System.out.println("User.lockUser : Can't create file : " + path + "/lock.txt");
            }
        }
        return false;
    }

    /**
     * Unlock the User's directory
     *
     * @return true if the user has been unlocked
     */
    public boolean unlockUser() {
        File f = new File(path + "/lock.txt");
        if (f.exists() && !f.isDirectory()) {
            if (f.delete()) {
                return true;
            }
        }
        return false;
    }
}
