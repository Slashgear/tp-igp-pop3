package com.polytech4a.smtp.mailmanager.mail;

import com.polytech4a.smtp.mailmanager.exceptions.MalFormedMailException;

/**
 * Created by Dimitri on 09/03/2015.
 */
public class ParameterSender extends Parameter {

    /**
     * Constructor of the sender parameter
     *
     * @param content : content of the parameter
     */
    protected ParameterSender(String content) throws MalFormedMailException {
        super(content, "FROM:");
    }
}
