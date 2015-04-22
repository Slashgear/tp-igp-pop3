package com.polytech4a.smtp.mailmanager.user;

import com.polytech4a.smtp.mailmanager.exceptions.MailManagerException;
import com.polytech4a.smtp.mailmanager.exceptions.MalFormedMailException;
import com.polytech4a.smtp.mailmanager.mail.Mail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
    public User(String login, String password, String path) throws MailManagerException {
        this.login = login;
        this.password = password;
        this.path = path + login;
        this.mails = new ArrayList<Mail>();

        File f = new File(this.path);
        if (!f.exists() && !f.mkdir()) {
            throw new MailManagerException("User.User : Could not init directories at " + this.path);
        }
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        this.path = "";
        this.mails = new ArrayList<Mail>();
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getPath() {
        return path;
    }

    public ArrayList<Mail> getMails() {
        return mails;
    }

    /**
     * Add a mail to the user's mail list
     *
     * @param mail Mail to add
     */
    public void addMail(Mail mail) {
        mails.add(mail);
    }

    /**
     * Check if a string is a valid pop3 mail to add it
     * to the User's list
     *
     * @param content : String to test
     */
    public void addMail(String content) throws MalFormedMailException {
        try {
            Mail mail = new Mail(content);
            mails.add(mail);
        } catch (MalFormedMailException e) {
            throw new MalFormedMailException("User.addMail : Can't add Mail :\n" + e.getMessage());
        }
    }

    /**
     * Get the User's mails in its directory
     */
    public ArrayList<Mail> initMails() throws MailManagerException {
        File folder = new File(path);
        Scanner scan;
        mails = new ArrayList<Mail>();
        for (File fileEntry : folder.listFiles())
            if (!fileEntry.isDirectory()) {
                try {
                    StringBuilder input = new StringBuilder();
                    scan = new Scanner(new File(folder + "/" + fileEntry.getName()));
                    while (scan.hasNextLine()) {
                        input.append(scan.nextLine());
                        input.append("\n");
                    }
                    scan.close();
                    Mail mail = new Mail(input.toString());
                    mails.add(mail);
                } catch (FileNotFoundException e) {
                    throw new MailManagerException("User.initMails : File in '" + path + "' not found");
                } catch (MalFormedMailException ex) {
                    System.out.println("WARNING User.initMails : The file " + folder + "\\" + fileEntry.getName() + " is not a valid email");
                }
            }
        return mails;
    }

    /**
     * Creates a mail to be send by the User
     *
     * @param receiver : String of the receiver of the mail
     * @param content  : String of the content of the mail
     * @param subject  : String of the subject of the mail
     * @return mail created
     */
    public String createMail(String receiver, String content, String subject) throws MalFormedMailException {
        Mail mail = new Mail(receiver, this.login, content, subject);
        return mail.getOutput().toString();
    }

    /**
     * Save the User's mails into its directory
     */
    public void saveMails() throws MailManagerException {
        Calendar calendar = Calendar.getInstance();
        try {
            for (int i = 0; i < mails.size(); i++) {
                StringBuilder filename = new StringBuilder();
                filename.append(path);
                filename.append("/mail_");
                filename.append(calendar.get(Calendar.DAY_OF_MONTH));
                filename.append(calendar.get(Calendar.MONTH) + 1);
                filename.append(calendar.get(Calendar.YEAR));
                filename.append("_");
                filename.append(i);
                filename.append(".txt");

                FileWriter file = new FileWriter(filename.toString());
                file.write(mails.get(i).getOutput().toString());
                file.close();
            }
        } catch (IOException e) {
            throw new MailManagerException("User.saveMail : Can't create file in " + path);
        }
    }
}
