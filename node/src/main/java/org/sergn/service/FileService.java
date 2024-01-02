package org.sergn.service;

import org.json.JSONException;
import org.sergn.dao.AppPhotoDAO;
import org.sergn.entity.AppDocument;
import org.sergn.entity.AppPhoto;
import org.sergn.service.enums.LinkType;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {
    AppDocument processDoc(Message telegramMessage) throws JSONException;
    AppPhoto processPhoto(Message telegramMessage) throws JSONException;

    String generateLink(Long docId, LinkType linkType);
}
