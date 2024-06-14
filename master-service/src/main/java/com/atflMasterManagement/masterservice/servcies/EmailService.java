package com.atflMasterManagement.masterservice.servcies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;
//This is original method....
//    public void sendSimpleMessage(String to, String subject, String text) throws MessagingException {
//        
//    	SimpleMailMessage message = new SimpleMailMessage();   //original code comment on 29-May-2024
//        
// //   	MimeMessage message = emailSender.createMimeMessage(); 
// //       MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//        
//          message.setTo(to);          //this is original code comment on 29-May-2024
//          message.setSubject(subject);
//        
//        //text= "<html> <head> </head> <body>     <div style=\"margin-top: 0;background-color:#f4f4f4!important;color:#555; font-family: 'Open Sans', sans-serif;\"><div style=\"font-size: 14px;margin: 0px auto;max-width: 620px;margin-bottom: 20px;background:#c02e2e05;\">              <div style=\"padding: 0px 0px 9px 0px;width: 100%;color: #fff;font-weight:bold;font-size: 15px;line-height: 20px; width: 562px; margin: 0 auto;\">                 <center>                     <table border=\"0\" cellpadding=\"20\"cellspacing=\"0\" width=\"100%\">                         <tbody>                             <tr>                                 <td valign=\"top\" style=\"padding: 48px 48px 32px\">                                     <div style=\"color:#101010;font-family:HelveticaNeue,Helvetica,Roboto,Arial,sans-serif;font-size: 14px;line-height: 150%;text-align:left\">                                          <p style=\"margin: 0 0 16px\">Dear Sir/Ma’am,</p>                                         <p style=\"margin: 0 0 16px\">MSO-29686-2425-967 has come for your approval. Please visit the app to approve/ reject the record.</p>    <h4 style=\"font-weight:bold\">Created by:Panchanan Rout</h4>  <h4 style=\"font-weight:bold\">Order Amount:0.0</h4>   <h4 style=\"font-weight:bold;color:blue\">This is a system-generated email. Please do not reply to this.</h4>  <p>Regards,</p><p>Lakshya App team.</p>                               <div style= \"margin-bottom: 40px\">                                                                                       </div>                                                                                                                       </div>                          </html>       </td>                             </tr>                         </tbody>                     </table>                 </center>             </div>            </div>      </div> </body>" ;
//
////        String salesOrder ="myOrder123";
////        String salespersonName = "Vaibhav";
////        String totalAmount= "100";
////        String htmlBody = "<html><head></head><body>"
////                + "<div style=\"margin-top:0;background-color:#f4f4f4!important;color:#555;font-family:'Open Sans',sans-serif;\">"
////                + "<div style=\"font-size:14px;margin:0px auto;max-width:620px;margin-bottom:20px;background:#c02e2e05;\">"
////                + "<div style=\"padding:0px 0px 9px 0px;width:100%;color:#fff;font-weight:bold;font-size:15px;line-height:20px;width:562px;margin:0 auto;\">"
////                + "<center><table border=\"0\" cellpadding=\"20\" cellspacing=\"0\" width=\"100%\"><tbody><tr><td valign=\"top\" style=\"padding:48px 48px 32px\">"
////                + "<div style=\"color:#101010;font-family:HelveticaNeue,Helvetica,Roboto,Arial,sans-serif;font-size:14px;line-height:150%;text-align:left\">"
////                + "<p style=\"margin:0 0 16px\">Dear Sir/Ma’am,</p>"
////                + "<p style=\"margin:0 0 16px\">" + salesOrder + " has come for your approval. Please visit the app to approve/ reject the record.</p>"
////                + "<h4 style=\"font-weight:bold\">Created by:" + salespersonName + "</h4>"
////                + "<h4 style=\"font-weight:bold\">Order Amount:" + totalAmount + "</h4>"
////                + "<h4 style=\"font-weight:bold;color:blue\">This is a system-generated email. Please do not reply to this.</h4>"
////                + "<p>Regards,</p><p>Lakshya App team.</p>"
////                + "<div style=\"margin-bottom:40px\"></div></div></td></tr></tbody></table></center></div></div></div></body></html>";
//
//        // Send email
//        
//        
//        //        text = text.replace("${salesOrder}", ltSoHeader.getOrderNumber());
////        text = text.replace("${salespersonName}", salespersonName);
////        text = text.replace("${totalAmount}", totalAmount);
//        
//        //String dynamicText = text.replace("$NAME$", to);
//        //byte[] decodedBytes = Base64.getDecoder().decode(text);
//        //String decodedString = new String(decodedBytes);
//        
//      message.setText(text);         //original comment on 29-May-2024
//     //   message.setText(decodedString);
//      
////      helper.setTo(to);
////      helper.setSubject(subject);
////      helper.setText(text, true); 
//      
//        emailSender.send(message);
//    }
  //This is end of original method....
    
   //This is Rohan created method 
