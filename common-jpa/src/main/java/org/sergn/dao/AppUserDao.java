package org.sergn.dao;

import org.sergn.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserDao extends JpaRepository<AppUser,Long> {
    Optional<AppUser> findByTelegramUserId(Long id);
    Optional<AppUser> findById(Long id);
    Optional<AppUser> findByEmail(String email);
}
