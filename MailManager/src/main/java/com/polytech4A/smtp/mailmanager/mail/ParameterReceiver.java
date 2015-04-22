package com.polytech4a.smtp.mailmanager.mail;

import com.polytech4a.smtp.mailmanager.exceptions.MalFormedMailException;

/**
 * Created by Dimitri on 09/03/2015.
 */
public class ParameterReceiver extends Parameter {
    /**
     * Constructor of the receiver parameter
     *
     * @param content : content of the parameter
     */
    protected ParameterReceiver(String content) throws MalFormedMailException {
        super(content, "TO:");
    }
}
