package com.polytech4A.pop3.mailmanager;

import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by Dimitri on 04/03/2015.
 */
public class Header {

    /**
     * linefeed for POP3 mails
     */
    public static final String END_LINE =System.getProperty("line.separator")+System.getProperty("line.separator");

    /**
     * Constant for the max number of characters per line
     */
    public static final int MAX_LINE_LENGTH=78;

    /**
     *  Receiver of the mail
     */
    private String receiver;

    /**
     * Sender of the mail
     */
    private String sender;

    /**
     * Subject of the mail
     */
    private String subject;

    /**
     * Date of sending
     */
    private Date sendingDate;

    /**
     * String that contains the header that will be send.
     *  Concatenation of the subject, the sending Date, the recipient and the sender of the mail
     */
    private StringBuffer output;

    /**
     * Getter of Receiver.
     *
     * @return Receiver String.
     */
    public String getReciever() {
        return receiver;
    }

    /**
     * Getter of Sender.
     *
     * @return Sender String.
     */
    public String getSender() {
        return sender;
    }

    /**
     * Getter of Subject.
     *
     * @return Subject String.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Getter of SendingDate.
     *
     * @return SendingDate Date.
     */
    public Date getSendingDate() {
        return sendingDate;
    }

    /**
     * Getter of Output.
     *
     * @return Output String.
     */
    public StringBuffer getOutput(){
        return output;
    }

    public Header(String receiver, String sender, String subject) {
        this.receiver = receiver;
        this.sender = sender;
        this.subject = subject;
    }

    /**
     * Check if the line has the right length
     * @param line : line to test
     * @return true if the line match the max number of characters
     */
    public static boolean checkLineLength(String line){
        return line.length()<=MAX_LINE_LENGTH;
    }

    /**
     * Parse a line to the regular expression for POP3
     * @param line : line to parse
     * @return line parsed
     */
    public static StringBuffer parseLine(String line) {
        int i=0;
        StringBuffer res=new StringBuffer();
        while (!checkLineLength(line.substring(i))) {
            res.append(line.substring(i,MAX_LINE_LENGTH));
            res.append(END_LINE);
            i+=MAX_LINE_LENGTH;
        }
        res.append(line.substring(i));

        return res.append(END_LINE);
    }

    public boolean parseReceiver(){
        String[] tamp;
        if (Pattern.matches("TO:\\S" + END_LINE, output)){
            tamp=output.toString().split("TO:");
            tamp=tamp[1].toString().split(END_LINE);
            receiver=tamp[0];
            return true;
        }
        else return false;
    }

    public boolean parseSender(){
        String[] tamp;
        if (Pattern.matches("FROM:\\S"+END_LINE,output)){
            tamp=output.toString().split("FROM:");
            tamp=tamp[1].toString().split(END_LINE);
            sender=tamp[0];
            return true;
        }
        else return false;
    }

    public boolean parseSubject(){
        String[] tamp;
        if (Pattern.matches("SUBJECT:\\S"+END_LINE,output)){
            tamp=output.toString().split("SUBJECT:");
            tamp=tamp[1].toString().split(END_LINE);
            subject=tamp[0];
            return true;
        }
        else return false;
    }

    public boolean parseDate(){
        String[] tamp;
        if (Pattern.matches("ORIG-DATE:\\S"+END_LINE,output)){
            tamp=output.toString().split("ORIG-DATE:");
            tamp=tamp[1].toString().split(END_LINE);
            sendingDate= new Date(tamp[0]);
            return true;
        }
        else return false;
    }

    public boolean parseHeader (StringBuffer header){
        this.output=header;
        return parseSender()&&parseReceiver()&&parseSubject()&&parseDate();
    }

    public void buildHeader(){
        sendingDate=new Date();
        output=new StringBuffer();
        output.append("FROM:");
        output.append(parseLine(sender));
        output.append("TO:");
        output.append(parseLine(receiver));
        output.append("SUBJECT:");
        output.append(parseLine(subject));
        output.append("ORIG-DATE:");
        output.append(parseLine(sendingDate.toString()));
        output.append(END_LINE);
    }
}
