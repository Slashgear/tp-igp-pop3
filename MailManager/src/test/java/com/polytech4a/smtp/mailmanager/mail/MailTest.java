package com.polytech4a.smtp.mailmanager.mail;

import com.polytech4a.smtp.mailmanager.exceptions.MalFormedMailException;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

/**
 * Created by Dimitri on 10/04/2015.
 */
public class MailTest {
    private Mail mail;
    private StringBuffer expectedOutput;

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
        Date date = new Date();

        this.mail = new Mail(receiver, sender,
                content, subject);
        this.expectedOutput = new StringBuffer();
        this.expectedOutput.append("TO:" + receiver + Parameter.END_LINE);
        this.expectedOutput.append("FROM:test sender with a very long mail address to check the line length building" + Parameter.END_LINE + " with the constant" + Parameter.END_LINE);
        this.expectedOutput.append("SUBJECT:" + subject + Parameter.END_LINE);
        this.expectedOutput.append("ORIG-DATE:" + date + Parameter.END_LINE);
        this.expectedOutput.append(Parameter.END_LINE);
        this.expectedOutput.append("This is an email content test with an email incredibly long but I have no" + Parameter.END_LINE
                + " more ideas to lengthen so I'll just add random words. Hoover, Pineapple," + Parameter.END_LINE
                + " Chameleon, Zephyr.\nThank you for your attention.\nBest Regards,\n\nPatrick Henry");
        this.expectedOutput.append(Parameter.END_LINE);
        this.expectedOutput.append(Parameter.END_LINE);
        this.expectedOutput.append(Mail.END_MAIL);
    }

    @Test
    public void testConstructor() throws Exception {
        TestCase.assertEquals("Test Mail constructor", new Mail("blablabla" + expectedOutput.toString() + "blablablablaba").equals(mail), true);
    }

    @Test
    public void testBuildParameter() throws Exception {
        TestCase.assertEquals("Test build header error", expectedOutput.toString(), mail.buildParameter().toString());
    }

    @Test(expected = MalFormedMailException.class)
    public void testConstructorError() throws Exception {
        TestCase.assertEquals("Test Mail constructor must return error", new Mail("blablablablaba").equals(mail), true);
    }
}