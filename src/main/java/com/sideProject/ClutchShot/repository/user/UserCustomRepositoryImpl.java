package com.sideProject.ClutchShot.repository.user;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.sideProject.ClutchShot.entity.user.QUser.user;


@Repository
@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Tuple> findAllEmailByUserInfo(String name, String phone) {
        return jpaQueryFactory
                .select(user.email, user.createdAt)
                .from(user)
                .where(user.name.eq(name).and(user.phone.eq(phone)))
                .fetch();
    }
}
