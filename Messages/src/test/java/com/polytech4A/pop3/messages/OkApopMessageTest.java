package com.polytech4A.pop3.messages;

import junit.framework.TestCase;

/**
 * Created by Antoine on 02/03/2015.
 *
 * @author Antoine
 * @version 1.0
 *          <p/>
 *          Test case for OkApopMessage class.
 */
public class OkApopMessageTest extends TestCase {

    /**
     * Message to Parse.
     */
    protected OkApopMessage okApopMessageToParse;

    /**
     * Message to Create.
     */
    protected OkApopMessage okApopMessageToCreate;

    /**
     * Setup of Test
     *
     * @throws Exception
     */
    public void setUp() throws Exception {
        super.setUp();
        okApopMessageToCreate = new OkApopMessage("alpha", 12, 670);
        okApopMessageToParse = new OkApopMessage("+OK alpha's maildrop has 12 messages (670 octets)");
    }

    /**
     * Tear down of test.
     *
     * @throws Exception
     */
    public void tearDown() throws Exception {
        super.tearDown();
        okApopMessageToCreate = null;
        okApopMessageToParse = null;
    }

    /**
     * Test of Parsing Message.
     * @throws Exception
     */
    public void testParse() throws Exception {
        assertEquals(true, OkApopMessage.matches(okApopMessageToParse.toString()));
        assertEquals("alpha", okApopMessageToParse.getId());
        assertEquals(12, okApopMessageToParse.getNbMessage().intValue());
        assertEquals(670, okApopMessageToParse.getMessageSize().intValue());
        assertEquals("+OK alpha's maildrop has 12 messages (670 octets)", okApopMessageToParse.toString());
    }

    /**
     * Test of creation of OkApopMessage by parameters.
     * @throws Exception
     */
    public void testCreate() throws Exception {
        assertEquals(true, OkApopMessage.matches(okApopMessageToCreate.toString()));
        assertEquals("alpha", okApopMessageToCreate.getId());
        assertEquals(12, okApopMessageToCreate.getNbMessage().intValue());
        assertEquals(670, okApopMessageToCreate.getMessageSize().intValue());
        assertEquals("+OK alpha's maildrop has 12 messages (670 octets)", okApopMessageToCreate.toString());

    }
}
