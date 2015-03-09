package com.polytech4A.pop3.server.core;

import org.apache.log4j.*;

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
        Server server = new Server(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Boolean.parseBoolean(args[2]));
        logger.info("-----------------------Server started------------------------------------");
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
            appender = new DailyRollingFileAppender(layout, "./Server_Log/log.html", "yyyy-MM-dd");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ConsoleAppender ca=new ConsoleAppender();
        ca.setLayout(new SimpleLayout());
        ca.activateOptions();
        logger.addAppender(appender);
        logger.addAppender(ca);
        logger.setLevel(Level.DEBUG);
    }
}
