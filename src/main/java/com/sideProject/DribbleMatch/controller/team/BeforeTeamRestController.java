//package com.sideProject.DribbleMatch.controller.team;
//
//import com.sideProject.DribbleMatch.common.response.ApiResponse;
//import com.sideProject.DribbleMatch.dto.team.request.TeamCreateRequestDto;
//import com.sideProject.DribbleMatch.dto.team.request.TeamJoinRequestDto;
//import com.sideProject.DribbleMatch.dto.team.request.TeamUpdateRequestDto;
//import com.sideProject.DribbleMatch.dto.team.response.TeamApplicationResponseDto;
//import com.sideProject.DribbleMatch.dto.team.response.TeamDetailResponseDto;
//import com.sideProject.DribbleMatch.service.team.TeamService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.web.bind.annotation.*;
//
//import java.security.Principal;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/team")
//public class BeforeTeamRestController {
//
//    private final TeamService teamService;
//
//    @PostMapping
//    public ApiResponse<Long> createTeam(Principal principal, @RequestBody @Valid TeamCreateRequestDto request) {
//        return ApiResponse.ok(teamService.createTeam(Long.valueOf(principal.getName()), request));
//    }
//
//    @PutMapping("/{teamId}")
//    public ApiResponse<Long> updateTeam(Principal principal, @PathVariable("teamId") Long teamId, @RequestBody @Valid TeamUpdateRequestDto request) {
//        return ApiResponse.ok(teamService.updateTeam(Long.valueOf(principal.getName()), teamId, request));
//    }
//
//    @DeleteMapping("/{teamId}")
//    public ApiResponse<String> deleteTeam(Principal principal, @PathVariable("teamId")  Long teamId) {
//        return ApiResponse.ok(teamService.deleteTeam(Long.valueOf(principal.getName()), teamId));
//    }
//
//    @GetMapping("/teams")
//    public ApiResponse<Page<TeamDetailResponseDto>> findAllTeams(@PageableDefault(size = 10) Pageable pageable, @RequestParam(name = "regionString") String regionString) {
//        return ApiResponse.ok(teamService.findAllTeams(pageable, regionString));
//    }
//
//    @GetMapping("/{teamId}")
//    public ApiResponse<TeamDetailResponseDto> findTeam(@PathVariable("teamId") Long teamId) {
//        return ApiResponse.ok(teamService.findTeam(teamId));
//    }
//
//    @PostMapping("/{teamId}/join")
//    public ApiResponse<Long> application(
//            Principal principal,
//            @PathVariable("teamId") Long teamId,
//            @RequestBody @Valid TeamJoinRequestDto request
//            ) {
//        return ApiResponse.ok(
//                teamService.join(request,teamId,Long.valueOf(principal.getName()))
//        );
//    }
//    @DeleteMapping("/application/{applicationId}")
//    public ApiResponse<Long> cancelApplication(
//            Principal principal,
//            @PathVariable("applicationId") Long applicationId,
//            @RequestBody @Valid TeamJoinRequestDto request
//    ) {
//        return ApiResponse.ok(
//                teamService.cancel(applicationId,Long.valueOf(principal.getName()))
//        );
//    }
//
//    @GetMapping("/application/{teamId}")
//    public ApiResponse<Page<TeamApplicationResponseDto>> findApplication(
//            Principal principal,
//            @PageableDefault(size = 10) Pageable pageable,
//            @PathVariable("teamId") Long teamId
//    ) {
//        return ApiResponse.ok(
//                teamService.findApplication(pageable,teamId)
//        );
//    }
//
//    @PostMapping("/application/{applicationId}/approve")
//    public ApiResponse<Long> approve(
//            Principal principal,
//            @PathVariable("applicationId") Long applicationId
//    ) {
//        return ApiResponse.ok(
//                teamService.approve(applicationId,Long.valueOf(principal.getName()))
//        );
//    }
//
//    @PostMapping("/application/{applicationId}/refuse")
//    public ApiResponse<Long> refuse(
//            Principal principal,
//            @PathVariable("applicationId") Long applicationId
//    ) {
//        return ApiResponse.ok(
//                teamService.refuse(applicationId,Long.valueOf(principal.getName()))
//        );
//    }
//}
