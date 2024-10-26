package com.sideProject.DribbleMatch.controller.user.controller;

import com.sideProject.DribbleMatch.entity.matching.ENUM.GameKind;
import com.sideProject.DribbleMatch.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/page/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/myPage")
    public String myPage(Model model, Principal principal) {
        model.addAttribute("user", userService.getUserDetail(Long.valueOf(principal.getName())));
        return "myPage/userInfo";
    }

    @GetMapping("/endedMatchingList")
    public String endedMatchingListPage(Model model, Principal principal) {
        return "myPage/endedMatchingList";
    }
}
