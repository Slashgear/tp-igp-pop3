package com.polytech4A.pop3.messages;

import junit.framework.TestCase;

/**
 * Created by Antoine on 02/03/2015.
 *@author Antoine
 *@version 1.0
 *
 * Test case of QuitMessage class.
 */
public class QuitMessageTest extends TestCase{
    /**
     * Quit message to test.
     */
    protected QuitMessage quitMessage;

    /**
     * Setup of test.
     * @throws Exception
     */
    public void setUp() throws Exception {
        super.setUp();
        quitMessage = new QuitMessage();
    }

    /**
     * Tear down of test.
     * @throws Exception
     */
    public void tearDown() throws Exception {
        super.tearDown();
        quitMessage = null;
    }

    public void testRegex() throws Exception {
        assertEquals("QUIT",quitMessage.toString());
        assertEquals(true,QuitMessage.matches(quitMessage.toString()));

    }
}
