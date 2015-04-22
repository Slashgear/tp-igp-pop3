package com.polytech4a.smtp.mailmanager;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Dimitri on 16/04/2015.
 */
public class FacadeServerTest {

    @Test
    public void testExistsUser() throws Exception {
        assertEquals("FacadeServer testExistsUser must return true", true, FacadeServer.existsUser("d.rodarie@a.fr", "../OUTPUT/Server/"));
        assertEquals("FacadeServer testExistsUser must return true", false, FacadeServer.existsUser("d.rodarie@b.fr", "../OUTPUT/Server/"));
    }

    @Test
    public void testExistsUser1() throws Exception {
        assertEquals("FacadeServer testExistsUser must return true", true, FacadeServer.existsUser("d.rodarie@a.fr", "test2", "../OUTPUT/Server/"));
        assertEquals("FacadeServer testExistsUser must return true", false, FacadeServer.existsUser("d.rodarie@a.fr", "blabla", "../OUTPUT/Server/"));
    }

    @Test
    public void testGetUserMails() throws Exception {
        testSaveMail();
        assertEquals("FacadeServer testGetUserMails", 1, FacadeServer.getUserMails("d.rodarie@a.fr", "test2", "../OUTPUT/Server/").size());
        assertEquals("FacadeServer testGetUserMails", 1, FacadeServer.getUserMails("d.rodarie@a.fr", "../OUTPUT/Server/").size());
    }

    @Test
    public void testSaveMail() throws Exception {
        testSaveUser();
        String mail = "TO:d.rodarie@a.fr\t\nFROM:test sender with a very long mail address to check the line length building\t\n" +
                " with the constant\t\nSUBJECT:test_subject\t\nORIG-DATE:Tue Apr 14 17:22:35 CEST 2015\t\n\t\n" +
                "This is an email content test with an email incredibly long but I have no\t\n" +
                " more ideas to lengthen so I'll just add random words. Hoover, Pineapple,\t\n" +
                " Chameleon, Zephyr.\nThank you for your attention.\nBest Regards,\n\nPatrick Henry\t\n\t\n\n.\n";

        FacadeServer.saveMail(mail, "../OUTPUT/Server/");
    }

    @Test
    public void testSaveUser() throws Exception {
        FacadeServer.saveUser("d.rodarie@a.fr", "test2", "../OUTPUT/Server/");
    }
}