package com.sideProject.DribbleMatch.repository.post;

import com.sideProject.DribbleMatch.entity.Post.ENUM.PostStatus;
import com.sideProject.DribbleMatch.entity.Post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;

public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {
    public Page<Post> findByStatus(Pageable pageable, PostStatus postStatus);
}
