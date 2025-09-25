package com.sample.mail;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {

    private final String host = "smtp.gmail.com";
    private final String port = "587";
    private final String username = "example@gmail.com";
    private final String password = "password";

    public void sendMail(Email email) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(email.from()));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.to()));
        message.setSubject(email.subject());
        message.setText(email.body());

        Transport.send(message);
    }
}
