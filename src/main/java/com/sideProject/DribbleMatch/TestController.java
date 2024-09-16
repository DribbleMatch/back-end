package com.sideProject.DribbleMatch;

import com.sideProject.DribbleMatch.common.response.ApiResponse;
import com.sideProject.DribbleMatch.common.util.FileUtil;
import com.sideProject.DribbleMatch.dto.team.request.TeamCreateRequestDto;
import com.sideProject.DribbleMatch.service.teamApplication.TeamApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class TestController {

    private final TeamApplicationService teamApplicationService;

    @GetMapping("/test")
    public String getApplicationList(Model model) {
        model.addAttribute("teamApplicationList", teamApplicationService.findTeamApplicationsByTeam(1L));
        return "test";
    }
}
