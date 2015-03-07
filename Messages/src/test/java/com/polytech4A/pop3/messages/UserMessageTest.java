package com.polytech4A.pop3.messages;

import junit.framework.TestCase;

/**
 * Created by Antoine on 07/03/2015.
 * @author Antoine
 * @version 1.0
 *
 * Test case for UserMessage.
 */
public class UserMessageTest extends TestCase{

    /**
     * Message to parse.
     */
    private UserMessage userMessageToParse;

    /**
     * User Message created.
     */
    private UserMessage userMessageToCreate;

    /**
     * Set up.
     * @throws Exception
     */
    public void setUp() throws Exception {
        super.setUp();
        userMessageToCreate=new UserMessage("Antoine");
        userMessageToParse=new UserMessage("USER Adrien");
    }

    /**
     * Tear down.
     * @throws Exception
     */
    public void tearDown() throws Exception {
        super.tearDown();
        userMessageToCreate=null;
        userMessageToParse=null;
    }

    /**
     * Test case for parsing String into User message.
     * @throws Exception
     */
    public void testParse() throws Exception {
        assertEquals(true,UserMessage.matches(userMessageToParse.toString()));
        assertEquals("Adrien",userMessageToParse.getId());
    }

    public void testCreate() throws Exception {
        assertEquals(true,UserMessage.matches(userMessageToCreate.toString()));
        assertEquals("Antoine",userMessageToCreate.getId());
    }
}
