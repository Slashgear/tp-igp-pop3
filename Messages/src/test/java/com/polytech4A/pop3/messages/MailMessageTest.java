package com.polytech4A.pop3.messages;

import junit.framework.TestCase;

/**
 * Created by Antoine on 02/03/2015.
 * @author Antoine
 * @version 1.0
 *
 * Test case for MailMessageTest.
 */
public class MailMessageTest extends TestCase{

    /**
     * Message created.
     */
    private MailMessage mailMessageToTest;

    /**
     * Message parsed.
     */
    private MailMessage mailMessageToParse;

    /**
     * Set up method.
     * @throws Exception
     */
    public void setUp() throws Exception {
        super.setUp();
        mailMessageToParse=new MailMessage("+OK 899 octets");
        mailMessageToTest =new MailMessage(978);
    }

    /**
     * Tear Down Method.
     * @throws Exception
     */
    public void tearDown() throws Exception {
        super.tearDown();
        mailMessageToParse=null;
        mailMessageToTest =null;
    }

    /**
     * Test case for parsing String into a Mail Message.
     * @throws Exception
     */
    public void testParse() throws Exception {
        assertEquals(899,mailMessageToParse.getSize().intValue());
        assertEquals(mailMessageToParse.toString(),"+OK 899 octets");
        assertEquals(true,MailMessage.matches(mailMessageToParse.toString()));
    }

    /**
     * Test case for creation of MailMessage.
     * @throws Exception
     */
    public void testCreate() throws Exception {
        assertEquals("+OK 978 octets", mailMessageToTest.toString());
        assertEquals(true,MailMessage.matches(mailMessageToTest.toString()));
        assertEquals(978, mailMessageToTest.getSize().intValue());
    }
}
