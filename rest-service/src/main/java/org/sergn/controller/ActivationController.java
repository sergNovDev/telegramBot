package org.sergn.controller;

import lombok.RequiredArgsConstructor;
import org.sergn.service.UserActivationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class ActivationController {
    private final UserActivationService userActivationService;


    @GetMapping("/activation")
    public ResponseEntity<?> activation(@RequestParam("id") String id){
        var res = userActivationService.activation(id);
        if (res){
            return ResponseEntity.ok("Активация успешно прошла");
        } else {
            return ResponseEntity.notFound().build();
        }

    }
}
