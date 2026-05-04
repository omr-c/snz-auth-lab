package com.esenezeta.lab_security_jwt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping ("/hello")
    public String hello() {
        return "Hola esenenzeta, el ,muro te dejo pasar";
    }
}
