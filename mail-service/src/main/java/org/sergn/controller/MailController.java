package org.sergn.controller;

import lombok.RequiredArgsConstructor;
import org.sergn.dto.MailParams;
import org.sergn.service.MailSenderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/mail")
@RestController
@RequiredArgsConstructor
public class MailController {
    private final MailSenderService mailSenderService;

    @PostMapping("/send")
    public ResponseEntity<?> sendActivationMail(@RequestBody MailParams mailParams){
        mailSenderService.send(mailParams);
        return ResponseEntity.ok().build();
    }

}
