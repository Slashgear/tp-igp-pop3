package com.polytech4A.pop3.messages;

import junit.framework.TestCase;

/**
 * Created by Antoine on 02/03/2015.
 *
 * @author Antoine
 * @version 1.0
 *          <p/>
 *          Test case for ApopMessageTest.
 */
public class ApopMessageTest extends TestCase {

    protected ApopMessage apopMessageToParse;

    protected ApopMessage apopMessageToCreate;

    public void setUp() throws Exception {
        super.setUp();
        apopMessageToParse = new ApopMessage("APOP Adrien CHAUSSENDE");
        apopMessageToCreate = new ApopMessage("Antoine", "CARON");
    }

    public void tearDown() throws Exception {
        super.tearDown();
        apopMessageToCreate = null;
        apopMessageToParse = null;
    }

    public void testParsing() throws Exception {
        assertEquals("APOP Adrien CHAUSSENDE", apopMessageToParse.toString());
        assertEquals("Adrien", apopMessageToParse.getId());
        assertEquals("CHAUSSENDE", apopMessageToParse.getPassword());
    }

    public void testCreate() throws Exception {
        assertEquals("APOP Antoine CARON", apopMessageToCreate.toString());
        assertEquals("Antoine", apopMessageToCreate.getId());
        assertEquals("CARON", apopMessageToCreate.getPassword());
    }
}
