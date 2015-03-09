package com.polytech4A.pop3.mailmanager;

import com.polytech4A.pop3.mailmanager.Exceptions.MailManagerException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
     * List of users of the MailManager
     */
    protected ArrayList<UserServer> users;

    /**
     * Constructor of ServerMailManager
     */
    public ServerMailManager(String path) throws MailManagerException{
        super(path);
        getUsers();
    }

    /**
     * Initialize a MailManager's user
     * @param login : String of the user's login
     * @param password : String of the user's password
     * @return Initialized user.
     */
    public User initUser(String login, String password) {
        UserServer user = new UserServer(login, password, path);
        if (isLockedUser(user)) {
            user.lockUser();
            user.initMails();
            return user;
        }
        return null;
    }

    /**
     * Check if a MailManager's user is locked
     *
     * @param user : User to test
     * @return true if the user is locked
     */
    public boolean isLockedUser(UserServer user) {
        return users.contains(user) && !user.isLocked();
    }

    /**
     * Unlock a MailManager's user if possible
     *
     * @param user : User to unlock
     */
    public void unlockUser(UserServer user) {
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
        UserServer user = new UserServer(login, password, path);
        return isLockedUser(user);
    }

    /**
     * Get the list of Users in a directory
     */
    public void getUsers() throws MailManagerException{
        this.users = new ArrayList<UserServer>();
        try{
            BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(path + "logins.txt")));
            String line;
            String[] identification;

            while ((line=br.readLine())!=null){
                identification=line.split(" ");
                users.add(new UserServer(identification[0],identification[1], path));
            }
            br.close();
        }
        catch (IOException e) {
            throw new MailManagerException("User.getUser : Can't open file : "+path+"logins.txt");
        }
    }
}
