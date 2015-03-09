package com.polytech4A.pop3.mailmanager;

import com.polytech4A.pop3.mailmanager.Exceptions.MailManagerException;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Dimitri on 03/03/2015.
 * @version 1.1
 *          <p/>
 *          Mail Manager for POP3.
 */
public abstract class MailManager {

    /**
     * Path to the MailManager's directory
     */
    protected String path;

    /**
     * List of users of the MailManager
     */
    protected ArrayList<User> users;

    /**
     * Initialize the MailManager's directory
     */
    protected void initDirectory() throws MailManagerException {
        File userFolder = new File(path),
                userMailFolder = new File(path+"mails/"),
                userLoginsFile = new File(path+"mails/logins.txt");

        if(userFolder.exists() && !userMailFolder.exists()) {
            try {
                if (!(userMailFolder.mkdir() && userLoginsFile.createNewFile())) {
                    throw new MailManagerException("MailManager.initDirectory : Could not init directories at " + path);
                }
            } catch (SecurityException se) {
                throw new MailManagerException("MailManager.initDirectory : Could not create folders at " + path);
            } catch (IOException e) {
                throw new MailManagerException("MailManager.initDirectory : Could not create logins.txt at " + userMailFolder.getPath());
            }
        } else if (!userFolder.exists() && !userMailFolder.exists()) try {
            if (!(userFolder.mkdir() && userMailFolder.mkdir() && userLoginsFile.createNewFile())) {
                throw new MailManagerException("MailManager.initDirectory : Could not init directories at " + path);
            }
        } catch (SecurityException se) {
            throw new MailManagerException("MailManager.initDirectory : Could not create folders at " + path);
        } catch (IOException e) {
            throw new MailManagerException("MailManager.initDirectory : Could not create logins.txt at " + path + "mails/");
        }
        path+="Server_mails/";
    }

    /**
     * Constructor of the MailManager
     */
    protected MailManager (){
        path = "";
        getUsers();
    }

    /**
     * Initialize a MailManager's user
     * @param login : String of the user's login
     * @param password : String of the user's password
     * @return Initialized user.
     */
    protected abstract User initUser (String login, String password);


    /**
     * Get the list of Users in a directory
     * @return list of Users
     */
    private void getUsers(){
        this.users = new ArrayList<User>();
        try{
            BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(path + "logins.txt")));
            String line;
            String[] identification;

            while ((line=br.readLine())!=null){
                identification=line.split(" ");
                users.add(new User(identification[0],identification[1], path));
            }
            br.close();
        }
        catch (IOException e) {
            MailManagerException ex = new MailManagerException("MailManager.getUsers : Can't open file : "+path+"logins.txt");
            System.out.println(ex.getMessage());
        }
    }
}
