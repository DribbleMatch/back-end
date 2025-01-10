package com.sideProject.DribbleMatch.repository.post;

import com.sideProject.DribbleMatch.entity.Post.Post;

import java.util.List;

public interface PostCustomRepository {

    public List<Post> searchPostsPreNowNextById(Long postId);
    public void plusViewCount(Long postId);
}
