package com.polytech4a.smtp.mailmanager.mail;

import com.polytech4a.smtp.mailmanager.exceptions.MalFormedMailException;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

/**
 * Created by Dimitri on 10/04/2015.
 */
public class HeaderTest {

    private Header header;
    private StringBuffer expectedOutput;

    @Before
    public void setUp() throws Exception {
        String receiver = "test_receiver",
                sender = "test sender with a very long mail address to check the line length building with the constant",
                subject = "test_subject";
        Date date = new Date();

        this.header = new Header(receiver, sender, subject);
        this.expectedOutput = new StringBuffer();
        this.expectedOutput.append("TO:" + receiver + Parameter.END_LINE);
        this.expectedOutput.append("FROM:test sender with a very long mail address to check the line length building" + Parameter.END_LINE + " with the constant" + Parameter.END_LINE);
        this.expectedOutput.append("SUBJECT:" + subject + Parameter.END_LINE);
        this.expectedOutput.append("ORIG-DATE:" + date + Parameter.END_LINE);
        this.expectedOutput.append(Parameter.END_LINE);
    }

    @Test
    public void testBuildHeader() throws Exception {
        TestCase.assertEquals("Test build header error", header.buildHeader().toString(), expectedOutput.toString());
    }

    @Test
    public void testConstructor() throws Exception {
        TestCase.assertEquals("Test Header constructor", new Header("blablabla" + expectedOutput.toString() + "blablablablaba").equals(header), true);
    }

    @Test(expected = MalFormedMailException.class)
    public void testConstructorError() throws Exception {
        TestCase.assertEquals("Test Header constructor must return error", new Header("blablablablaba").equals(header), true);
    }
}