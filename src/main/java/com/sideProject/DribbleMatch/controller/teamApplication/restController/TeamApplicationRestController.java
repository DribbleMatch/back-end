package com.sideProject.DribbleMatch.controller.teamApplication.restController;

import com.sideProject.DribbleMatch.common.response.ApiResponse;
import com.sideProject.DribbleMatch.entity.teamApplication.ENUM.ApplicationStatus;
import com.sideProject.DribbleMatch.repository.teamApplication.TeamApplicationRepository;
import com.sideProject.DribbleMatch.service.teamApplication.TeamApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teamApplication")
public class TeamApplicationRestController {

    private final TeamApplicationService teamApplicationService;

    @PostMapping("/requestJoin")
    public ApiResponse<Long> requestJoin(Principal principal,
                                   @RequestParam(name = "id") Long teamId,
                                   @RequestParam(name = "introduce") String introduce) {

        return ApiResponse.ok(teamApplicationService.createTeamApplication(Long.valueOf(principal.getName()), teamId, introduce));
    }

    @GetMapping("/approval/{teamApplicationId}")
    public ApiResponse<Long> approvalTeamApplication(@PathVariable Long teamApplicationId) {

        return ApiResponse.ok(teamApplicationService.changeTeamApplicationStatus(teamApplicationId, ApplicationStatus.APPROVE));
    }

    @GetMapping("/refuse/{teamApplicationId}")
    public ApiResponse<Long> refuseTeamApplication(@PathVariable Long teamApplicationId) {

        return ApiResponse.ok(teamApplicationService.changeTeamApplicationStatus(teamApplicationId, ApplicationStatus.REFUSE));
    }
}
