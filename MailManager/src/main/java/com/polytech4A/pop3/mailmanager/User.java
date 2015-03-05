package com.polytech4A.pop3.mailmanager;

import java.io.*;
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
        try{
            InputStream ips=new FileInputStream("Mails/logins.txt");
            InputStreamReader ipsr=new InputStreamReader(ips);
            BufferedReader br=new BufferedReader(ipsr);
            String line;
            String[] ident;

            while ((line=br.readLine())!=null){
                ident=line.split(" ");
                if(login==ident[0]&&password==ident[1]){
                    return true;
                }
            }
            br.close();
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
        return false;
    }

    public void saveUser(){
        StringBuffer line = new StringBuffer(login);
        line.append(" ");
        line.append(password);
        try {
            BufferedWriter fichier = new BufferedWriter(new FileWriter("Mails/logins.txt", true));
            fichier.newLine();
            fichier.write(line.toString());
            fichier.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveMails (){
        Date date = new Date();

        try {
            BufferedWriter fichier = new BufferedWriter(new FileWriter("Mails/"+login+"/mails_"+date.toString()+".txt", true));
            for (int i=0; i<mails.size(); i++){
                fichier.write(mails.get(i).getOutput().toString());
                fichier.newLine();
            }
            fichier.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
