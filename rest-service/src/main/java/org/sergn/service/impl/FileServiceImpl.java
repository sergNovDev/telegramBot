package org.sergn.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.aspectj.util.FileUtil;
import org.sergn.CryptoTool;
import org.sergn.dao.AppDocumentDAO;
import org.sergn.dao.AppPhotoDAO;
import org.sergn.entity.AppDocument;
import org.sergn.entity.AppPhoto;
import org.sergn.entity.BinaryContent;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements org.sergn.service.FileService {
    private final AppDocumentDAO appDocumentDAO;
    private final AppPhotoDAO appPhotoDAO;
    private final CryptoTool cryptoTool;

    @Override
    public AppDocument getDocument(String docId) {
        var id = cryptoTool.idOf(docId);
        if (id==null){
            return null;
        }
       // var id = Long.parseLong(aLong);
        return appDocumentDAO.findById(id).orElse(null);
    }

    @Override
    public AppPhoto getPhoto(String docId) {
        var id = cryptoTool.idOf(docId);
        if (id==null){
            return null;
        }
        //var id = Long.parseLong(docId);
        return appPhotoDAO.findById(id).orElse(null);
    }

    @Override
    public FileSystemResource getFileSystemResource(BinaryContent binaryContent) {
        try{
            //TODO добавить генерацию имени временного файла
            File temp = File.createTempFile("tempFile",".bin");
            temp.deleteOnExit();
            FileUtils.writeByteArrayToFile(temp,binaryContent.getFileAsArrayOfBytes());
            return new FileSystemResource(temp);
        }
        catch(IOException e){
           // e.printStackTrace();
            log.error(e.getMessage());
            return null;
        }
    }
}
