package com.polytech4A.pop3.mailmanager;

import com.polytech4A.pop3.mailmanager.Exceptions.MalFormedMailException;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Dimitri on 04/03/2015.
 *  * @version 1.1
 *          <p/>
 *          Header of POP3 email.
 */
public class Header {

    /**
     * String that contains the header that will be send.
     *  Concatenation of the subject, the sending Date, the recipient and the sender of the mail
     */
    private StringBuffer output;

    private ArrayList<MailParameter> parameters;


    public Header(String receiver, String sender, String subject) {
        parameters = new ArrayList<MailParameter>();
        parameters.add(new MailParameterSender(sender));
        parameters.add(new MailParameterReceiver(receiver));
        parameters.add(new MailParameterSubject(subject));
    }


    public Header (String input) throws MalFormedMailException{
        parameters = new ArrayList<MailParameter>();
        parameters.add(new MailParameterSender(""));
        parameters.add(new MailParameterReceiver(""));
        parameters.add(new MailParameterSubject(""));
        parameters.add(new MailParameterDate(""));
        output=new StringBuffer(input);
        for (MailParameter param: parameters){
            if(!param.parseParameter(output.toString()))
                throw new MalFormedMailException("Header.Header(String input): The input string does not match the pop3 format for "+param.getClass().toString());
        }
    }

    /**
     * Build the mail Header
     */
    public StringBuffer buildHeader(){
        parameters.add(new MailParameterDate(new Date().toString()));
        for (MailParameter param : parameters){
            param.buildParameter();
            output.append(param.content);
        }
        output.append(MailParameter.END_LINE);
        return output;
    }
}
