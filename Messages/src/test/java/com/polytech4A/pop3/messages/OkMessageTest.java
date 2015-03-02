package com.polytech4A.pop3.messages;

import junit.framework.TestCase;

/**
 * Created by Antoine on 02/03/2015.
 * @author Antoine
 * @version 1.0
 *
 * Test case for OkMessage class.
 */
public class OkMessageTest extends TestCase{
    /**
     * OkMessage to test.
     */
    protected OkMessage okMessage;

    /**
     * Set up of test.
     * @throws Exception
     */
    public void setUp() throws Exception {
        super.setUp();
        okMessage=new OkMessage();
    }

    /**
     * Tear down of test.
     * @throws Exception
     */
    public void tearDown() throws Exception {
        super.tearDown();
        okMessage=null;
    }

    /**
     * Test of the regex of OkMessage.
     * @throws Exception
     */
    public void testRegex() throws Exception {
        assertEquals("+OK",okMessage.toString());
        assertEquals(true,OkMessage.matches(okMessage.toString()));
    }
}
