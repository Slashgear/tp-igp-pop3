package com.polytech4A.pop3.messages;

import junit.framework.TestCase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        apopMessageToCreate = new ApopMessage("Antoine", "CARON",dateFormat.format(cal.getTime()));
    }

    public void tearDown() throws Exception {
        super.tearDown();
        apopMessageToCreate = null;
        apopMessageToParse = null;
    }

    public void testParsing() throws Exception {
        assertEquals("Adrien", apopMessageToParse.getId());
    }

    public void testCreate() throws Exception {
        assertEquals("Antoine", apopMessageToCreate.getId());
        assertEquals("CARON", apopMessageToCreate.getPassword());
    }
}
