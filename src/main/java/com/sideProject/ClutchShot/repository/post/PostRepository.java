package com.sideProject.ClutchShot.repository.post;

import com.sideProject.ClutchShot.entity.Post.ENUM.PostStatus;
import com.sideProject.ClutchShot.entity.Post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {
    public Page<Post> findByStatus(Pageable pageable, PostStatus postStatus);
}
