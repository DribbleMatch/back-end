package com.sideProject.DribbleMatch.repository.region;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
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

    //todo: findRegionStringById
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Region> findByRegionString(String regionString) {

        List<String> regionStringList = new ArrayList<>(List.of(regionString.split(" "))); // 가변 리스트 생성

        while (regionStringList.size() < 5) {
            regionStringList.add(null);
        }

        BooleanExpression predicate = region.siDo.eq(regionStringList.get(0))
                .and(regionStringList.get(1) == null ? region.siGunGu.eq("") : region.siGunGu.eq(regionStringList.get(1)))
                .and(regionStringList.get(2) == null ? region.eupMyeonDongGu.eq("") : region.eupMyeonDongGu.eq(regionStringList.get(2)))
                .and(regionStringList.get(3) == null ? region.eupMyeonLeeDong.eq("") : region.eupMyeonLeeDong.eq(regionStringList.get(3)))
                .and(regionStringList.get(4) == null ? region.lee.eq("") : region.lee.eq(regionStringList.get(4)));

        return Optional.ofNullable(jpaQueryFactory.selectFrom(region)
                .where(predicate)
                .fetchOne());
    }
    @Override
    public Optional<String> findRegionStringById(Long regionId) {

         Tuple tuple = jpaQueryFactory.select(
                region.siDo,
                region.siGunGu,
                region.eupMyeonDongGu,
                region.eupMyeonLeeDong,
                region.lee
                ).from(region)
                 .where(region.id.eq(regionId))
                 .fetchOne();

         String result = "";
         if (tuple != null) {
             result = tuple.get(region.siDo).equals("") ? result : result.concat(tuple.get(region.siDo) + " ");
             result = tuple.get(region.siGunGu).equals("") ? result : result.concat(tuple.get(region.siGunGu) + " ");
             result = tuple.get(region.eupMyeonDongGu).equals("") ? result : result.concat(tuple.get(region.eupMyeonDongGu) + " ");
             result = tuple.get(region.eupMyeonLeeDong).equals("") ? result : result.concat(tuple.get(region.eupMyeonLeeDong) + " ");
             result = tuple.get(region.lee).equals("") ? result : result.concat(tuple.get(region.lee) + " ");
         }

         if (result.isBlank()) {
             return Optional.empty();
         }

         return Optional.of(result.trim());
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

    @Override
    public List<String> findAllSiDo() {
        return jpaQueryFactory.select(region.siDo)
                .from(region)
                .groupBy(region.siDo)
                .fetch();
    }

    public List<String> findAllSiGunGuBySiDo(String siDo) {
        return jpaQueryFactory.select(region.siGunGu)
                .from(region)
                .groupBy(region.siGunGu)
                .where(region.siDo.eq(siDo)
                        .and(region.siGunGu.isNotEmpty()))
                .fetch();
    }
}