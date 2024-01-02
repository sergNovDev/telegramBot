package org.sergn.service.impl;

import org.junit.jupiter.api.Test;
import org.sergn.dao.RawDataDAO;
import org.sergn.entity.RawData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MainServiceImplTest {

    @Autowired
    private RawDataDAO rawDataDAO;

    @Test
    public void testSaveRawDate(){
        Update update = new Update();
        Message msg = new Message();

        msg.setText("ololol");
        RawData rawData = RawData.builder()
                .event(update)
                .build();

        Set<RawData> testData = new HashSet<>();
        testData.add(rawData);
        rawDataDAO.save(rawData);

        Assert.isTrue(testData.contains(rawData),"Entity not found in the set");
    }

}