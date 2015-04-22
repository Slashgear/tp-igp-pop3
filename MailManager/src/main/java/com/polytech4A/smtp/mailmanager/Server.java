package com.polytech4a.smtp.mailmanager;

import com.polytech4a.smtp.mailmanager.exceptions.MailManagerException;
import com.polytech4a.smtp.mailmanager.exceptions.MalFormedMailException;
import com.polytech4a.smtp.mailmanager.exceptions.UnknownUserException;
import com.polytech4a.smtp.mailmanager.mail.Mail;
import com.polytech4a.smtp.mailmanager.user.UserServer;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Dimitri on 07/03/2015.
 *
 * @version 1.1
 *          <p/>
 *          Server Mail Manager for POP3.
 */
public class Server extends MailManager {

    /**
     * List of users of the MailManager
     */
    protected ArrayList<UserServer> users;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initDirectory() throws MailManagerException {
        File userFolder = new File(path),
                userMailFolder = new File(path + "Server_mails/"),
                userLoginsFile = new File(path + "Server_mails/logins.txt");
        try {
            if (userFolder.exists() && !userMailFolder.exists()) {
                if (!(userMailFolder.mkdir() && userLoginsFile.createNewFile())) {
                    throw new MailManagerException("Server.initDirectory : Could not init directories at " + path);
                }
            } else if (userFolder.exists() && userMailFolder.exists()) {
                if (!userLoginsFile.exists() && !userLoginsFile.createNewFile()) {
                    throw new MailManagerException("Server.initDirectory : Could not init directories at " + path);
                }
            } else if (!userFolder.exists())
                if (!(userFolder.mkdirs() && userMailFolder.mkdir() && userLoginsFile.createNewFile())) {
                    throw new MailManagerException("Server.initDirectory : Could not init directories at " + path);
                }
        } catch (SecurityException se) {
            throw new MailManagerException("Server.initDirectory : Could not create folders at " + path);
        } catch (IOException e) {
            throw new MailManagerException("Server.initDirectory : Could not create logins.txt at " + path + "Server_mails/");
        }
        path += "Server_mails/";
    }

    /**
     * Initialize the list of Users in a directory
     *
     * @return list of Users
     */
    protected ArrayList<UserServer> initUsers() throws MailManagerException {
        ArrayList<UserServer> users = new ArrayList<UserServer>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path + "logins.txt")));
            String line;
            String[] identification;

            while ((line = br.readLine()) != null) {
                identification = line.split(" ");
                if (identification.length == 2) {
                    users.add(new UserServer(identification[0], identification[1], path));
                }
            }
            br.close();
            return users;
        } catch (IOException e) {
            throw new MailManagerException("Server.getUsers : Can't open file : " + path + "logins.txt");
        } catch (MailManagerException e) {
            throw e;
        }
    }

    /**
     * Constructor of Server
     */
    protected Server(String path) throws MailManagerException {
        super(path);
        this.users = initUsers();
    }

    /**
     * Constructor of Server without initialization of users and directories
     */
    protected ArrayList<UserServer> getUsers() {
        return users;
    }

    /**
     * Try to find an user who match the login
     *
     * @param login    : String of the user's login
     * @return Boolean : true if an user match.
     */
    protected boolean existsUser(String login) {
        for (UserServer u : users) {
            if (u.getLogin().equals(login)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Try to find an user who match the login and the password
     *
     * @param login    : String of the user's login
     * @param password : String of the user's password
     * @return Boolean : true if an user match.
     */
    protected boolean existsUser(String login, String password) {
        for (UserServer u : users) {
            if (u.getLogin().equals(login) && u.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Try to find an user who match the login and password
     *
     * @param login    : String of the user's login
     * @param password : String of the user's password
     * @return Initialized user or null if the user does not exist.
     */
    protected UserServer getUser(String login, String password) throws MailManagerException {
        for (UserServer u : users) {
            if (u.getLogin().equals(login) && u.getPassword().equals(password)) {
                /*if (u.isLocked()){
                    throw new MailManagerException("Server.getUser : couldn't get user " + login + " : the user is locked");
                }*/
                try {
                    //u.lockUser();
                    u.initMails();
                    //u.unlockUser();
                    return u;
                } catch (MailManagerException e) {
                    throw new MailManagerException("Server.getUser : couldn't get user " + login + " mails :\n" + e.getMessage());
                }
            }
        }
        return null;
    }

    /**
     * Try to find an user who match the login
     *
     * @param login    : String of the user's login
     * @return Initialized user or null if the user does not exist.
     */
    protected UserServer getUser(String login) throws MailManagerException {
        for (UserServer u : users) {
            if (u.getLogin().equals(login)) {
                /*if (u.isLocked()){
                    throw new MailManagerException("Server.getUser : couldn't get user " + login + " : the user is locked");
                }*/
                try {
                    //u.lockUser();
                    u.initMails();
                    //u.unlockUser();
                    return u;
                } catch (MailManagerException e) {
                    throw new MailManagerException("Server.getUser : couldn't get user " + login + " mails :\n" + e.getMessage());
                }
            }
        }
        return null;
    }

    /**
     * Save user into logins.txt
     *
     * @param login : Address mail of the user
     * @param password : Password of the user
     * @throws MailManagerException
     */
    protected void saveUser(String login, String password) throws MailManagerException {
        UserServer user = new UserServer(login, password, path);
        if (getUser(user.getLogin(), user.getPassword()) == null) {
            try {
                FileWriter file = new FileWriter(path + "/logins.txt", true);
                file.write(user.getLogin() + " " + user.getPassword() + "\n");
                file.close();
                users.add(user);
            } catch (IOException e) {
                throw new MailManagerException("Server.saveMails : can't open/write in " + path + "/logins.txt");
            }
        }
    }

    /**
     * Check if the input string is a valid mail to save it
     *
     * @param input String : mail to test
     * @throws MailManagerException
     */
    protected void saveMail(String input, String receiver) throws MailManagerException, MalFormedMailException, UnknownUserException {
        Mail mail;
        try {
            mail = new Mail(input);
        } catch (MalFormedMailException e) {
            throw new MalFormedMailException("Server.saveMail : the input is not a mail :\n" + e.getMessage());
        }

        for (UserServer u : users) {
            if (u.getLogin().equals(receiver)) {
                u.addMail(mail);
                u.saveMails();
                return;
            }
        }
        throw new UnknownUserException("Server.saveMail : The user " + receiver + " does not exist");
    }

    /**
     * Check if the input string is a valid mail and if the receiver is known to save it
     *
     * @param input String : mail to test
     * @throws MailManagerException
     */
    protected void saveMail(String input) throws MailManagerException, MalFormedMailException, UnknownUserException {
        String receiver;
        Mail mail;
        try {
            mail = new Mail(input);
            receiver = mail.getReceiver();
        } catch (MalFormedMailException e) {
            throw new MalFormedMailException("Server.saveMail : the input is not a mail :\n" + e.getMessage());
        }

        for (UserServer u : users) {
            if (u.getLogin().equals(receiver)) {
                u.addMail(mail);
                u.saveMails();
                return;
            }
        }
        throw new UnknownUserException("Server.saveMail : The user " + receiver + " does not exist");
    }
}
