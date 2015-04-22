package com.polytech4a.smtp.mailmanager;

import com.polytech4a.smtp.mailmanager.mail.Mail;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Dimitri on 16/04/2015.
 */
public class FacadeClientTest {

    @Test
    public void testCreateMail() throws Exception {
        String receiver = "test_receiver",
                sender = "test sender with a very long mail address to check the line length building with the constant",
                subject = "test_subject",
                content = "This is an email content test with an email incredibly long but I have no more ideas to lengthen so I'll just add random words. Hoover, Pineapple, Chameleon, Zephyr.\n" +
                        "Thank you for your attention.\n" +
                        "Best Regards,\n" +
                        "\n" +
                        "Patrick Henry";
        assertEquals("FacadeClient TestCreateMail", new Mail(receiver, sender, content, subject).getOutput().toString(), FacadeClient.createMail(sender, receiver, content, subject));
    }

    @Test
    public void testSaveMail() throws Exception {
        String receiver = "test_receiver",
                sender = "test sender with a very long mail address to check the line length building with the constant",
                subject = "test_subject",
                content = "This is an email content test with an email incredibly long but I have no more ideas to lengthen so I'll just add random words. Hoover, Pineapple, Chameleon, Zephyr.\n" +
                        "Thank you for your attention.\n" +
                        "Best Regards,\n" +
                        "\n" +
                        "Patrick Henry";
        FacadeClient.saveMail(new Mail(receiver, sender, content, subject).getOutput().toString(), receiver, "../OUTPUT/Client/");
    }
}