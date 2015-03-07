package com.polytech4A.pop3.server.core;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.HTMLLayout;
import org.apache.log4j.Level;

import java.io.IOException;

/**
 * Created by Antoine on 04/03/2015.
 *
 * @author Antoine
 * @version 1.0
 *          <p/>
 *          Main Class of a POP3 server.
 */
public class Main {
    /**
     * Logger.
     */
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Server.class);

    /**
     * Main function.
     *
     * @param args arg0 = port, arg1 = nbConnection, arg2 = boolDeleteMsg
     */
    public static void main(String[] args) {
        defineLogger();
        Server server = new Server(110, 10);
        server.listen();
        server.close();
    }

    /**
     * Define the Logger Appender for LOG4j
     */
    private static void defineLogger() {
        HTMLLayout layout = new HTMLLayout();
        DailyRollingFileAppender appender = null;
        try {
            appender = new DailyRollingFileAppender(layout, "/Server_Log/log", "yyyy-MM-dd");
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.addAppender(appender);
        logger.setLevel(Level.DEBUG);
    }
}
