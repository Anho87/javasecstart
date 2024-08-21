package se.systementor.javasecstart.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    public void sendVerificationEmail(String recipientEmail, String token) throws MessagingException {
        String subject = "Email Verification";
        String verificationLink = "http://localhost:8080/verify?token=" + token;
        String content = "<p>Dear user,</p>"
                + "<p>Please click the link below to verify your registration:</p>"
                + "<a href=\"" + verificationLink + "\">Verify your email</a>";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo(recipientEmail);
        helper.setSubject(subject);
        helper.setText(content, true);
        javaMailSender.send(message);
    }
}
