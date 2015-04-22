package com.polytech4a.smtp.mailmanager.mail;

import com.polytech4a.smtp.mailmanager.exceptions.MalFormedMailException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Dimitri on 09/03/2015.
 */
public abstract class Parameter {

    /**
     * linefeed for POP3 mails
     */
    protected static final String END_LINE = "\t\n";

    /**
     * Constant for the max number of characters per line
     */
    protected static final int MAX_LINE_LENGTH = 78;

    /**
     * Content of the parameter
     */
    protected String content;

    /**
     * Perser of the parameter
     */
    protected String parser;

    /**
     * Constructor for the parameter
     *
     * @param content : content of the parameter
     * @param parser  : parser of the parameter
     */
    protected Parameter(String content, String parser) throws MalFormedMailException {
        if (content == null)
            throw new MalFormedMailException("MailParameter " + this.getClass().toString() + " must not be empty");
        this.content = content;
        this.parser = parser;
    }

    /**
     * Check if a line matches the parameter's parser
     * If so, initiate the content of the parameter
     *
     * @param output : String to test
     * @return boolean : true if the string match the parser's parameter
     */
    protected boolean parseParameter(String output) {
        String tamp;
        if (output != null) {
            // Text to catch matches : parser text_to_catch \t\n
            Matcher matcher = Pattern.compile(parser + "[\\S ]+\\t\\n").matcher(output);
            if (matcher.find()) {
                int i, j = 0;

                content = "";
                tamp = output.split(parser)[1];
                i = tamp.indexOf(END_LINE) + 2;

                while (i + 1 < tamp.length() && " ".equals(tamp.substring(i, i + 1))) {
                    content += tamp.substring(j, i - 2);
                    j = i;
                    i = tamp.indexOf(END_LINE, i) + 2;
                }
                content += tamp.substring(j, i - 2);
                return true;
            }
        }
        return false;
    }

    /**
     * Build a StringBuffer with the parameter parser and its content
     * to send it in a mail
     *
     * @return StringBuffer : parameter built
     */
    protected StringBuffer buildParameter() {
        StringBuffer output = new StringBuffer(parser);
        output.append(parseLine(content));
        return output;
    }

    /**
     * Check if the line has the right length
     *
     * @param line : String to test
     * @return boolean : true if the line match the max number of characters
     */
    protected boolean checkLineLength(String line) {
        return line.length() <= MAX_LINE_LENGTH;
    }

    /**
     * Parse a line to the regular expression for mail exchange
     *
     * @param line : String to parse
     * @return StringBuffer : line parsed
     */
    protected StringBuffer parseLine(String line) {
        int i = 0;
        StringBuffer res = new StringBuffer();
        while (!checkLineLength(line.substring(i))) {
            int lastSpaceIndex = line.substring(i, i + MAX_LINE_LENGTH - 1).lastIndexOf(' ');
            if (lastSpaceIndex < 1) {
                res.append(line.substring(i, i + MAX_LINE_LENGTH - 1));
                i += MAX_LINE_LENGTH - 1;
            } else {
                res.append(line.substring(i, i + lastSpaceIndex));
                i += lastSpaceIndex;
            }
            res.append(END_LINE);

        }
        res.append(line.substring(i));

        return res.append(END_LINE);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Parameter) {
            Parameter parameter = (Parameter) o;
            return parameter.content.equals(this.content) && parameter.parser.equals(this.parser);
        }
        return false;
    }
}
