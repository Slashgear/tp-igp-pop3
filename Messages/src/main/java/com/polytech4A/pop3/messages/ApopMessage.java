package com.polytech4A.pop3.messages;

import com.polytech4A.pop3.messages.Exceptions.MalFormedMessageException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

/**
 * Created by Antoine on 01/03/15.
 *
 * @author Antoine
 * @version 1.1
 *          <p/>
 *          Message of ApopMessage for POP3 exchange.
 */
public class ApopMessage extends Message {

    /**
     * Name of User
     */
    private String id;

    /**
     * Password of user.
     */
    private String password;

    /**
     * Arpa given by the server during ServerReadyMessage.
     */
    private String arpa;

    /**
     * CryptedPwd.
     */
    private String cryptedPwd;

    /**
     * Getter of Id.
     *
     * @return Name String.
     */
    public String getId() {
        return id;
    }

    /**
     * Getter of Password.
     *
     * @return Password String.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Getter of Crypted Password.
     *
     * @return string
     */
    public String getCryptedPwd() {
        return cryptedPwd;
    }

    /**
     * Getter of the Arpa.
     *
     * @return string
     */
    public String getArpa() {
        return arpa;
    }


    /**
     * Constructor who create a  APOP message and who parse informations in the text.
     *
     * @param text ApopMessage in String
     * @throws com.polytech4A.pop3.messages.Exceptions.MalFormedMessageException
     */
    public ApopMessage(String text) throws MalFormedMessageException {
        if (ApopMessage.matches(text)) {
            String[] array = text.split(" ");
            this.id = array[1];
            this.cryptedPwd = array[2];
        } else {
            throw new MalFormedMessageException("ApopMessage MalFormed : expected^APOP \\S* \\S*$)");
        }

    }

    /**
     * Constructor who create a ApopMessage from parameters.
     *
     * @param id       Name
     * @param password Password in String
     * @param arpa     Arpa defined in the ServerReadyMesssage of the Server.
     */
    public ApopMessage(String id, String password, String arpa) {
        super();
        this.id = id;
        this.password = password;
        this.arpa = arpa;
        contructMessage();
    }

    /**
     * Procédure who create the message.
     */
    private void contructMessage() {
        try {
            this.message.append("APOP ");
            this.message.append(this.id);
            this.message.append(" ");
            byte[] crypted;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            out.write(this.password.getBytes("UTF8"));
            out.write(this.arpa.getBytes("UTF8"));
            crypted = MessageDigest.getInstance("MD5").digest(out.toByteArray());
            StringBuffer sb = new StringBuffer();
            for (byte b : crypted) {
                sb.append(String.format("%02x", b & 0xff));
            }
            this.cryptedPwd = sb.toString();
            this.message.append(cryptedPwd);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Verify if a Apop Message crypted pwd is correct.
     *
     * @param pwd  Password of the user defines in this ApopMessage.
     * @param arpa Arpa of the server, the same given in the ServerReadyMessage.
     * @return true/false true if cryptedPwd of this Apop is equal to MD5(pwd+arpa).
     */
    public boolean verify(String pwd, String arpa) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            out.write(pwd.getBytes("UTF8"));
            out.write(arpa.getBytes("UTF8"));
            byte[] crypted = MessageDigest.getInstance("MD5").digest(out.toByteArray());
            StringBuffer sb = new StringBuffer();
            for (byte b : crypted) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return (this.cryptedPwd.equals(sb.toString()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Static function for testing if a message have the structure of the ApopMessage.
     *
     * @param text Text to test.
     * @return true/false
     */
    public static boolean matches(String text) {
        String regex = "^APOP \\S* \\S*$";
        return Pattern.matches(regex, text);
    }
}
