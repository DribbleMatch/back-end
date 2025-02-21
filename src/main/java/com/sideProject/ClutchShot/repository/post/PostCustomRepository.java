package com.sideProject.ClutchShot.repository.post;

import com.sideProject.ClutchShot.entity.Post.Post;

import java.util.List;

public interface PostCustomRepository {

    public List<Post> searchPostsPreNowNextById(Long postId);
    public void plusViewCount(Long postId);
}
