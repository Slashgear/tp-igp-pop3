package com.polytech4a.smtp.mailmanager.user;

import com.polytech4a.smtp.mailmanager.mail.Mail;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by Dimitri on 13/04/2015.
 */
public class UserTest {
    private Mail mail, mail2;
    private UserServer userServer;
    private UserClient userClient;

    @Before
    public void setUp() throws Exception {
        String receiver = "test_receiver",
                sender = "test sender with a very long mail address to check the line length building with the constant",
                subject = "test_subject",
                content = "This is an email content test with an email incredibly long but I have no more ideas to lengthen so I'll just add random words. Hoover, Pineapple, Chameleon, Zephyr.\n" +
                        "Thank you for your attention.\n" +
                        "Best Regards,\n" +
                        "\n" +
                        "Patrick Henry";

        this.mail = new Mail(receiver, sender,
                content, subject);
        this.mail2 = new Mail(receiver + "2", sender, content, subject);
        this.userServer = new UserServer("a.caron@a.fr", "test", "../OUTPUT/Server/Server_mails/");
        this.userClient = new UserClient("a.caron@a.fr", "../OUTPUT/Client/Client_mails/");
    }

    @Test
    public void testSaveMails() throws Exception {
        Calendar calendar = Calendar.getInstance();
        userServer.addMail(mail.getOutput().toString());
        userClient.addMail(mail.getOutput().toString());
        userServer.addMail(mail2.getOutput().toString());
        userServer.saveMails();
        assertEquals("Test Save mails Server", true, new File(userServer.path).exists() && new File(userServer.path + "/mail_" + calendar.get(Calendar.DAY_OF_MONTH) + (calendar.get(Calendar.MONTH) + 1) + calendar.get(Calendar.YEAR) + "_0.txt").exists());
        userClient.saveMails();
        assertEquals("Test Save mails Server", true, new File(userClient.path).exists() && new File(userClient.path + "/mail_" + calendar.get(Calendar.DAY_OF_MONTH) + (calendar.get(Calendar.MONTH) + 1) + calendar.get(Calendar.YEAR) + "_0.txt").exists());
    }

    @Test
    public void testInitMails() throws Exception {
        testSaveMails();
        ArrayList<Mail> mails_Server = new ArrayList<Mail>(), mails_Client = new ArrayList<Mail>();
        mails_Client.add(mail);
        mails_Server.add(mail);
        mails_Server.add(mail2);

        //Remove all the common mail to see differences
        mails_Client.removeAll(userClient.initMails());
        mails_Server.removeAll(userServer.initMails());
        assertEquals("Test Init Mails Client", true, mails_Client.size() == 0);
        assertEquals("Test Init Mails Server", true, mails_Server.size() == 0);
    }

    @Test
    public void testLock() throws Exception {
        assertEquals("Test is lock with user not lock", false, userServer.isLocked());
        assertEquals("Test lock with user not lock", true, userServer.lockUser());
        assertEquals("Test is lock with user not lock", true, userServer.isLocked());
        assertEquals("Test lock with user not lock", false, userServer.lockUser());
        assertEquals("Test lock with user not lock", true, userServer.unlockUser());
        assertEquals("Test lock with user not lock", false, userServer.unlockUser());
    }
}