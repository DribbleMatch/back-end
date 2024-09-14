package com.sideProject.DribbleMatch.controller.teamApplication.restController;

import com.sideProject.DribbleMatch.entity.teamApplication.ENUM.ApplicationStatus;
import com.sideProject.DribbleMatch.repository.teamApplication.TeamApplicationRepository;
import com.sideProject.DribbleMatch.service.teamApplication.TeamApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teamApplication/rest")
public class TeamApplicationRestController {

    private final TeamApplicationService teamApplicationService;

    @GetMapping("/approval/{teamApplicationId}")
    public void approvalTeamApplication(@PathVariable Long teamApplicationId) {
        teamApplicationService.changeTeamApplicationStatus(teamApplicationId, ApplicationStatus.APPROVE);
    }

    @GetMapping("/refuse/{teamApplicationId}")
    public void refuseTeamApplication(@PathVariable Long teamApplicationId) {
        teamApplicationService.changeTeamApplicationStatus(teamApplicationId, ApplicationStatus.REFUSE);
    }

    @PostMapping("/requestJoin")
    public void requestJoin(Principal principal,
                            @RequestParam(name = "id") Long teamId,
                            @RequestParam(name = "introduce") String introduce) {
        teamApplicationService.requestJoin(Long.valueOf(principal.getName()), teamId, introduce);
    }
}
