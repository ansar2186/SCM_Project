package com.smart.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import com.smart.util.UtilClass;

@Service
public class EmailService {


	public boolean sendEmail(String subject,String message,String to) {
		boolean IsEmailSend=false;
		//String from="ahmad.ansar74@gmail.com";
		//String host="smtp.gmail.com";
		//get System properties
		Properties properties= System.getProperties();
		System.out.println("Properties--------" +properties);

		//host set
		properties.put("mail.smtp.host", UtilClass.host);
		properties.put("mail.smtp.port", UtilClass.smtPort);
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");

		//get Seesion object
		Session session=Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				
				return new PasswordAuthentication(UtilClass.userEmail, UtilClass.userPassword);
			}

			
		});
		
		//session.getDebug(true);

		//step2: compose the mesaage
		MimeMessage mimeMessage = new MimeMessage(session);



		try {

			mimeMessage.setFrom(UtilClass.from);
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			mimeMessage.setSubject(subject);
			mimeMessage.setText(message);

			//Step3: send the  msg using transport Class
			Transport.send(mimeMessage);
			IsEmailSend= true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return IsEmailSend;
	}

}
