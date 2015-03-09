package com.polytech4A.pop3.mailmanager;

import com.polytech4A.pop3.mailmanager.Exceptions.MalFormedMailException;

/**
 * Created by Dimitri on 02/03/2015.
 * @version 1.1
 *          <p/>
 *          Mails exchanged for POP3.
 */
public class Mail extends MailParameter {

    /**
     * Header part of the mail.
     *  Concatenation of the subject, the sending Date, the recipient and the sender of the mail
     */
    private Header header;

    /**
     * String that contains the mail that will be send.
     */
    private StringBuffer output;

    /***
     * Getter of the header of the mail
     * @return
     */
    public Header getHeader(){
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
        super("", MailParameter.END_LINE+ MailParameter.END_LINE);
        this.header=new Header(receiver, sender,subject);
        this.content = content;
        this.output = buildParameter();
    }

    public Mail(String input) throws MalFormedMailException{
        super(input, MailParameter.END_LINE+ MailParameter.END_LINE);
        if (!parseParameter (input)){
            throw new MalFormedMailException("Mail Header MalFormed : expected parameters TO, FROM, SUBJECT, ORIG-DATE");
        }
    }

    @Override
    public StringBuffer buildParameter (){
        StringBuffer res = new StringBuffer(header.buildHeader());
        res.append(parseLine(content));
        return res.append(MailParameter.END_LINE);
    }

    @Override
    public boolean parseParameter (String output){
        String [] tamp;
        if (output.contains(parser)){
            tamp = output.split(parser);
            try {
                header = new Header (tamp[0]);
            }catch (MalFormedMailException ex){
                System.out.println("Mail.parseParameter : Failed to parse header : " + ex.getMessage());
                return false;
            }
            content = tamp[1];
            return true;
        }
        else return false;
    }


}
