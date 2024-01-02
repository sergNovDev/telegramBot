package org.sergn.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sergn.CryptoTool;
import org.sergn.dao.AppUserDao;
import org.sergn.dto.MailParams;
import org.sergn.entity.AppUser;
import org.sergn.entity.enums.UserState;
import org.sergn.service.AppUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private final AppUserDao appUserDao;
    private final CryptoTool cryptoTool;

    @Value("${service.mail.uri}")
    private String mailServiceUri;

    @Override
    public String registerUser(AppUser appUser) {
        if (appUser.getIsActive()) {
            return "Вы уже зарегистрированы";
        } else if (appUser.getEmail() != null){
            return "Вам на почту уже было отправлено письмо для регистрации";
        }
        appUser.setState(UserState.WAIT_FOR_EMAIL_STATE);
        appUserDao.save(appUser);
        return "Введите пожалуйста ваш e-mail";
    }

    @Override
    public String setEmail(AppUser appUser, String email) {

//        try{
//            InternetAddress emailAddr = new InternetAddress(email);
//            emailAddr.validate();
//        } catch(AddressException e){
//           return "Введите, пожалуйста корректный email. Для отмены команды введите /cancel";
//        }
        var maybeUser = appUserDao.findByEmail(email);
        if (maybeUser.isEmpty()){
            appUser.setEmail(email);
            appUser.setState(UserState.BASIC_STATE);
            appUser = appUserDao.save(appUser);

            //отправка ссылки на почту
            var cryptoUserId = cryptoTool.hashOf(appUser.getId());
            var response = sendRequestToMailService(cryptoUserId, email);
            if (response.getStatusCode() != HttpStatus.OK){
                var msg = String.format("Отправка письма на почту %s не удалась",email);
                log.error(msg);
                appUser.setEmail(null);
                appUserDao.save(appUser);
                return msg;
            }
            return "На почту вам было отправлено письмо, перейдите по ссылке для регистрации";
        } else {
            return "Этот email уже используется. Введите корректный email";
        }
    }

    private ResponseEntity<String> sendRequestToMailService(String cryptoUserId, String email) {
        var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var mailParams = MailParams.builder()
                .id(cryptoUserId)
                .emailTo(email)
                .build();

        var request = new HttpEntity<MailParams>(mailParams, headers);
        return restTemplate.exchange(mailServiceUri,
                HttpMethod.POST,
                request,
                String.class);
    }
}
