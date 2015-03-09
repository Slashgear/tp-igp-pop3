package com.polytech4A.pop3.mailmanager;

/**
 * Created by Dimitri on 09/03/2015.
 */
public class MailParameterReceiver extends MailParameter {
    public MailParameterReceiver(String content){
        super(content, "TO:");
    }
}
