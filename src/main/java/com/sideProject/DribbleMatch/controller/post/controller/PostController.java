package com.sideProject.DribbleMatch.controller.post.controller;

import com.sideProject.DribbleMatch.entity.Post.Post;
import com.sideProject.DribbleMatch.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/page/post")
public class PostController {

    private final PostService postService;

    @GetMapping("/create")
    public String createPostPage(Model model) {

        return "post/createPost";
    }

    @GetMapping("/detail/{postId}")
    public String postDetailPage(Model model,
                                 @PathVariable Long postId) {
        model.addAttribute("postDetail", postService.getPostDetail(postId));
        postService.plusViewCount(postId);
        return "post/postDetail";
    }

    @GetMapping("/list")
    public String postListPage(Model model,
                               @PageableDefault(page = 0, size = 10) Pageable pageable) {
        model.addAttribute("postList", postService.getPostList(pageable));
        return "post/postList";
    }
}
