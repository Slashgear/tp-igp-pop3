package com.polytech4A.pop3.mailmanager;

import com.polytech4A.pop3.mailmanager.Exceptions.MalFormedMailException;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Dimitri on 04/03/2015.
 *
 * @version 1.1
 *          <p/>
 *          Users for POP3.
 */
public abstract class User {

    /**
     * Login of the User
     */
    protected String login;

    /**
     * Password of the User
     */
    protected String password;

    /**
     * List of Mails of the User
     */
    protected ArrayList<Mail> mails;
    /**
     * Path to the User's directory
     */
    protected String path;

    /**
     * Constructor of the User
     */
    public User(String login, String password, String path) {
        this.login = login;
        this.password = password;
        this.path = path + login;
        this.mails = new ArrayList<Mail>();
    }

    /**
     * Getter of the User's mail list
     *
     * @return List of mails
     */
    public ArrayList<Mail> getMails() {
        return mails;
    }

    /**
     * Check if a string is a valid pop3 mail to add it
     * to the User's list
     *
     * @param content : String to test
     * @return true if the string is a valid pop3 mail
     */
    public boolean addMail(String content) {
        try {
            Mail mail = new Mail(content);
            mails.add(mail);
            return true;
        } catch (MalFormedMailException e) {
            System.out.println("User.addMail : Can't add Mail : " + e.getMessage());
            return false;
        }

    }

    /**
     * Remove a mail from the user's mail list
     *
     * @param mail : Mail to delete
     */
    public void deleteMail(Mail mail) {
        mails.remove(mail);
    }

    /**
     * Get the User's mails in its directory
     */
    public void initMails() {
        File folder = new File(path);
        try {
            for (File fileEntry : folder.listFiles()) {
                if (!fileEntry.isDirectory()) {
                    mails.add(new Mail(new Scanner(new File(fileEntry.getName())).useDelimiter("\\Z").next()));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("User.initMails : File in '" + path + "' not found");
        } catch (MalFormedMailException e) {
            System.out.println("User.InitMails : " + e.getMessage());
        }
    }

    /**
     * Creates a mail to be send by the User
     *
     * @param receiver : String of the receiver of the mail
     * @param content  : String of the content of the mail
     * @param subject  : String of the subject of the mail
     * @return mail created
     */
    public String createMail(String receiver, String content, String subject) {
        Mail mail = new Mail(this.login, receiver, content, subject);
        return mail.getOutput().toString();
    }
}
