package com.sideProject.DribbleMatch.repository.region;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideProject.DribbleMatch.entity.region.Region;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.sideProject.DribbleMatch.entity.region.QRegion.region;

@Repository
@RequiredArgsConstructor
public class RegionCustomRepositoryImpl implements RegionCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Region> findByRegionString(String regionString) {

        List<String> regionStringList = new ArrayList<>(List.of(regionString.split(" "))); // 가변 리스트 생성

        while (regionStringList.size() < 5) {
            regionStringList.add(null);
        }

        BooleanExpression predicate = region.siDo.eq(regionStringList.get(0))
                .and(regionStringList.get(1) == null ? region.siGunGu.isNull() : region.siGunGu.eq(regionStringList.get(1)))
                .and(regionStringList.get(2) == null ? region.eupMyeonDongGu.isNull() : region.eupMyeonDongGu.eq(regionStringList.get(2)))
                .and(regionStringList.get(3) == null ? region.eupMyeonLeeDong.isNull() : region.eupMyeonLeeDong.eq(regionStringList.get(3)))
                .and(regionStringList.get(4) == null ? region.lee.isNull() : region.lee.eq(regionStringList.get(4)));

        return Optional.ofNullable(jpaQueryFactory.selectFrom(region)
                .where(predicate)
                .fetchOne());
    }
    @Override
    public Optional<String> findRegionStringById(Long regionId) {
        //todo: null 처리
        
        return Optional.of(Objects.toString(jpaQueryFactory
                .select(
                        Expressions.stringTemplate(
                                "concat(" +
                                        "coalesce({0}, ''), " +
                                        "coalesce({1}, ''), " +
                                        "coalesce({2}, ''), " +
                                        "coalesce({3}, ''), " +
                                        "coalesce({4}, '')" +
                                        ")",
                                region.siDo.concat(" "),
                                region.siGunGu.concat(" "),
                                region.eupMyeonDongGu.concat(" "),
                                region.eupMyeonLeeDong.concat(" "),
                                region.lee
                        )
                )
                .from(region)
                .where(region.id.eq(regionId))
                .fetchOne()).trim());
    }

    @Override
    public List<Long> findIdsByRegionString(String regionString) {

        List<String> regionStringList = new ArrayList<>(List.of(regionString.split(" ")));

        while (regionStringList.size() < 5) {
            regionStringList.add(null);
        }

        BooleanExpression predicate = region.siDo.eq(regionStringList.get(0))
                .and(regionStringList.get(1) == null?null:region.siGunGu.eq(regionStringList.get(1)))
                .and(regionStringList.get(2) == null?null:region.eupMyeonDongGu.eq(regionStringList.get(2)))
                .and(regionStringList.get(3) == null?null:region.eupMyeonLeeDong.eq(regionStringList.get(3)))
                .and(regionStringList.get(4) == null?null:region.lee.eq(regionStringList.get(4)));

        return jpaQueryFactory.select(region.id)
                .from(region)
                .where(predicate)
                .fetch();
    }
}
