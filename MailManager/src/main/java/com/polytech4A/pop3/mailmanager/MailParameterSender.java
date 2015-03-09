package com.polytech4A.pop3.mailmanager;

/**
 * Created by Dimitri on 09/03/2015.
 */
public class MailParameterSender extends MailParameter {

    public MailParameterSender(String content){
        super(content, "FROM:");
    }
}
