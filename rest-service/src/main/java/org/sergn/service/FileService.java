package org.sergn.service;

import org.sergn.entity.AppDocument;
import org.sergn.entity.AppPhoto;
import org.sergn.entity.BinaryContent;
import org.springframework.core.io.FileSystemResource;

public interface FileService {
    AppDocument getDocument(String id);
    AppPhoto getPhoto(String id);
    FileSystemResource getFileSystemResource(BinaryContent binaryContent);
}
