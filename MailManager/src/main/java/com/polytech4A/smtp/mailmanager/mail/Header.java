package com.polytech4a.smtp.mailmanager.mail;

import com.polytech4a.smtp.mailmanager.exceptions.MalFormedMailException;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Dimitri on 04/03/2015.
 * * @version 1.1
 * <p/>
 * Header of POP3 email.
 */
public class Header {

    /**
     * String that contains the header that will be send.
     * Concatenation of the subject, the sending Date, the recipient and the sender of the mail
     */
    private StringBuffer output;

    /**
     * Array that contains all the required parameter for the header
     */
    private ArrayList<Parameter> parameters;

    /**
     * Constructor for a mail's header
     *
     * @param receiver : String receiver of the mail
     * @param sender   : String sender of the mail
     * @param subject  : String subject of the mail
     */
    protected Header(String receiver, String sender, String subject) throws MalFormedMailException {
        this.parameters = new ArrayList<Parameter>();
        this.parameters.add(new ParameterReceiver(receiver));
        this.parameters.add(new ParameterSender(sender));
        this.parameters.add(new ParameterSubject(subject));
        this.parameters.add(new ParameterDate(new Date().toString()));
        this.output = new StringBuffer();
        buildHeader();
    }

    /**
     * Constructor for the mail Header
     * Initialize the header if the input string contains all the parameters required
     * else throw a MalFormedMailException
     *
     * @param input : String to initialize the header with
     * @throws MalFormedMailException
     */
    protected Header(String input) throws MalFormedMailException {
        this.parameters = new ArrayList<Parameter>();
        this.parameters.add(new ParameterReceiver(""));
        this.parameters.add(new ParameterSender(""));
        this.parameters.add(new ParameterSubject(""));
        this.parameters.add(new ParameterDate(""));
        this.output = new StringBuffer(input);
        for (Parameter param : parameters) {
            if (!param.parseParameter(input))
                throw new MalFormedMailException("Header.Header : The input string does not match the mail format for " + param.getClass().toString());
        }
    }

    protected String getReceiver() {
        return parameters.get(0).content;
    }

    /**
     * Build the mail Header and all its parameters
     *
     * @return StringBuffer : header built
     */
    protected StringBuffer buildHeader() {
        output = new StringBuffer();
        for (Parameter param : parameters) {
            output.append(param.buildParameter());
        }
        output.append(Parameter.END_LINE);
        return output;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Header) {
            Header header = (Header) o;
            for (int i = 0; i < header.parameters.size(); i++) {
                if (!header.parameters.get(i).equals(this.parameters.get(i))) return false;
            }
            return true;
        }
        return false;
    }
}
