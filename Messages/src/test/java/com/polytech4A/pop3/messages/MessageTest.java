package com.polytech4A.pop3.messages;

import junit.framework.TestCase;

/**
 * Created by Antoine on 02/03/2015.
 *
 * @author Antoine
 * @version 1.0
 *          <p/>
 *          Test case for Message Class.
 */
public class MessageTest extends TestCase {

    /**
     * Message to test.
     */
    protected Message message;

    /**
     * Setup of Test
     *
     * @throws Exception
     */
    public void setUp() throws Exception {
        super.setUp();
        message = new Message();
    }

    /**
     * Tear down of test.
     *
     * @throws Exception
     */
    public void tearDown() throws Exception {
        super.tearDown();
        message = null;
    }

    /**
     * Test of initialisation of a Message.
     *
     * @throws Exception
     */
    public void testInit() throws Exception {
        assertEquals("Message is set to default", "", message.toString());
    }

    /**
     * Test of initialisation of
     *
     * @throws Exception
     */
    public void testRegex() throws Exception {
        assertEquals("regex of Message is not correct", true, Message.matches(""));
        assertEquals("regex of Message is not correct", false, Message.matches("This is a test"));
    }
}
