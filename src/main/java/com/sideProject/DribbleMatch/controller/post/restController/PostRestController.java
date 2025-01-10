package com.sideProject.DribbleMatch.controller.post.restController;

import com.sideProject.DribbleMatch.common.response.ApiResponse;
import com.sideProject.DribbleMatch.dto.post.request.PostCreateRequestDto;
import com.sideProject.DribbleMatch.service.componentService.BannerPost.BannerPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostRestController {

    private final BannerPostService bannerPostService;

    @PostMapping("/create")
    public ApiResponse<Long> createPost(@ModelAttribute PostCreateRequestDto requestDto) {
        return ApiResponse.ok(bannerPostService.createPost(requestDto));
    }
}
