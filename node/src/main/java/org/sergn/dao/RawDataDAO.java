package org.sergn.dao;

import org.sergn.entity.RawData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RawDataDAO extends JpaRepository<RawData,Long> {
}
