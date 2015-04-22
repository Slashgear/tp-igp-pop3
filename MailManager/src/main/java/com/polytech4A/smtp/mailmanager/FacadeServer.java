package com.polytech4a.smtp.mailmanager;

import com.polytech4a.smtp.mailmanager.exceptions.MailManagerException;
import com.polytech4a.smtp.mailmanager.exceptions.MalFormedMailException;
import com.polytech4a.smtp.mailmanager.exceptions.UnknownUserException;
import com.polytech4a.smtp.mailmanager.mail.Mail;
import com.polytech4a.smtp.mailmanager.user.UserServer;

import java.util.ArrayList;

/**
 * Created by Dimitri on 16/04/2015.
 */
public class FacadeServer {
    /**
     * Check if an user exists in the list of user known by the Server
     *
     * @param login : address mail of the user
     * @param path  : directory where the Server users' directories are
     * @return Boolean : true if the user exists
     * @throws MailManagerException : Errors with directories during initialisation
     */
    public static boolean existsUser(String login, String path) throws MailManagerException {
        return new Server(path).existsUser(login);
    }

    /**
     * Check if an user exists in the list of user known by the Server
     *
     * @param login    : address mail of the user
     * @param password : password of the user
     * @param path     : directory where the Server users' directories are
     * @return Boolean : true if the user exists
     * @throws MailManagerException : Errors with directories during initialisation
     */
    public static boolean existsUser(String login, String password, String path) throws MailManagerException {
        return new Server(path).existsUser(login, password);
    }

    /**
     * Try to find an user who match the input parameters and return his mails
     *
     * @param login : address mail of the user
     * @param path  : directory where the Server users' directories are
     * @return ArrayList(String): list of mail of the user
     * @throws MailManagerException : Errors with directories during initialisation
     * @throws UnknownUserException : The user is unknown by the Server
     */
    public static ArrayList<String> getUserMails(String login, String path) throws MailManagerException, UnknownUserException {
        UserServer user = new Server(path).getUser(login);
        if (user == null) {
            throw new UnknownUserException("Server.getUserMails : There is no user " + login);
        }
        ArrayList<String> res = new ArrayList<String>();
        for (Mail mail : user.getMails()) {
            res.add(mail.getOutput().toString());
        }
        return res;
    }

    /**
     * Try to find an user who match the input parameters and return his mails
     *
     * @param login    : address mail of the user
     * @param path     : directory where the Server users' directories are
     * @param password : password of the user
     * @return ArrayList(String): list of mail of the user
     * @throws MailManagerException : Errors with directories during initialisation
     * @throws UnknownUserException : The user is unknown by the Server
     */
    public static ArrayList<String> getUserMails(String login, String password, String path) throws MailManagerException, UnknownUserException {
        UserServer user = new Server(path).getUser(login, password);
        if (user == null) {
            throw new UnknownUserException("Server.getUserMails : There is no user " + login + " with password " + password);
        }
        ArrayList<String> res = new ArrayList<String>();
        for (Mail mail : user.getMails()) {
            res.add(mail.getOutput().toString());
        }
        return res;
    }

    /**
     * Check if the input string is a mail and save it if the receiver exists
     *
     * @param mail : mail to save
     * @param path : directory where the Server users' directories are
     * @throws MailManagerException   : Errors with directories during initialisation
     * @throws MalFormedMailException : The mail is not a valid mail
     * @throws UnknownUserException   : The receiver of the mail is unknown by the server
     */
    public static void saveMail(String mail, String path) throws MailManagerException, MalFormedMailException, UnknownUserException {
        new Server(path).saveMail(mail);
    }

    /**
     * Check if the input string is a mail and save it if the receiver exists
     *
     * @param mail : mail to save
     * @param path : directory where the Server users' directories are
     * @param receiver : user which will keep the mail
     * @throws MailManagerException   : Errors with directories during initialisation
     * @throws MalFormedMailException : The mail is not a valid mail
     * @throws UnknownUserException   : The receiver of the mail is unknown by the server
     */
    public static void saveMail(String mail, String path, String receiver) throws MailManagerException, MalFormedMailException, UnknownUserException {
        new Server(path).saveMail(mail, receiver);
    }

    /**
     * Check if the user is already known by the server and save it if not
     *
     * @param login    : address mail of the user
     * @param password : password of the user
     * @param path     : directory where the Server users' directories are
     * @throws MailManagerException
     */
    public static void saveUser(String login, String password, String path) throws MailManagerException {
        new Server(path).saveUser(login, password);
    }
}
