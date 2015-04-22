package com.polytech4a.smtp.mailmanager.mail;

import com.polytech4a.smtp.mailmanager.exceptions.MalFormedMailException;

/**
 * Created by Dimitri on 09/03/2015.
 */
public class ParameterSubject extends Parameter {
    /**
     * Constructor of Subject Parameter
     *
     * @param content : content of the parameter
     */
    protected ParameterSubject(String content) throws MalFormedMailException {
        super(content, "SUBJECT:");
    }
}
