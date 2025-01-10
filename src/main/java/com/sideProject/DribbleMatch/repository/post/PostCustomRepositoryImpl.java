package com.sideProject.DribbleMatch.repository.post;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.entity.Post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.sideProject.DribbleMatch.entity.Post.QPost.post;

@RequiredArgsConstructor
@Repository
public class PostCustomRepositoryImpl implements PostCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> searchPostsPreNowNextById(Long postId) {

        Post currentPost = jpaQueryFactory
                .selectFrom(post)
                .where(post.id.eq(postId))
                .fetchOne();

        if (currentPost == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_POST);
        }

        Post previousPost = jpaQueryFactory
                .selectFrom(post)
                .where(post.createdAt.lt(currentPost.getCreatedAt()))
                .orderBy(post.createdAt.desc())
                .limit(1)
                .fetchOne();

        Post nextPost = jpaQueryFactory
                .selectFrom(post)
                .where(post.createdAt.gt(currentPost.getCreatedAt()))
                .orderBy(post.createdAt.asc())
                .limit(1)
                .fetchOne();

        List<Post> result = new ArrayList<>();

        result.add(previousPost);
        result.add(currentPost);
        result.add(nextPost);

        return result;
    }

    @Override
    public void plusViewCount(Long postId) {
        jpaQueryFactory
                .update(post)
                .set(post.viewCount, post.viewCount.add(1))
                .where(post.id.eq(postId))
                .execute();
    }
}
