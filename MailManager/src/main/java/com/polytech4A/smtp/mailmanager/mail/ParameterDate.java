package com.polytech4a.smtp.mailmanager.mail;

import com.polytech4a.smtp.mailmanager.exceptions.MalFormedMailException;

/**
 * Created by Dimitri on 09/03/2015.
 */
public class ParameterDate extends Parameter {
    /**
     * Constructor of the date parameter
     *
     * @param content : content of the parameter
     */
    protected ParameterDate(String content) throws MalFormedMailException {
        super(content, "ORIG-DATE:");
    }
}
