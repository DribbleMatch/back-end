package com.sideProject.DribbleMatch.controller.user.controller;

import com.sideProject.DribbleMatch.dto.user.request.FindInfoRequestDto;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import com.sideProject.DribbleMatch.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
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
    public String login(ModelMap model) {

        return "login/login";
    }

    @GetMapping("/forgetEmail")
    public String forgetEmailPage(Model model) {
        return "login/forgetEmail";
    }

    @PostMapping("/findEmail")
    public String findEmailPage(Model model, FindInfoRequestDto requestDto) {
        model.addAttribute("emailList", userService.getEmailList(requestDto));
        return "login/findEmail";
    }

    @GetMapping("/forgetPassword")
    public String forgetPasswordPage(Model model) {
        return "login/forgetPassword";
    }

    @PostMapping("/resetPassword")
    public String resetPasswordPage(Model model, FindInfoRequestDto requestDto) {
        model.addAttribute("userId", userService.getUserId(requestDto.getEmail()));
        return "login/resetPassword";
    }
}
