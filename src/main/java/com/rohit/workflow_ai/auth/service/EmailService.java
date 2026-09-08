package com.rohit.workflow_ai.auth.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(
            String email,
            String resetLink
    ) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Reset your Workflow AI password");

        message.setText(
                "Hello,\n\n" +
                        "We received a request to reset your Workflow AI password.\n\n" +
                        "Click the link below to reset your password:\n\n" +
                        resetLink + "\n\n" +
                        "This link will expire in 15 minutes.\n\n" +
                        "If you did not request this password reset, " +
                        "you can safely ignore this email.\n\n" +
                        "Regards,\n" +
                        "Workflow AI Team"
        );

        mailSender.send(message);
    }
}