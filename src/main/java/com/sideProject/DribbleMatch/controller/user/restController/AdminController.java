package com.sideProject.DribbleMatch.controller.user.restController;

import com.sideProject.DribbleMatch.common.response.ApiResponse;
import com.sideProject.DribbleMatch.service.user.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @PutMapping("/{userId}")
    public ApiResponse<Long> changeUserToAdmin(@PathVariable Long userId) {
        return ApiResponse.ok(adminService.changeToAdmin(userId));
    }
}
