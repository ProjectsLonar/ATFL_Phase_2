package com.lonar.cartservice.atflCartService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

//@Service
@Component
public class EmailService {

    @Autowired
    JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        
        message.setText(text);
        emailSender.send(message);
    }
    
//    public void sendSimpleMessage1(String to, String subject, String text) {
////        SimpleMailMessage message = new SimpleMailMessage();
////        message.setTo(to);
////        message.setSubject(subject);
////        message.setText(text);
////        emailSender.send(message);
//    
//            // Sender's email address
//            String senderEmail = "atfl4867@gmail.com";
//            // Sender's password
//            String senderPassword = "vjug xicp wiuw fakw"; //"Atfl@12345";
//
//            // Receiver's email address
//            String receiverEmail = "shubham.magare@lonartech.com";
//
//            // SMTP server details
//            String host = "smtp.gmail.com";
//            String port =  "587";  // "465";   //"25";  // Port for TLS
//
//            // Email subject and message
//            String subject1 = "Test Email from Java";
//            String messageContent = "This is a test email sent from Java 8.";
//
//            // Set properties for SMTP server
//            Properties properties = new Properties();
//            properties.put("mail.smtp.auth", "true");
//            properties.put("mail.smtp.starttls.enable", "true");
//            properties.put("mail.smtp.host", host);
//            properties.put("mail.smtp.port", port);
//
//            // Create session with authentication
//            Session session = Session.getInstance(properties, new Authenticator() {
//                @Override
//                protected PasswordAuthentication getPasswordAuthentication() {
//                    return new PasswordAuthentication(senderEmail, senderPassword);
//                }
//            });
//
//            try {
//                // Create MimeMessage object
//                MimeMessage message = new MimeMessage(session);
//                // Set sender address
//                message.setFrom(new InternetAddress(senderEmail));
//                // Set recipient address
//                message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiverEmail));
//                // Set email subject
//                message.setSubject(subject);
//                // Set email message
//                message.setText(messageContent);
//
//                // Send email
//                Transport.send(message);
//                System.out.println("Email sent successfully!");
//            } catch (MessagingException e) {
//                System.err.println("Failed to send email: " + e.getMessage());
//                e.printStackTrace();
//            }
//    }
//    
//
}
