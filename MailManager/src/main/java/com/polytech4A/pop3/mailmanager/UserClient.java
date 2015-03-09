package com.polytech4A.pop3.mailmanager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Dimitri on 09/03/2015.
 */
public class UserClient extends User {
    public UserClient (String login, String path){
        super(login,"",path);
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
}
