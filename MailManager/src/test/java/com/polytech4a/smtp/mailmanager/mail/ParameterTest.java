package com.polytech4a.smtp.mailmanager.mail;

import junit.framework.TestCase;

import java.util.Date;

/**
 * Created by Dimitri on 06/04/2015.
 */
public class ParameterTest extends TestCase {
    private Parameter dateParameter;
    private Parameter subjectParameter;
    private Parameter senderParameter;
    private Parameter receiverParameter;

    @org.junit.Before
    public void setUp() throws Exception {
        this.dateParameter = new ParameterDate(new Date().toString());
        this.subjectParameter = new ParameterSubject("test_subject");
        this.senderParameter = new ParameterSender("test sender with a very long mail address to check the line length building with the constant");
        this.receiverParameter = new ParameterReceiver("test_receiver");
    }

    @org.junit.Test
    public void testParseParameter() throws Exception {
        String date = dateParameter.content,
                subject = subjectParameter.content,
                sender = senderParameter.content,
                receiver = receiverParameter.content;
        assertEquals("Test parse date : 'ORIG-DATE:" + date + "' must return true", true, dateParameter.parseParameter("ORIG-DATE:" + date + Parameter.END_LINE));
        assertEquals("Test parse date : 'DATEORIG:" + date + "' must return false (wrong parser)", false, dateParameter.parseParameter("DATEORIG:" + date + Parameter.END_LINE));

        assertEquals("Test parse subject : 'SUBJECT:" + subject + "' must return true", true, subjectParameter.parseParameter("SUBJECT:" + subject + Parameter.END_LINE));
        assertEquals("Test parse subject : 'SUBJECT:" + subject + "' must return false (no endline)", false, subjectParameter.parseParameter("SUBJECT:" + subject));

        assertEquals("Test parse sender : 'FROM:" + sender + "' must return true", true, senderParameter.parseParameter("FROM:" + sender + Parameter.END_LINE));
        assertEquals("Test parse sender : 'FROM:' must return false (no content)", false, senderParameter.parseParameter("FROM:" + Parameter.END_LINE));

        assertEquals("Test parse receiver : 'TO:" + receiver + "' must return true", true, receiverParameter.parseParameter("TO:" + receiver + Parameter.END_LINE));
        assertEquals("Test parse receiver :  must return false (null)", false, receiverParameter.parseParameter(null));
    }

    @org.junit.Test
    public void testBuildParameter() throws Exception {
        assertEquals("Test build date error", "ORIG-DATE:" + dateParameter.content + Parameter.END_LINE, dateParameter.buildParameter().toString());
        assertEquals("Test build subject error", "SUBJECT:" + subjectParameter.content + Parameter.END_LINE, subjectParameter.buildParameter().toString());
        assertEquals("Test build sender error", "FROM:test sender with a very long mail address to check the line length building" + Parameter.END_LINE + " with the constant" + Parameter.END_LINE, senderParameter.buildParameter().toString());
        assertEquals("Test build receiver error", "TO:" + receiverParameter.content + Parameter.END_LINE, receiverParameter.buildParameter().toString());
    }
}