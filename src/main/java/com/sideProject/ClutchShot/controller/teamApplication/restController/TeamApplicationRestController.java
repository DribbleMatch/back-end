package com.sideProject.ClutchShot.controller.teamApplication.restController;

import com.sideProject.ClutchShot.common.response.ApiResponse;
import com.sideProject.ClutchShot.entity.teamApplication.ENUM.ApplicationStatus;
import com.sideProject.ClutchShot.service.teamApplication.TeamApplicationService;
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
