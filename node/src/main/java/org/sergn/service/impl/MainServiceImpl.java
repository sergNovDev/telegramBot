package org.sergn.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.sergn.dao.AppDocumentDAO;
import org.sergn.dao.RawDataDAO;
import org.sergn.dao.AppUserDao;
import org.sergn.entity.AppDocument;
import org.sergn.entity.AppPhoto;
import org.sergn.entity.AppUser;
import org.sergn.entity.RawData;
import org.sergn.entity.enums.UserState;
import org.sergn.service.AppUserService;
import org.sergn.service.FileService;
import org.sergn.service.MainService;
import org.sergn.service.ProducerService;
import org.sergn.service.enums.LinkType;
import org.sergn.service.enums.ServiceCommands;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import static org.sergn.entity.enums.UserState.BASIC_STATE;
import static org.sergn.entity.enums.UserState.WAIT_FOR_EMAIL_STATE;
import static org.sergn.service.enums.ServiceCommands.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {
    private final RawDataDAO rawDataDAO;
    private final ProducerService producerService;
    private final AppUserDao appUserDAO;
    private final FileService fileService;
    private final AppUserService appUserService;

    @Override
    public void processTextMessage(Update update) {

        saveRawData(update);
        var appUser=findOrSaveAppUser(update);
        var userState = appUser.getState();
        String text = update.getMessage().getText();
        var output = "";

        //var serviceCommand = ServiceCommand.fromValue(text);

        if (CANCEL.equals(text)){
            output = cancelProcess(appUser);
        } else if (BASIC_STATE.equals(userState)){
            output = processServiceCommand(appUser, text);
        } else if (WAIT_FOR_EMAIL_STATE.equals(userState)){
            //TODO сделаем позже
            appUserService.setEmail(appUser, text);
        } else {
            log.error("Unknown user state: "+userState);
            output = "Неизвестная ошибка! Введите /cancel и попробуйте снова!";
        }

        var chatId = update.getMessage().getChatId();
        sendAnswer(output, chatId);
    }

    @Override
    public void processDocMessage(Update update) {
        saveRawData(update);
        var appUser=findOrSaveAppUser(update);
        var chatId = update.getMessage().getChatId();

        //Иногда будет запрещена загрузка
        if (isNotAllowToSendContent(chatId, appUser)){
            return;
        }

        try{
            AppDocument doc = fileService.processDoc(update.getMessage());
            String link = fileService.generateLink(doc.getId(), LinkType.GET_DOC);
            var answer = "Документ загружен ссылка для скачивания: "+link;
            sendAnswer(answer, chatId);
        } catch (JSONException e) {
           String error = "К сожалению, загрузка файла не удалась. Повторите попытку позже.";
           sendAnswer(error, chatId);
        }

    }

    private boolean isNotAllowToSendContent(Long chatId, AppUser appUser) {
        UserState state = appUser.getState();
        if (!appUser.getIsActive()){
            var error = "Зарегистрируйтесь или активируйте свою учетную запись для загрузки контента";
            sendAnswer(error, chatId);
            return true;
        } else if (!BASIC_STATE.equals(state)){
            var error = "Отмените текущую команду с помощью /cancel для отправки файлов";
            sendAnswer(error, chatId);
            return true;
        }
        return false;
    }

    @Override
    public void processPhotoMessage(Update update) {
        saveRawData(update);
        var appUser=findOrSaveAppUser(update);
        var chatId = update.getMessage().getChatId();

        //Иногда будет запрещена загрузка
        if (isNotAllowToSendContent(chatId, appUser)){
            return;
        }


        try {
            AppPhoto photo = fileService.processPhoto(update.getMessage());
            String link = fileService.generateLink(photo.getId(), LinkType.GET_PHOTO);
            var answer = "Фото успешно загружено!"
                         + " Cсылка для скачивания: "+link;
            sendAnswer(answer, chatId);
        } catch (JSONException e) {
            //TODO добавить загрузку документа
            var answer = "Ошибка загрузки фото! Повторите попытку позже";
            sendAnswer(answer, chatId);
        }

    }

    private String processServiceCommand(AppUser appUser, String cmd) {
        if (REGISTRATION.equals(cmd)){
            //TODO добавить регистрацию
            //appUserService.registerUser(appUser);
            return "Временно недоступно";
        } else if (HELP.equals(cmd)){
            return help();
        } else if (START.equals(cmd)){
            return "Приветствую! Чтобы посмотреть список доступных команд введите /help";
        } else{
            return "Неизвестная команда!";
        }

    }

    private String help(){
        return "Список дсотупных команд:\n"
                + "/cancel - отмена выполнения текущей команды;\n"
                + "/registration - регистрация пользователя.";
    }

    private String cancelProcess(AppUser appUser) {
        appUser.setState(BASIC_STATE);
        appUserDAO.save(appUser);
        return "Команда отменена!";
    }

    private void sendAnswer(String output, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);
        producerService.producerAnswer(sendMessage);
    }

    private void saveRawData(Update update) {
        RawData rawData = RawData.builder()
                .event(update)
                .build();

        rawDataDAO.save(rawData);
    }

    private AppUser findOrSaveAppUser(Update update){
        Message textMessage = update.getMessage();
        User telegramUser = textMessage.getFrom();

        var persistentAppUser = appUserDAO.findByTelegramUserId(telegramUser.getId());
        if (persistentAppUser.isEmpty()){
            AppUser transientAppUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    .username(telegramUser.getUserName())
                    //TODO изменить значение по умолчанию после добавления регистрации
                    .isActive(true)
                    .state(BASIC_STATE)
                    .build();
            return appUserDAO.save(transientAppUser);
        }
        return persistentAppUser.get();
    }

}
