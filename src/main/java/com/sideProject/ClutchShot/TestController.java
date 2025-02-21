package com.sideProject.ClutchShot;

import com.sideProject.ClutchShot.service.teamApplication.TeamApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TeamApplicationService teamApplicationService;

    @GetMapping("/healthcheck")
    public String healthcheck() {
        return "OK";
    }
}
