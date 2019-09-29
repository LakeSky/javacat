package com.kzh.sys.util;

import org.apache.commons.lang.CharSet;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created by gang on 2019/9/24.
 */
public class EmailUtil {
    public static void main(String[] args) {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.qq.com");
        javaMailSender.setUsername("623907705@qq.com");
        javaMailSender.setPassword("xxxxxxxxxxxxxxxx");
        javaMailSender.setDefaultEncoding("UTF-8");

        send(javaMailSender, "623907705@qq.com", new String[]{"623907705@qq.com"}, "测试邮件", "测试邮件", false);
    }


    public static void send(JavaMailSender javaMailSender, String from, String[] emails, String subject, String text, boolean ifHtml) {
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
