package com.talentco.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp, String subject) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(
                "Dear TalentCo User,\n\n" +
                "Your One-Time Password (OTP) is: " + otp + "\n\n" +
                "This OTP is valid for 10 minutes. Do not share it with anyone.\n\n" +
                "Best regards,\nTalentCo Team"
            );
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    public void sendRegistrationOtp(String email, String otp) {
        sendOtpEmail(email, otp, "TalentCo - Email Verification OTP");
    }

    public void sendPasswordResetOtp(String email, String otp) {
        sendOtpEmail(email, otp, "TalentCo - Password Reset OTP");
    }

    public void sendWelcomeEmail(String email, String name) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Welcome to TalentCo!");
            message.setText(
                "Dear " + name + ",\n\n" +
                "Welcome to TalentCo – Your platform to connect with the best professionals!\n\n" +
                "Your account has been successfully verified.\n\n" +
                "Get started by exploring professionals or completing your profile.\n\n" +
                "Best regards,\nTalentCo Team"
            );
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send welcome email: " + e.getMessage());
        }
    }
}
