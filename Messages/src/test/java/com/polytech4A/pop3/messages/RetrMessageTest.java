package com.polytech4A.pop3.messages;

import junit.framework.TestCase;

/**
 * Created by Antoine on 02/03/2015.
 * @author Antoine
 * @version 1.0
 *
 * Test for RetrMessage class.
 */
public class RetrMessageTest extends TestCase{

    /**
     * RETR message to Parse.
     */
    protected RetrMessage retrMessageToParse;

    /**
     * RETR message to create with info in parameters.
     */
    protected RetrMessage retrMessageToCreate;

    /**
     * Set up of test.
     * @throws Exception
     */
    public void setUp() throws Exception {
        super.setUp();
        retrMessageToCreate=new RetrMessage(13);
        retrMessageToParse = new RetrMessage("RETR 13");
    }

    /**
     * Tear down of test.
     * @throws Exception
     */
    public void tearDown() throws Exception {
        super.tearDown();
        retrMessageToParse = null;
        retrMessageToCreate = null;
    }

    public void testParse() throws Exception {
        assertEquals(true,RetrMessage.matches(retrMessageToParse.toString()));
        assertEquals("RETR 13",retrMessageToParse.toString());
        assertEquals(13,retrMessageToParse.getNoMessages().intValue());
        assertEquals(false,RetrMessage.matches("Ceci est un test."));
    }

    public void testCreate() throws Exception {
        assertEquals(true,RetrMessage.matches(retrMessageToCreate.toString()));
        assertEquals("RETR 13",retrMessageToCreate.toString());
        assertEquals(13,retrMessageToCreate.getNoMessages().intValue());
    }
}
