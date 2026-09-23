package com.sameer.notifyservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class HealthController {
    @GetMapping("/api/test")
    public String health(){
        return  "Notification service is running!";
    }   
}
