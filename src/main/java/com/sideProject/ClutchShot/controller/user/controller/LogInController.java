package com.sideProject.ClutchShot.controller.user.controller;

import com.sideProject.ClutchShot.dto.user.request.FindInfoRequestDto;
import com.sideProject.ClutchShot.repository.region.RegionRepository;
import com.sideProject.ClutchShot.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/page/login")
public class LogInController {

    private final UserService userService;
    private final RegionRepository regionRepository;

    @GetMapping
    public String login(Model model) {

        return "login/login";
    }

    @GetMapping("/forgetUserInfo")
    public String forgetUserInfoPage(Model model) {
        return "login/forgetUserInfo";
    }

    @PostMapping("/findEmailResult")
    public String findEmailResultPage(Model model,
                                FindInfoRequestDto requestDto) {
        model.addAttribute("emailInfoList", userService.getEmailList(requestDto));
        return "login/findEmailResult";
    }

    @PostMapping("/resetPassword")
    public String resetPasswordPage(Model model,
                                    FindInfoRequestDto requestDto) {
        model.addAttribute("userId", userService.getUserId(requestDto.getEmail()));
        return "login/resetPassword";
    }
}
