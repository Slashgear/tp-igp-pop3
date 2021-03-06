package com.polytech4A.pop3.mailmanager;

import com.polytech4A.pop3.mailmanager.Exceptions.MalFormedMailException;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by Dimitri on 04/03/2015.
 *
 * @version 1.1
 *          <p/>
 *          Users for POP3.
 */
public class User {

    /**
     * Login of the User
     */
    private String login;

    /**
     * Password of the User
     */
    private String password;

    /**
     * List of Mails of the User
     */
    private ArrayList<Mail> mails;
    /**
     * Path to the User's directory
     */
    private String path;

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

    /**
     * Save the User's mails into its directory
     */
    public void saveMails() {
        Date date = new Date();

        try {
            BufferedWriter file = new BufferedWriter(new FileWriter(path + "/mails_" + date.toString() + ".txt", true));
            for (Mail mail : mails) {
                file.write(mail.getOutput().toString());
                file.newLine();
            }
            file.close();
        } catch (IOException e) {
            System.out.println("User.saveMail : Can't create file : " + path + "/mails_" + date.toString() + ".txt");
        }
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
