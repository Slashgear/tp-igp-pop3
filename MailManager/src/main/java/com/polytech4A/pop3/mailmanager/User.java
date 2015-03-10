package com.polytech4A.pop3.mailmanager;

import com.polytech4A.pop3.mailmanager.Exceptions.MalFormedMailException;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

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

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }

    /**
     * Getter of the User's mail list
     *
     * @return List of mails
     */
    public ArrayList<Mail> getMails() {
        initMails();
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
    public void deleteMail(Mail mail) throws FileNotFoundException {
        mails.remove(mail);
        File file= new File("./Server_mails/"+this.getLogin());
        if(file.exists()&& file.isDirectory()){
            File[] messages=file.listFiles();
            for(int i=0;i<messages.length;i++){
                if(!messages[i].getName().contains("lock")){
                    messages[i].delete();
                }
            }
        }else throw new FileNotFoundException("Directory Server_mail was not found");
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
            File directory= new File("./Client_mails/"+login+"/");
            if(!directory.exists()){
                directory.mkdir();
            }
            File file= new File("./Client_mails/"+login+"/mails_" + date.getTime() + ".txt");
            file.createNewFile();
            BufferedWriter bfile = new BufferedWriter(new FileWriter(file.getAbsoluteFile(), true));
            for (Mail mail : mails) {
                bfile.write(mail.getOutput().toString());
                bfile.newLine();
                bfile.flush();
            }
            bfile.close();
        } catch (IOException e) {
            System.out.println(e);
            System.out.println("User.saveMail : Can't create file : " + path + "/mails_" + date.getTime() + ".txt");
        }
    }

    /**
     * Get the User's mails in its directory
     */
    public void initMails() {
        File folder = new File(path);
        mails.clear();
        try {
            for (File fileEntry : folder.listFiles()) {
                if (!fileEntry.isDirectory()&& !fileEntry.getAbsolutePath().contains("lock")) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileEntry.getAbsolutePath())), "UTF-8"));
                    StringBuilder builder = new StringBuilder();
                    String separator = System.getProperty("line.separator");
                    String line;
                    while((line = reader.readLine()) != null) {
                        builder.append(line);
                        builder.append(separator);
                    }
                    reader.close();
                    mails.add(new Mail(builder.toString()));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("User.initMails : File in '" + path + "' not found");
        } catch (MalFormedMailException e) {
            System.out.println("User.InitMails : " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
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
