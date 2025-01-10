package com.sideProject.DribbleMatch.repository.banner;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideProject.DribbleMatch.entity.banner.Banner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.sideProject.DribbleMatch.entity.banner.QBanner.banner;

@Repository
@RequiredArgsConstructor
public class BannerCustomRepositoryImpl implements BannerCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Banner> selectMainBannerList() {

        LocalDateTime now = LocalDateTime.now();

        return jpaQueryFactory
                .selectFrom(banner)
                .where(banner.startAt.loe(LocalDate.from(now))
                        .and(banner.endAt.goe(LocalDate.from(now)))
                )
                .fetch();
    }
}
