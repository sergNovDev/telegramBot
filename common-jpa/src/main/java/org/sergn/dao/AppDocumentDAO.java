package org.sergn.dao;

import org.sergn.entity.AppDocument;
import org.sergn.entity.BinaryContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppDocumentDAO extends JpaRepository<AppDocument, Long> {
}
