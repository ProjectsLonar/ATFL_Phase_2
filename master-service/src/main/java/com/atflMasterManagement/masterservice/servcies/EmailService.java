package com.atflMasterManagement.masterservice.servcies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Base64;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        text= "<html> <head> </head> <body>     <div style=\"margin-top: 0;background-color:#f4f4f4!important;color:#555; font-family: 'Open Sans', sans-serif;\"><div style=\"font-size: 14px;margin: 0px auto;max-width: 620px;margin-bottom: 20px;background:#c02e2e05;\">              <div style=\"padding: 0px 0px 9px 0px;width: 100%;color: #fff;font-weight:bold;font-size: 15px;line-height: 20px; width: 562px; margin: 0 auto;\">                 <center>                     <table border=\"0\" cellpadding=\"20\"cellspacing=\"0\" width=\"100%\">                         <tbody>                             <tr>                                 <td valign=\"top\" style=\"padding: 48px 48px 32px\">                                     <div style=\"color:#101010;font-family:HelveticaNeue,Helvetica,Roboto,Arial,sans-serif;font-size: 14px;line-height: 150%;text-align:left\">                                          <p style=\"margin: 0 0 16px\">Dear Sir/Ma’am,</p>                                         <p style=\"margin: 0 0 16px\">MSO-29686-2425-967 has come for your approval. Please visit the app to approve/ reject the record.</p>    <h4 style=\"font-weight:bold\">Created by:Panchanan Rout</h4>  <h4 style=\"font-weight:bold\">Order Amount:0.0</h4>   <h4 style=\"font-weight:bold;color:blue\">This is a system-generated email. Please do not reply to this.</h4>  <p>Regards,</p><p>Lakshya App team.</p>                               <div style= \"margin-bottom: 40px\">                                                                                       </div>                                                                                                                       </div>                          </html>       </td>                             </tr>                         </tbody>                     </table>                 </center>             </div>            </div>      </div> </body>" ;
//        text = text.replace("${salesOrder}", ltSoHeader.getOrderNumber());
//        text = text.replace("${salespersonName}", salespersonName);
//        text = text.replace("${totalAmount}", totalAmount);
        
        //String dynamicText = text.replace("$NAME$", to);
        //byte[] decodedBytes = Base64.getDecoder().decode(text);
        //String decodedString = new String(decodedBytes);
        
      message.setText(text);
     //   message.setText(decodedString);
        emailSender.send(message);
    }
    
    public void sendSimpleMessage1(String to, String subject, String text) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(text);
//        emailSender.send(message);
    
            // Sender's email address
            String senderEmail = "atfl4867@gmail.com";
            // Sender's password
            String senderPassword = "vjug xicp wiuw fakw"; //"Atfl@12345";

            // Receiver's email address
            String receiverEmail = "shubham.magare@lonartech.com";

            // SMTP server details
            String host = "smtp.gmail.com";
            String port =  "587";  // "465";   //"25";  // Port for TLS

            // Email subject and message
            String subject1 = "Test Email from Java";
            String messageContent = "This is a test email sent from Java 8.";

            // Set properties for SMTP server
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", port);

            // Create session with authentication
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, senderPassword);
                }
            });

            try {
                // Create MimeMessage object
                MimeMessage message = new MimeMessage(session);
                // Set sender address
                message.setFrom(new InternetAddress(senderEmail));
                // Set recipient address
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiverEmail));
                // Set email subject
                message.setSubject(subject);
                // Set email message
                message.setText(messageContent);

                // Send email
                Transport.send(message);
                System.out.println("Email sent successfully!");
            } catch (MessagingException e) {
                System.err.println("Failed to send email: " + e.getMessage());
                e.printStackTrace();
            }
    }
    

}
