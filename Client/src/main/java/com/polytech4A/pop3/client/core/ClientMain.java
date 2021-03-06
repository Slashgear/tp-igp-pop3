package com.polytech4A.pop3.client.core;

import com.polytech4A.pop3.client.ui.MainForm;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.HTMLLayout;
import org.apache.log4j.Level;

import java.io.IOException;

public class ClientMain {
    /**
     * Logger.
     */
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ClientMain.class);

    public static void main(String[] args){
        defineLogger();
        Client client = new Client();
        MainForm window = new MainForm(client);
        window.setVisible(true);
        client.addObserver(window);
    }

    private static void defineLogger(){
        HTMLLayout layout = new HTMLLayout();
        DailyRollingFileAppender appender = null;
        try {
            appender = new DailyRollingFileAppender(layout, "./Client_Log/log.html", "yyyy-MM-dd");
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.addAppender(appender);
        logger.setLevel(Level.DEBUG);
    }
}
