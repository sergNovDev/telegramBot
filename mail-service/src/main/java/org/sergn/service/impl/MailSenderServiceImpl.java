package org.sergn.service.impl;

import lombok.RequiredArgsConstructor;
import org.sergn.dto.MailParams;
import org.sergn.service.MailSenderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderServiceImpl implements MailSenderService {

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String emailFrom;

    @Value("${service.activation.uri}")
    private String activationSenderUri;
    @Override
    public void send(MailParams mailParams) {
        var subject = "Активация учетной записи";
        var messageBody = getActivationMailBody(mailParams.getId());
        var emailTo = mailParams.getEmailTo();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(emailFrom);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(messageBody);

        //javaMailSender.send(mailMessage);
        //TODO сдесь есть проблемы с PKIX
    }

    private String getActivationMailBody(String id) {
        String msg = String.format("Для завершения реистрации перейдите по ссылке\n%s",
                activationSenderUri);
        return msg.replace("{id}",id);
    }
}
