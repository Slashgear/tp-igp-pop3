package com.polytech4A.pop3.mailmanager;

import com.polytech4A.pop3.mailmanager.Exceptions.MailManagerException;
import com.polytech4A.pop3.mailmanager.Exceptions.MalFormedMailException;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

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

    public User(String login) {
        this.login = login;
        this.password="";
        this.mails=new ArrayList<Mail>();
    }

    public ArrayList<Mail> getMails() {
        return mails;
    }

    public void addMail(Mail mail){
        mails.add(mail);
    }

    public void deleteMail(Mail mail){
        mails.remove(mail);
    }

    public boolean isLocked (){
        File f = new File("Mails/"+login+"/lock.txt");
        return f.exists() && !f.isDirectory();
    }

    public boolean lockUser (){
        File f = new File("Mails/"+login+"/lock.txt");
        if(!f.exists()&& !f.isDirectory()) {
            try {
                return f.createNewFile();
            } catch (IOException e) {
                MailManagerException ex = new MailManagerException("User.lockUser : Can't create file : Mails/"+login+"/lock.txt");
                System.out.println(ex.getMessage());
            }
        }
        return false;
    }

    public boolean unlockUser(){
        File f = new File("Mails/"+login+"/lock.txt");
        if(f.exists()&& !f.isDirectory()) {
            if (f.delete()){
                return true;
            }
        }
        return false;
    }

    public void saveMails (){
        Date date = new Date();

        try {
            BufferedWriter file = new BufferedWriter(new FileWriter("Mails/"+login+"/mails_"+date.toString()+".txt", true));
            for (Mail mail : mails) {
                file.write(mail.getOutput().toString());
                file.newLine();
            }
            file.close();

        } catch (IOException e) {
            MailManagerException ex = new MailManagerException("User.saveMail : Can't create file : Mails/"+login+"/mails_"+date.toString()+".txt");
            System.out.println(ex.getMessage());
        }
    }

    public static ArrayList<User> getUsers(){
        ArrayList<User> users = new ArrayList<User>();
        try{
            InputStream ips=new FileInputStream("Mails/logins.txt");
            InputStreamReader ipsr=new InputStreamReader(ips);
            BufferedReader br=new BufferedReader(ipsr);
            String line;
            String[] identification;

            while ((line=br.readLine())!=null){
                identification=line.split(" ");
                users.add(new User(identification[0],identification[1]));
            }
            br.close();
        }
        catch (IOException e) {
            MailManagerException ex = new MailManagerException("User.getUser : Can't open file : Mails/logins.txt");
            System.out.println(ex.getMessage());
        }
        return users;
    }

    public void initMails(){
        File folder = new File ("Mails/"+login);
        try{
            for (File fileEntry : folder.listFiles()) {
                if (!fileEntry.isDirectory()) {
                    mails.add(new Mail(new Scanner(new File(fileEntry.getName())).useDelimiter("\\Z").next()));
                }
            }
        } catch (FileNotFoundException e) {
            MailManagerException ex = new MailManagerException("User.initMails : File in 'Mails/"+login+"' not found");
            System.out.println(ex.getMessage());
        } catch (MalFormedMailException e) {
            MailManagerException ex = new MailManagerException("User.InitMail : " + e.getMessage());
            System.out.println(ex.getMessage());
        }
    }

    public String createMail(String receiver, String content,String subject){
        Mail mail = new Mail(this.login, receiver,content,subject);
        return mail.getOutput().toString();
    }
}
