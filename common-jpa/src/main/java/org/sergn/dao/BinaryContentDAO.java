package org.sergn.dao;

import org.sergn.entity.BinaryContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BinaryContentDAO extends JpaRepository<BinaryContent,Long> {
}
