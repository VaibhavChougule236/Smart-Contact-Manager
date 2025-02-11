package com.smart.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	@Autowired
	private JavaMailSender javaMailSender;

	@Value("$(spring.mail.username)")
	private String fromEmailId;

	public boolean sendEmail(String to, String subject, String body) {
		boolean f = false;
		try {
	        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
	        simpleMailMessage.setFrom(fromEmailId);
	        simpleMailMessage.setTo(to);
	        simpleMailMessage.setSubject(subject);
	        simpleMailMessage.setText(body);
	        javaMailSender.send(simpleMailMessage);
	       f= true;
	    } catch (MailException e) {
	    	e.printStackTrace();
	         f= false;
	    } catch (Exception e) {
	    	e.printStackTrace();
	        f= false;
	    }

		return f;
	}
}
