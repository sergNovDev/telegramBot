package org.sergn.service;

import org.sergn.entity.AppUser;

public interface AppUserService {
    String registerUser(AppUser appUser);
    String setEmail(AppUser appUser, String mail);
}
