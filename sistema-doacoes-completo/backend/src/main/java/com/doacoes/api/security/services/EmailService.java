package com.doacoes.api.security.services;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    
    private final String sendedFrom;
    
    private final EmailTemplateService emailTemplateProcessor;
    
    public EmailService(JavaMailSender mailSender, @Value("${spring.mail.username}") String sendedFrom) {
		super();
		this.mailSender = mailSender;
		this.sendedFrom = sendedFrom;
		this.emailTemplateProcessor = new EmailTemplateService();
	}

	public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sendedFrom);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendHtmlEmailWithAttachment(String to, String subject, String htmlContent, File attachment) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        // true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(sendedFrom);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // true = HTML

        if (attachment != null) {
            FileSystemResource file = new FileSystemResource(attachment);
            helper.addAttachment(attachment.getName(), file);
        }

        mailSender.send(message);
    }
    
    public void sendHtmlEmailFromTemplate(String to, String subject, String templateName, Map<String, Object> emailTemplateVariables) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();

        // true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(sendedFrom);
        helper.setTo(to);
        helper.setSubject(subject);
        
        String emailHtmlContent = this.emailTemplateProcessor.renderEmail(templateName, emailTemplateVariables);
        helper.setText(emailHtmlContent, true);

        mailSender.send(message);
    }
}
