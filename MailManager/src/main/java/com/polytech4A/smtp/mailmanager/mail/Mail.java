package com.polytech4a.smtp.mailmanager.mail;

import com.polytech4a.smtp.mailmanager.exceptions.MalFormedMailException;

/**
 * Created by Dimitri on 02/03/2015.
 *
 * @version 1.1
 *          <p/>
 *          Mails exchanged for POP3.
 */
public class Mail extends Parameter {

    /**
     * Header part of the mail.
     * Concatenation of the subject, the sending Date, the recipient and the sender of the mail
     */
    private Header header;

    protected static final String END_MAIL_PARSER="\\n\\.\\n";

    protected static final String END_MAIL="\n.\n";

    /**
     * String that contains the mail that will be send.
     */
    private StringBuffer output;

    public String getReceiver() {
        return header.getReceiver();
    }

    public StringBuffer getOutput() {
        return output;
    }

    /**
     * Constructor for the Mail
     *
     * @param receiver : String receiver of the mail
     * @param sender   : String sender of the mail
     * @param content  : String content of the mail
     * @param subject  : String subject of the mail
     */
    public Mail(String receiver, String sender, String content, String subject) throws MalFormedMailException {
        super(content, Parameter.END_LINE + Parameter.END_LINE);
        this.header = new Header(receiver, sender, subject);
        this.output = new StringBuffer();
        buildParameter();
    }

    /**
     * Constructor for the Mail
     * Initialize the Mail if the input string contains all the parameters required
     * else throw a MalFormedMailException
     *
     * @param input : String to initialize the header with
     * @throws MalFormedMailException
     */
    public Mail(String input) throws MalFormedMailException {
        super(input, Parameter.END_LINE + Parameter.END_LINE);
        this.output = new StringBuffer(input);
        if (!parseParameter(input)) {
            throw new MalFormedMailException("Mail Header MalFormed : expected parameters TO, FROM, SUBJECT, ORIG-DATE");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringBuffer buildParameter() {
        output = new StringBuffer(header.buildHeader());
        output.append(parseLine(content));
        output.append(Parameter.END_LINE);
        output.append(END_MAIL);
        return output;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean parseParameter(String output) {
        String[] tamp;
        if (output.contains(END_MAIL)&&output.contains(parser)) {
            tamp = output.split(END_MAIL_PARSER);
            tamp = tamp[0].split(parser);
            try {
                header = new Header(tamp[0] + Parameter.END_LINE);
            } catch (MalFormedMailException ex) {
                System.out.println("Mail.parseParameter : Failed to create header : " + ex.getMessage());
                return false;
            }
            tamp = tamp[1].split(Parameter.END_LINE);
            content = "";
            for (String line : tamp) {
                content += line;
            }
            return true;
        } else return false;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Mail) {
            Mail mail = (Mail) o;
            return mail.content.equals(this.content) && mail.parser.equals(this.parser) && mail.header.equals(this.header);
        }
        return false;
    }
}
