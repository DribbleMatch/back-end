package com.sideProject.ClutchShot.controller.user.controller;

import com.sideProject.ClutchShot.dto.user.request.SignupPlayerInfoRequestDto;
import com.sideProject.ClutchShot.dto.user.request.SignupUserInfoRequestDto;
import com.sideProject.ClutchShot.repository.region.RegionRepository;
import com.sideProject.ClutchShot.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/page/signup")
public class SignUpController {

    private final UserService userService;
    private final RegionRepository regionRepository;

    @GetMapping("/agree")
    public String agreePage(Model model) {
        return "signup/agree";
    }

    @GetMapping("/userInfo")
    public String userInfoPage(Model model) {
        return "signup/user_info";
    }

    //refactor: 유효성 더블 체크 구현
    @PostMapping("/playerInfo")
    public String playerInfoPage(Model model,
                                 SignupUserInfoRequestDto requestDto) {
        model.addAttribute("user", requestDto);
        model.addAttribute("siDoList", regionRepository.findAllSiDo());
        return "signup/player_info";
    }

    @PostMapping("/complete")
    public String completePage(Model model,
                               @ModelAttribute SignupPlayerInfoRequestDto requestDto) {
        userService.createUser(requestDto);
        return "signup/complete";
    }
}
