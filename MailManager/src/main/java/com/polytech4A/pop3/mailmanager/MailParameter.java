package com.polytech4A.pop3.mailmanager;

import java.util.regex.Pattern;

/**
 * Created by Dimitri on 09/03/2015.
 */
public abstract class MailParameter {

    /**
     * linefeed for POP3 mails
     */
    protected static final String END_LINE =System.getProperty("line.separator")+System.getProperty("line.separator");

    /**
     * Constant for the max number of characters per line
     */
    protected static final int MAX_LINE_LENGTH=78;

    protected String content;
    protected String parser;

    public MailParameter(String content, String parser){
        this.content = content;
        this.parser = parser;
    }

    public boolean parseParameter(String output){
        String[] tamp;
        if (Pattern.matches(parser + "\\S" + END_LINE, output)){
            tamp=output.split(parser);
            tamp= tamp[1].split(END_LINE);
            content= tamp[0];
            return true;
        }
        else return false;
    }

    public StringBuffer buildParameter(){
        StringBuffer output = new StringBuffer(parser);
        output.append(parseLine(content));
        return output;
    }

    /**
     * Check if the line has the right length
     * @param line : line to test
     * @return true if the line match the max number of characters
     */
    public boolean checkLineLength(String line){
        return line.length()<=MAX_LINE_LENGTH;
    }

    /**
     * Parse a line to the regular expression for POP3
     * @param line : line to parse
     * @return line parsed
     */
    public StringBuffer parseLine(String line) {
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
}
