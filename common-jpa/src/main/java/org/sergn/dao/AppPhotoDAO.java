package org.sergn.dao;

import org.sergn.entity.AppPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppPhotoDAO extends JpaRepository<AppPhoto,Long> {
}
