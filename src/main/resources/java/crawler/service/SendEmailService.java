package crawler.service;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class SendEmailService {
	
	private String username; // gmail username
	private String password; // gmail password
	private String subject;
	private String body;
	private String[] receivers;

	public SendEmailService(String username, String password, String subject, String body, String[] receivers) {
		this.username = username;
		this.password = password;
		this.subject = subject;
		this.body = body;
		this.receivers = receivers;
	}

	public static void main(String[] args) {
		String [] receivers = new String [] { "lintcodept.1@gmail.com" };
		SendEmailService sender = new SendEmailService("velozproject", "standby123", "test subject2", "test body2", receivers);
		sender.send("gmail", 587);
	}
	
	/**
	 * send email via gmail with default settings
	 **/
	public void send(String host, int port) {
		Properties props = System.getProperties();
		host = "smtp." + host + ".com";
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.trust", host);
		props.put("mail.smtp.user", username);
		props.put("mail.smtp.password", password);
		props.put("mail.smtp.port", Integer.toString(port));
		props.put("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(props);
		MimeMessage message = new MimeMessage(session);

		try {
			message.setFrom(new InternetAddress(username));
			InternetAddress[] toAddress = new InternetAddress[receivers.length];
			for (int i = 0; i < receivers.length; i++) {
				toAddress[i] = new InternetAddress(receivers[i]);
			}

			for (int i = 0; i < toAddress.length; i++) {
				message.addRecipient(Message.RecipientType.TO, toAddress[i]);
			}
			message.setSubject(subject);
			message.setText(body);
			Transport transport = session.getTransport("smtp");
			transport.connect(host, username, password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			System.out.println("Email has been sent successfully @ " + new java.util.Date());
		} catch (AddressException e1) {
			e1.printStackTrace();
		} catch (MessagingException e2) {
			e2.printStackTrace();
		}
	}
	
	/**
	 * send email via gmail with default settings
	 **/
	public void sendByDefault() {
		Properties props = System.getProperties();
		String host = "smtp.gmail.com";
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.trust", host);
		props.put("mail.smtp.user", username);
		props.put("mail.smtp.password", password);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(props);
		MimeMessage message = new MimeMessage(session);

		try {
			message.setFrom(new InternetAddress(username));
			InternetAddress[] toAddress = new InternetAddress[receivers.length];
			for (int i = 0; i < receivers.length; i++) {
				toAddress[i] = new InternetAddress(receivers[i]);
			}

			for (int i = 0; i < toAddress.length; i++) {
				message.addRecipient(Message.RecipientType.TO, toAddress[i]);
			}
			message.setSubject(subject);
			message.setText(body);
			Transport transport = session.getTransport("smtp");
			transport.connect(host, username, password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			System.out.println("Email(s) have been sent successfully @ " + new java.util.Date());
		} catch (AddressException e1) {
			e1.printStackTrace();
		} catch (MessagingException e2) {
			e2.printStackTrace();
		}
	}
}