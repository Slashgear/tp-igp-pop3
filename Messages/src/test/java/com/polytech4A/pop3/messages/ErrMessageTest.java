package com.polytech4A.pop3.messages;

import junit.framework.TestCase;

/**
 * Created by Antoine on 02/03/2015.
 *
 * @author Antoine
 * @version 1.0
 *          <p/>
 *          Test case for ErrMessage Class.
 */
public class ErrMessageTest extends TestCase {
    /**
     * Error message to parse.
     */
    protected ErrMessage errMessagetoParse;

    /**
     * Error message to create.
     */
    protected ErrMessage errMessagetocreate;


    /**
     * Setup of Test
     *
     * @throws Exception
     */
    public void setUp() throws Exception {
        super.setUp();
        errMessagetoParse = new ErrMessage("Acces Denied");
        errMessagetocreate = new ErrMessage("-ERR No Acces");
    }

    /**
     * Tear down of test.
     *
     * @throws Exception
     */
    public void tearDown() throws Exception {
        super.tearDown();
        errMessagetocreate = null;
        errMessagetoParse = null;
    }

    /**
     * Test of creation of a ErrMessage with Error message in Parameter.
     *
     * @throws Exception
     */
    public void testCreateWithErrorInParam() throws Exception {
        assertEquals("-ERR No Acces", errMessagetocreate.toString());
        assertEquals("No Acces", errMessagetocreate.getErrorMessage());
    }

    /**
     * Test of creation of a ErrMessage with a well-formed ErrMessage.
     *
     * @throws Exception
     */
    public void testParsing() throws Exception {
        assertEquals("-ERR Acces Denied", errMessagetoParse.toString());
        assertEquals("Acces Denied", errMessagetoParse.getErrorMessage());
    }
}
