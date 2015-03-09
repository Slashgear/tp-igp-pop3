package com.polytech4A.pop3.mailmanager;

/**
 * Created by Dimitri on 09/03/2015.
 */
public class MailParameterSubject extends MailParameter {
    public MailParameterSubject(String content){
        super(content, "SUBJECT:");
    }
}
