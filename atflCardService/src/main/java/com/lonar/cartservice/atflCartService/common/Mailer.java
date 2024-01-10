package com.lonar.cartservice.atflCartService.common;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.VelocityException;
import org.springframework.ui.velocity.VelocityEngineFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.lonar.cartservice.atflCartService.AtflCartServiceApplication;
import com.lonar.cartservice.atflCartService.model.Mail;


@Component
@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
@PropertySource(value = "classpath:queries/config.properties", ignoreResourceNotFound = true)
public class Mailer {


	@Autowired
	private Environment env;

	@Bean
	public VelocityEngine getVelocityEngine() throws VelocityException, IOException {
		//System.out.println("getVelocityEngine");
		VelocityEngineFactory velocityEngineFactory = new VelocityEngineFactory();
		Properties props = new Properties();
		props.put("resource.loader", "class");
		props.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		velocityEngineFactory.setVelocityProperties(props);
		return velocityEngineFactory.createVelocityEngine();
		// Properties props = new Properties();
		/*
		 * String templatePath = "resources\\templates\\"; /*Map<String, String>
		 * configPropertyMap = A2zMailServiceApplication.configMap.get(supplierId); if
		 * (configPropertyMap != null) { templatePath =
		 * configPropertyMap.get("TEMPLATE_PATH"); }else { templatePath = "C:/a2z/"; }
		 */
		// templatePath=""
		// String path = "C:/a2z/";
		/*
		 * props.put("file.resource.loader.path", templatePath);
		 * props.setProperty("runtime.log.logsystem.class",
		 * "org.apache.velocity.runtime.log.NullLogSystem");
		 * 
		 * velocityEngineFactory.setVelocityProperties(props); return
		 * velocityEngineFactory.createVelocityEngine();
		 */
	}

	public int sendMail(Mail mail, VelocityContext velocityContext,Long orgId,String fileName)
			throws ResourceNotFoundException, ParseErrorException, Exception {
		System.out.println("send Mail from mailer" + mail.toString()+mail.getMailFrom().getAddress());
		VelocityEngine velocityEngine = getVelocityEngine();
		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.host", AtflCartServiceApplication.configMap.get(orgId).get("EMAIL_HOST")); // "mail.lonartech.com";
		javaMailProperties.put("mail.smtp.port", AtflCartServiceApplication.configMap.get(orgId).get("EMAIL_PORT"));
		javaMailProperties.put("mail.smtp.auth", env.getProperty("emailAuth")); // true or false
		javaMailProperties.put("mail.smtp.starttls.enable", env.getProperty("emailIsttlEnabled"));
		Session session = Session.getInstance(javaMailProperties, // Get the Session object.
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						try {
			//				System.out.println("in auth");
						return new PasswordAuthentication(mail.getMailFrom().getAddress(), AtflCartServiceApplication.configMap.get(orgId).get("EMAIL_PASSWORD"));
						}
						catch (Exception e) {
							e.printStackTrace();
							return null;
						}}
				});
	MimeMessage message = new MimeMessage(session); // Create a default MimeMessage object.
		message.setFrom(mail.getMailFrom());
		if (mail.getMailTo() != null && !mail.getMailTo().isEmpty())
		{																					// field of the
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail.getMailTo())); // Set To: header
		}																							// header.
		if (mail.getMailCc() != null && !mail.getMailCc().isEmpty()) {
			// message.setCc(InternetAddress.parse(mail.getMailCc()));
			message.addRecipient(RecipientType.CC, new InternetAddress(mail.getMailCc()));
		}
			message.setSubject(mail.getMailSubject());
		Template template = velocityEngine.getTemplate("./templates/" + mail.getTemplateName());
		StringWriter stringWriter = new StringWriter();
	
template.merge(velocityContext, stringWriter);
//System.out.println(template.toString());
message.addHeader("Content-type", "text/HTML; charset=UTF-8");
message.addHeader("format", "flowed");
message.addHeader("Content-Transfer-Encoding", "8bit");
/*
message.setContent(stringWriter.toString(),"text/html");
*///need to change
Multipart multipart = new MimeMultipart(); 
String filePath=mail.getAttachment();
if(filePath!=null &&!filePath.isEmpty())
{
BodyPart attachmentBodyPart = new MimeBodyPart(); //2
DataSource source = new FileDataSource(filePath);
attachmentBodyPart.setDataHandler(new DataHandler(source)); //2
attachmentBodyPart.setFileName(fileName); // 2
multipart.addBodyPart(attachmentBodyPart);}
BodyPart htmlBodyPart = new MimeBodyPart(); //4
htmlBodyPart.setContent(stringWriter.toString() , "text/html"); //5
multipart.addBodyPart(htmlBodyPart);
message.setContent(multipart);
// put everything together

			try {
			Transport.send(message);// Send message
			System.out.println("after send");
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}


}
