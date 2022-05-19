package kr.co.demo.util;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class Mail {

    public Mail() {
        msg = null;
        address = null;
        fadd = null;
    }

    public void init(String fname, String fid, String fpw, String to, String tname, String msgSubj, String msgText) {
        try {
            final String SEND_MAIL_ID = fid;
            final String SEND_MAIL_PW = fpw;

            props = new Properties();

            props.put("mail.smtp.host", "smart.whoismail.net");
            props.put("mail.smtp.port", "587");
            props.put("mail.debug", "false");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable","false");
            props.put("mail.smtp.EnableSSL.enable","false");
            /*props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.smtp.port", "587");
            props.setProperty("mail.smtp.socketFactory.port", "587");*/
            sess = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(SEND_MAIL_ID, SEND_MAIL_PW);
                        }});
            msg = new MimeMessage(sess);
            address = new InternetAddress(to, tname, "UTF-8");
            fadd = new InternetAddress(fid, fname, "UTF-8");
            msg.setFrom(fadd);
            msg.setRecipient(javax.mail.Message.RecipientType.TO, address);
            msg.setSubject(msgSubj, "UTF-8");
            msg.setSentDate(new Date());
            msg.setContent(msgText, "text/html;charset=utf-8");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendMail() {
        try {
            Transport.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void setSubject(String subject) {
        try {
            msg.setSubject(subject, "UTF-8");
        } catch (Exception exception) {
        }
    }

    public void setMessage(String msgText) {
        try {
            msg.setContent(msgText, "text/html;charset=utf-8");
        } catch (Exception exception) {
        }
    }

    private MimeMessage msg;
    private Properties props;
    private Session sess;
    private InternetAddress address;
    private InternetAddress fadd;

}