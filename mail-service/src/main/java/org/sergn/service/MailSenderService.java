package org.sergn.service;

import org.sergn.dto.MailParams;

public interface MailSenderService {
    void send(MailParams mailParams);

}