//    public Map<String,String> sendSimpleMessage(String to, String subject, String text) throws MessagingException {
//    	Map<String,String> map = new HashMap<String,String>();
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(text);
//
//        try {
//            emailSender.send(message);
//            System.out.println("Email sent successfully to: " + to);
//            map.put("status", "true");
//            map.put("message", "Email sent successfully to: "+ to);
//        } catch (MailException ex) {
//            System.out.println("Failed to send email to: " + to);
//            map.put("status", "false");
//            map.put("message", "Failed to send email to: "+to);
//            ex.printStackTrace(); // Log the stack trace for debugging
//        }
//        return map;
//    }
    //This is end of Rohan created method 
    
    //This is Rohan second method
    public Map<String,String> sendSimpleMessage(String to, String subject, String text) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        Map<String,String> map = new HashMap<String,String>();
        try {
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true); // true indicates HTML content
            emailSender.send(message);
            System.out.println("Email sent successfully to: " + to);
            map.put("status", "true");
            map.put("message", "Email sent successfully to: "+ to);
        } catch (MailException ex) {
            System.out.println("Failed to send email to: " + to);
            map.put("status", "false");
            map.put("message", "Failed to send email to: "+to);
            ex.printStackTrace(); // Log the stack trace for debugging
        }
        return map;
    }
    //This is end of Rohan second method
    
    
    public Map<String,String> sendSimpleMessageWithAuth(String to, String subject, String text) throws MessagingException {
//        MimeMessage message = emailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        Map<String,String> map = new HashMap<String,String>();
        System.out.println("in sendSimpleMessageWithAuth");
        String host = "smtp.gmail.com";         
        String port = "587";         
        String mailFrom = "atfl4867@gmail.com";         
        String password = "vjug xicp wiuw fakw";         
        // Outgoing email information
        String mailTo = to;        
        String subject1 = subject;   
//        text = "<html><head></head><body><h1>Greetings!</h1><p>We hope this message finds you well.</p></body></html>";
        String salesOrder ="myOrder123";
        String salespersonName = "Vaibhav";
        String totalAmount= "100";
         text = "<html><head></head><body>"
                + "<div style=\"margin-top:0;background-color:#f4f4f4!important;color:#555;font-family:'Open Sans',sans-serif;\">"
                + "<div style=\"font-size:14px;margin:0px auto;max-width:620px;margin-bottom:20px;background:#c02e2e05;\">"
                + "<div style=\"padding:0px 0px 9px 0px;width:100%;color:#fff;font-weight:bold;font-size:15px;line-height:20px;width:562px;margin:0 auto;\">"
                + "<center><table border=\"0\" cellpadding=\"20\" cellspacing=\"0\" width=\"100%\"><tbody><tr><td valign=\"top\" style=\"padding:48px 48px 32px\">"
                + "<div style=\"color:#101010;font-family:HelveticaNeue,Helvetica,Roboto,Arial,sans-serif;font-size:14px;line-height:150%;text-align:left\">"
                + "<p style=\"margin:0 0 16px\">Dear Sir/Ma’am,</p>"
                + "<p style=\"margin:0 0 16px\">" + salesOrder + " has come for your approval. Please visit the app to approve/ reject the record.</p>"
                + "<h4 style=\"font-weight:bold\">Created by:" + salespersonName + "</h4>"
                + "<h4 style=\"font-weight:bold\">Order Amount:" + totalAmount + "</h4>"
                + "<h4 style=\"font-weight:bold;color:blue\">This is a system-generated email. Please do not reply to this.</h4>"
                + "<p>Regards,</p><p>Lakshya App team.</p>"
                + "<div style=\"margin-bottom:40px\"></div></div></td></tr></tbody></table></center></div></div></div></body></html>";
        String message = text;
        String result = String.join("   ", host,port,mailFrom,password,mailTo,subject1,message);
    	System.out.println("result in method = "+result);
        try {
//            helper.setTo(to);
//            helper.setSubject(subject);
//            helper.setText(text, true); // true indicates HTML content
//            emailSender.send(message);
        	System.out.println("result in try = "+result);
        	map = sendEmail(host, port, mailFrom, password, mailTo, subject1, message);
//            map.put("status", "true");
//            map.put("message", "Email sent successfully to: "+ to);
        } catch (MailException ex) {
        	System.out.println("result in catch = "+result);
            System.out.println("Failed to send email to: " + to);
            map.put("status", "false");
            map.put("message", "Failed to send email to: "+to);
            ex.printStackTrace(); // Log the stack trace for debugging
        }
        return map;
    }
    
    
    public static Map<String,String> sendEmail(String host, String port, final String userName, final String password, String toAddress,String subject, String messageBody) throws AddressException, MessagingException {         
    	// Set up mail server properties
    	System.out.println("in sendEmail");
    	Map<String,String> map = new HashMap<String,String>();
    	Properties properties = new Properties();         
    	properties.put("mail.smtp.host", host);         
    	properties.put("mail.smtp.port", port);         
    	properties.put("mail.smtp.auth", "true");         
    	properties.put("mail.smtp.starttls.enable", "true"); 
    	properties.put("mail.debug", "true");
    	System.out.println("below properties");
    	// Create a mail session
    	Authenticator auth = new Authenticator() { 
    		public PasswordAuthentication getPasswordAuthentication() { 
    			return new PasswordAuthentication(userName, password);           
    			}       
    		};
        	System.out.println("below Authenticator");
    		Session session = Session.getInstance(properties, auth); 
        	System.out.println("below session");
    		// Create the email message
    		Message message = new MimeMessage(session); 
    		message.setFrom(new InternetAddress(userName)); 
    		message.setRecipient(Message.RecipientType.TO, new InternetAddress(toAddress)); 
    		message.setSubject(subject); 
//    		message.setText(messageBody);
            message.setContent(messageBody, "text/html; charset=utf-8");
    		// Send the email 
        	System.out.println("Above Transport");
        	try {
        		Transport.send(message);
        		map.put("status", "101");
        		map.put("message", "Email sent successfully to: "+ toAddress);
        	}catch(Exception ex) {
        		System.out.println("Failed...");
        		map.put("status", "102");
                map.put("message", "Failed to send email to: "+ toAddress);
        		ex.printStackTrace();
        	}
        	System.out.println("Below Transport");
        	return map;

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
