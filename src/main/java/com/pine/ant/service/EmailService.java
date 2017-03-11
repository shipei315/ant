package com.pine.ant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${email.fromEmail}")
    String fromEmail;
    
    @Value("${email.toEmail}")
    String toEmail;
    
    public void sendSimpleMail() throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("主题：简单邮件");
        message.setText("spring boot 测试邮件内容");

        mailSender.send(message);
    }

}
