package com.sideProject.DribbleMatch.repository.region;

import com.sideProject.DribbleMatch.entity.region.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long> {

    //todo: queryDSL 적용 후 매개변수 하나로 변경
    @Query("SELECT r FROM Region r WHERE " +
            "(:siDo IS NULL OR r.siDo = :siDo) " +
            "AND ((:siGunGu IS NULL AND r.siGunGu IS NULL ) OR r.siGunGu = :siGunGu) " +
            "AND ((:eupMyeonDongGu IS NULL AND r.eupMyeonDongGu IS NULL ) OR r.eupMyeonDongGu = :eupMyeonDongGu) " +
            "AND ((:eupMyeonLeeDong IS NULL AND r.eupMyeonLeeDong IS NULL ) OR r.eupMyeonLeeDong = :eupMyeonLeeDong) " +
            "AND ((:lee IS NULL AND r.lee IS NULL ) OR r.lee = :lee)")
    Optional<Region> findByRegionString(String siDo, String siGunGu, String eupMyeonDongGu, String eupMyeonLeeDong, String lee);

    //todo: QueryDSL 적용 후 공백 제거까지 repository layer에서
    @Query("SELECT CONCAT_WS(' ', " +
            "CASE WHEN r.siDo IS NOT NULL THEN r.siDo ELSE '' END," +
            "CASE WHEN r.siGunGu IS NOT NULL THEN r.siGunGu ELSE '' END," +
            "CASE WHEN r.eupMyeonDongGu IS NOT NULL THEN r.eupMyeonDongGu ELSE '' END," +
            "CASE WHEN r.eupMyeonLeeDong IS NOT NULL THEN r.eupMyeonLeeDong ELSE '' END," +
            "CASE WHEN r.lee IS NOT NULL THEN r.lee ELSE '' END) " +
            "FROM Region r")
    String findRegionStringById(Long Id);

    @Query("SELECT r.id FROM Region r WHERE " +
            "(:siDo IS NULL OR r.siDo = :siDo) " +
            "AND (:siGunGu IS NULL OR r.siGunGu = :siGunGu) " +
            "AND (:eupMyeonDongGu IS NULL OR r.eupMyeonDongGu = :eupMyeonDongGu) " +
            "AND (:eupMyeonLeeDong IS NULL OR r.eupMyeonLeeDong = :eupMyeonLeeDong) " +
            "AND (:lee IS NULL OR r.lee = :lee)")
    public List<Long> findIdsByRegionString(String siDo, String siGunGu, String eupMyeonDongGu, String eupMyeonLeeDong, String lee);
}
