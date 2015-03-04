package com.polytech4A.pop3.mailManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Dimitri on 04/03/2015.
 */
public class User {
    private String login;
    private String password;
    private ArrayList<Mail> mails;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        this.mails=new ArrayList<Mail>();
    }

    public void addMail(Mail mail){
        mails.add(mail);
    }

    public void deleteMail(Mail mail){
        mails.remove(mail);
    }

    public boolean findUser(){
        return false;
    }

    public void saveUser(){
        try {
            BufferedWriter fichier = new BufferedWriter(new FileWriter("Mails/logins.txt"));
            fichier.write("bonjour tout le monde");
            fichier.newLine();

            fichier.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveMails (){
        Date date = new Date();
        try {
            BufferedWriter fichier = new BufferedWriter(new FileWriter("Mails/"+login+"/mails_"+date.toString()+".txt"));

            fichier.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
