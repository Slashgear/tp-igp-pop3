package com.polytech4A.pop3.mailmanager;

import java.util.ArrayList;

/**
 * Created by Dimitri on 03/03/2015.
 */
public class MailManager {

    private ArrayList<User> users;

    public MailManager() {
        this.users =User.getUsers();
    }

    public MailManager(User user){
        this.users = new ArrayList<User>();
        this.users.add(user);
    }

    public boolean initUser(User user){
        int index = users.indexOf(user);
        if (index>=0&&!users.get(index).isLocked()){
            user.lockUser();
            user.initMails();
            return true;
        }
        return false;
    }

    public boolean isLockedUser(User user){
        int index = users.indexOf(user);
        if (index>=0&&!users.get(index).isLocked()){
            return true;
        }
        return false;
    }

    public ArrayList<Mail> getMails (User user){
        int index = users.indexOf(user);
        if (index>=0&&users.get(index).isLocked()){

            ArrayList<Mail> mails = users.get(index).getMails();
            for (Mail mail : users.get(index).getMails()){
                users.get(index).deleteMail(mail);
            }
        }
        return null;
    }

    public void unlockUser(User user){
        int index = users.indexOf(user);
        if (index>=0&&users.get(index).isLocked()){
            users.get(index).unlockUser();
        }
    }

    public String createMail (User user, String receiver, String content, String subject){
        int index = users.indexOf(user);
        if (index>=0&&users.get(index).isLocked()){
            return users.get(index).createMail( receiver, content,  subject);
        }
        return null;
    }

    public void addMail (Mail mail,User user){
        int index = users.indexOf(user);
        if (index>=0&&users.get(index).isLocked()){
            users.get(index).addMail(mail);
        }
    }

    public void saveMails (User user){
        int index = users.indexOf(user);
        if (index>=0&&users.get(index).isLocked()){
            users.get(index).saveMails();
        }
    }
}
