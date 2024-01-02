package org.sergn.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.HttpHead;
import org.json.JSONException;
import org.json.JSONObject;
import org.sergn.CryptoTool;
import org.sergn.dao.AppDocumentDAO;
import org.sergn.dao.AppPhotoDAO;
import org.sergn.dao.BinaryContentDAO;
import org.sergn.entity.AppDocument;
import org.sergn.entity.AppPhoto;
import org.sergn.entity.BinaryContent;
import org.sergn.exception.UploadFileException;
import org.sergn.service.FileService;
import org.sergn.service.enums.LinkType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    @Value("${token}")
    private String token;

    @Value("${service.file_info.uri}")
    private String fileInfoUri;

    @Value("${service.file_storage.uri}")
    private String fileStorageUri;

    @Value("${link.address}")
    private String linkAddress;

    private final AppDocumentDAO appDocumentDAO;
    private final AppPhotoDAO appPhotoDAO;
    private final BinaryContentDAO binaryContentDAO;
    private final CryptoTool cryptoTool;

    @Override
    public AppPhoto processPhoto(Message telegramMessage) throws JSONException {
        var photoSizeCount = telegramMessage.getPhoto().size();
        var photoIndex = photoSizeCount > 1 ? telegramMessage.getPhoto().size()-1 : 0;
        //TODO пока обрабатываем только одно фото
        var appPhoto = telegramMessage.getPhoto().get(photoIndex);
        ResponseEntity<String> response = getFilePath(appPhoto.getFileId());
        if (response.getStatusCode() == HttpStatus.OK){
            BinaryContent persistentBinaryContent = getBinaryContent(response);
            AppPhoto transientAppPhoto = buildTransientAppPhoto(appPhoto, persistentBinaryContent);
            return appPhotoDAO.save(transientAppPhoto);
        }
        else {
            throw new UploadFileException("Bad response from telegram service: " + response);
        }
    }

    @Override
    public String generateLink(Long docId, LinkType linkType) {
        String hash = cryptoTool.hashOf(docId);
        return "http://"+linkAddress+"/"+linkType+"?id="+hash;
    }

    private AppPhoto buildTransientAppPhoto(PhotoSize appPhoto, BinaryContent persistentBinaryContent) {
        return AppPhoto.builder()
                .telegramFileId(appPhoto.getFileId())
                .binaryContent(persistentBinaryContent)
                .fileSize(appPhoto.getFileSize())
                .build();
    }

    @Override
    public AppDocument processDoc(Message telegramMessage) throws JSONException {
        String fileId = telegramMessage.getDocument().getFileId();
        ResponseEntity<String> response = getFilePath(fileId);
        if (response.getStatusCode() == HttpStatus.OK){
            BinaryContent persistentBinaryContent = getBinaryContent(response);
            Document telegramDoc = telegramMessage.getDocument();
            AppDocument transientAppDoc = buildTransientAppDoc(telegramDoc, persistentBinaryContent);
            return appDocumentDAO.save(transientAppDoc);
        }
        else {
            throw new UploadFileException("Bad response from telegram service: " + response);
        }
    }

    private BinaryContent getBinaryContent(ResponseEntity<String> response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response.getBody());
        String filePath = String.valueOf(jsonObject
                .getJSONObject("result")
                .getString("file_path"));

        byte[] fileInByte = downloadFile(filePath);
        BinaryContent transientBinaryContent = BinaryContent.builder()
                .fileAsArrayOfBytes(fileInByte)
                .build();
        BinaryContent persistentBinaryContent = binaryContentDAO.save(transientBinaryContent);
        return persistentBinaryContent;
    }

    private AppDocument buildTransientAppDoc(Document telegramDoc, BinaryContent persistentBinaryContent){
        return AppDocument.builder()
                .telegramFieldId(telegramDoc.getFileId())
                .docName(telegramDoc.getFileName())
                .binaryContent(persistentBinaryContent)
                .mimeType(telegramDoc.getMimeType())
                .fileSize(telegramDoc.getFileSize())
                .build();
    }

    private ResponseEntity<String> getFilePath(String fileId){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);

        return restTemplate.exchange(
                fileInfoUri,
                HttpMethod.GET,
                request,
                String.class,
                token,
                fileId);
    }

    private byte[] downloadFile(String filePath){
        String fullUri = fileStorageUri
                .replace("{token}", token)
                .replace("{filePath}", filePath);
        URL urlObj = null;

        try {
            urlObj = new URL(fullUri);
        } catch (MalformedURLException e) {
            throw new UploadFileException("my Error call URL",e);
        }

        //TODO подумать над оптимизацией
        //Для небольших файлов будет работать, но для больших это проблема
        try(InputStream is = urlObj.openStream()){
            return is.readAllBytes();
        } catch (IOException e) {
            throw new UploadFileException(urlObj.toExternalForm(),e);
        }

    }
}
