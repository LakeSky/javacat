package com.kzh.sys.service.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created by gang on 2019/6/17.
 */
@Service
@PropertySource({"classpath:mail.properties"})
public class EmailService {
    @Value("${mail.smtp.username}")
    private String from;

    @Resource
    private JavaMailSender javaMailSender;

    public void send(String[] emails, String subject, String text, boolean ifHtml) {
        MimeMessage mMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mMessageHelper;
        try {
            mMessageHelper = new MimeMessageHelper(mMessage, true, "UTF-8");
            mMessageHelper.setFrom(from);
            mMessageHelper.setTo(emails);
            mMessageHelper.setSubject(subject);
            mMessageHelper.setText(text, ifHtml);
            javaMailSender.send(mMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
