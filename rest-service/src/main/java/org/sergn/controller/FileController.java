package org.sergn.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sergn.service.FileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @GetMapping("/get-photo")
    public ResponseEntity<?> getPhoto(@RequestParam("id") String id){
        //TODO для формирования badRequest добавить ControllerAdvice
        var photo = fileService.getPhoto(id);
        if (photo==null) {
            return ResponseEntity.badRequest().build();
        }
        var binaryContent = photo.getBinaryContent();
        var fileSystemResource = fileService.getFileSystemResource(binaryContent);
        if (fileSystemResource == null){
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                //Если не добавить Content-Disposition то файл сразу откроется в окне браузера без скачивания
                .header("Content-disposition","attachment;") //телега не хранит имя файла
                .body(fileSystemResource);
    }


    @GetMapping("/get-doc")
    public ResponseEntity<?> getDoc(@RequestParam("id") String id){
        //TODO для формирования badRequest добавить ControllerAdvice
        var doc = fileService.getDocument(id);
        if (doc==null) {
            return ResponseEntity.badRequest().build();
        }
        var binaryContent = doc.getBinaryContent();
        var fileSystemResource = fileService.getFileSystemResource(binaryContent);
        if (fileSystemResource == null){
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.getMimeType()))
                //Если не добавить Content-Disposition то файл сразу откроется в окне браузера без скачивания
                .header("Content-disposition","attachment; filename="+doc.getDocName())
                .body(fileSystemResource);
    }

}
