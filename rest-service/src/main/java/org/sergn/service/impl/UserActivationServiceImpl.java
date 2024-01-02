package org.sergn.service.impl;

import lombok.RequiredArgsConstructor;
import org.sergn.CryptoTool;
import org.sergn.dao.AppUserDao;
import org.sergn.entity.AppUser;
import org.sergn.service.UserActivationService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserActivationServiceImpl implements UserActivationService {
    private final AppUserDao appUserDao;
    private final CryptoTool cryptoTool;

    @Override
    public boolean activation(String cryptoUserId) {
        var userId = cryptoTool.idOf(cryptoUserId);
        var maybeUser = appUserDao.findById(userId);

        maybeUser.ifPresent(
                (appUser) -> {
                    appUser.setIsActive(true);
                    appUserDao.save(appUser);
                });

        return maybeUser.isPresent();
    }
}
