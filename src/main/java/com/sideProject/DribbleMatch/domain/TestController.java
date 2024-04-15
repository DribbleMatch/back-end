package com.sideProject.DribbleMatch.domain;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class TestController {

    @GetMapping("/")
    public String test() {
        return "success";
    }
}
