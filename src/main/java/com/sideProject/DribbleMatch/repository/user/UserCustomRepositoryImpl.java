package com.sideProject.DribbleMatch.repository.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

import static com.sideProject.DribbleMatch.entity.user.QUser.user;


@Repository
@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<String> findAllEmailByUserInfo(LocalDate birth, String phone) {
        return jpaQueryFactory
                .select(user.email)
                .from(user)
                .where(user.birth.eq(birth).and(user.phone.eq(phone)))
                .fetch();
    }
}
