package com.polytech4A.pop3.mailManager;

import com.polytech4A.pop3.mailManager.Exceptions.MalFormedMailException;

import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by Dimitri on 02/03/2015.
 * @version 1.1
 *          <p/>
 *          Mails exchange for POP3.
 */
public class Mail {

    /**
     * Header part of the mail.
     *  Concatenation of the subject, the sending Date, the recipient and the sender of the mail
     */
    public Header header;

    /**
     * Content of the mail
     */
    private String content;

    /**
     * String that contains the mail that will be send.
     */
    private StringBuffer output;

    /**
     * Getter of Content.
     *
     * @return Content String.
     */
    public String getContent() {
        return content;
    }

    /**
     * Getter of Header.
     *
     * @return Header.
     */
    public Header getHeader() {
        return header;
    }

    /**
     * Getter of Output.
     *
     * @return Output String.
     */
    public StringBuffer getOutput(){
        return output;
    }

    /**
     * Constructor of the Mail.
     */
    public Mail(String receiver, String sender, String content, String subject) {
        this.header=new Header(receiver, sender,subject);
        this.content = content;

        buildMail();
    }

    public Mail(String input) throws MalFormedMailException{
        String [] parsedContent = match (input, Header.END_LINE+Header.END_LINE);
        if (parsedContent != null){
            if (header.parseHeader(new StringBuffer(parsedContent[0]))){
                content =parsedContent[1];
            }
            else throw new MalFormedMailException("Mail Header MalFormed : expected parameters TO, FROM, SUBJECT, ORIG-DATE");
        }
    }

    /**
     * Build the mail class in concatenating the data
     */
    public void buildMail (){
        header.buildHeader();
        output.append(header.getOutput());
        output.append(Header.parseLine(content));
        output.append(Header.END_LINE);
    }

    public String[] match (String input, String parser){
        if (input.contains(parser)){
            return input.split(parser);

        }
        else return null;
    }


}
