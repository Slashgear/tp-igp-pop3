package com.polytech4A.pop3.messages;

import junit.framework.TestCase;

/**
 * Created by Antoine on 05/03/2015.
 *
 * @author Antoine
 * @version 1.0
 *          <p/>
 *          Test case for AlreadyLockedErrMessage class.
 */
public class AlreadyLockedErrMessageTest extends TestCase {
    /**
     * AlreadyLocked Message to test.
     */
    private AlreadyLockedErrMessage alreadyLockedErrMessage;

    /**
     * Setup of Test
     *
     * @throws Exception
     */
    public void setUp() throws Exception {
        super.setUp();
        alreadyLockedErrMessage = new AlreadyLockedErrMessage();
    }

    /**
     * Tear down of test.
     *
     * @throws Exception
     */
    public void tearDown() throws Exception {
        super.tearDown();
        alreadyLockedErrMessage = null;
    }

    /**
     * Test case for parsing.
     *
     * @throws Exception
     */
    public void testParse() throws Exception {
        assertEquals(true, AlreadyLockedErrMessage.matches("-ERR Already locked mailbox"));
        assertEquals(false, AlreadyLockedErrMessage.matches("-err already locked mailbox"));
    }

    /**
     * Test case for constructing.
     *
     * @throws Exception
     */
    public void testName() throws Exception {
        assertEquals("-ERR Already locked mailbox", alreadyLockedErrMessage.toString());
        assertEquals(true, AlreadyLockedErrMessage.matches(alreadyLockedErrMessage.toString()));
    }
}
