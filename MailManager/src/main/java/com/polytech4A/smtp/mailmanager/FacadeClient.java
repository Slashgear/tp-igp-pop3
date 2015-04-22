package com.polytech4a.smtp.mailmanager;

import com.polytech4a.smtp.mailmanager.exceptions.MailManagerException;
import com.polytech4a.smtp.mailmanager.exceptions.MalFormedMailException;

/**
 * Created by Dimitri on 16/04/2015.
 */
public class FacadeClient {
    /**
     * Create a mail with the input parameters
     *
     * @param login    : client's mail address
     * @param receiver : receiver mail address
     * @param content  : content of the mail
     * @param subject  : subject of the mail
     * @return String : mail ready to be send
     * @throws MalFormedMailException
     */
    public static String createMail(String login, String receiver, String content, String subject) throws MalFormedMailException {
        try {
            return new Client(login).createMail(receiver, content, subject);
        } catch (MalFormedMailException e) {
            throw new MalFormedMailException("FacadeClient.createMail : The login, receiver and subject parameters must not be null : \n" + e.getMessage());
        }
    }

    /**
     * Check if the input mail is a valid one to save it into the Client directory
     *
     * @param mail       mail to save
     * @param login      client's address mail
     * @param outputPath output directory where mails are saved
     * @throws MailManagerException
     */
    public static void saveMail(String mail, String login, String outputPath) throws MailManagerException, MalFormedMailException {
        new Client(login, outputPath).addMail(mail);
    }
}
