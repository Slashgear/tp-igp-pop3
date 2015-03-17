package com.polytech4A.pop3.messages;

import com.polytech4A.pop3.messages.OkMessages.ServerReadyMessage;
import junit.framework.TestCase;

/**
 * Created by Antoine on 02/03/2015.
 * @author Antoine
 * @version 1.0
 *
 * Test case for ServerReadyMessage class.
 */
public class ServerReadyMessageTest extends TestCase {

    /**
     * ServerReadyMessage parsed.
     */
    protected ServerReadyMessage serverReadyMessageToParse;

    /**
     * ServerReadyMessage created from paramters.
     */
    protected ServerReadyMessage serverReadyMessageToCreate;

    /**
     * Set up of test.
     * @throws Exception
     */
    public void setUp() throws Exception {
        super.setUp();
        serverReadyMessageToParse =new ServerReadyMessage("+OK alpha 2015/01/15 15:20:18");
        serverReadyMessageToCreate =new ServerReadyMessage("beta","2018/20/17 20:34:27");
    }

    /**
     * Tear down of test.
     * @throws Exception
     */
    public void tearDown() throws Exception {
        super.tearDown();
        serverReadyMessageToCreate =null;
        serverReadyMessageToParse =null;
    }

    /**
     * Test of the creation of ServerReadyMessage.
     * @throws Exception
     */
    public void testCreate() throws Exception {
        assertEquals("+OK beta 2018/20/17 20:34:27",serverReadyMessageToCreate.toString());
        assertEquals(true,ServerReadyMessage.matches(serverReadyMessageToCreate.toString()));
        assertEquals("beta",serverReadyMessageToCreate.getServerName());

    }

    /**
     * Test of the Parsing.
     * @throws Exception
     */
    public void testParsing() throws Exception {
        assertEquals("+OK alpha 2015/01/15 15:20:18",serverReadyMessageToParse.toString());
        assertEquals(true,ServerReadyMessage.matches(serverReadyMessageToParse.toString()));
        assertEquals("alpha",serverReadyMessageToParse.getServerName());
    }
}
