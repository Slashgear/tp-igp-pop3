package com.polytech4a.smtp.mailmanager;

import com.polytech4a.smtp.mailmanager.user.UserServer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by Dimitri on 14/04/2015.
 */
public class MailManagerTest {
    Client client;
    Server server;

    @Before
    public void setUp() throws Exception {
        client = new Client("a.caron@a.fr", "../OUTPUT/Client/");
        server = new Server("../OUTPUT/Server/");
    }

    @Test
    public void testInitUsers() throws Exception {
        testSaveUser();
        UserServer user = new UserServer("d.rodarie@a.fr", "test2", "../OUTPUT/Server/Server_mails/");
        UserServer user2 = new UserServer("a.caron@a.fr", "test", "../OUTPUT/Server/Server_mails/");

        //Test InitUsers
        ArrayList<UserServer> server_expected = new ArrayList<UserServer>();
        server_expected.add(user);
        server_expected.add(user2);
        server.initUsers();

        //Remove all the common mail to see differences
        assert (server.getUsers().size() == server_expected.size());
        for (int i = 0; i < server.getUsers().size(); i++) {
            assert (server.getUsers().get(i).getPath().equals(server_expected.get(i).getPath()) &&
                    server.getUsers().get(i).getLogin().equals(server_expected.get(i).getLogin()) &&
                    server.getUsers().get(i).getPassword().equals(server_expected.get(i).getPassword()));
        }
    }

    @Test
    public void testSaveMails() throws Exception {
        String mail = "TO:d.rodarie@a.fr\t\nFROM:test sender with a very long mail address to check the line length building\t\n" +
                " with the constant\t\nSUBJECT:test_subject\t\nORIG-DATE:Tue Apr 14 17:22:35 CEST 2015\t\n\t\n" +
                "This is an email content test with an email incredibly long but I have no\t\n" +
                " more ideas to lengthen so I'll just add random words. Hoover, Pineapple,\t\n" +
                " Chameleon, Zephyr.\nThank you for your attention.\nBest Regards,\n\nPatrick Henry\t\n\t\n\n.\n";

        server.saveMail(mail);
    }

    @Test
    public void testSaveUser() throws Exception {
        server.saveUser("a.caron@a.fr", "test");
        server.saveUser("d.rodarie@a.fr", "test2");
    }
}