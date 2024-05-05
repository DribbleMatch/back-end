package com.sideProject.DribbleMatch.dto.region;

import com.sideProject.DribbleMatch.entity.region.Region;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
public class RegionCsvDto {
    //todo: setter를 사용하지 않는 방법 (fieldSetMapper 직접 구현?) 고안하기
    private String siDo;
    private String siGunGu;
    private String eupMyeonDongGu;
    private String eupMyeonLeeDong;
    private String lee;
    private double latitude;
    private double longitude;

    @Builder
    public RegionCsvDto(String siDo, String siGunGu, String eupMyeonDongGu, String eupMyeonLeeDong, String lee, double latitude, double longitude) {
        this.siDo = siDo;
        this.siGunGu = siGunGu;
        this.eupMyeonDongGu = eupMyeonDongGu;
        this.eupMyeonLeeDong = eupMyeonLeeDong;
        this.lee = lee;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Region of(RegionCsvDto regionCsvDto) {
        return Region.builder()
                .siDo(regionCsvDto.siDo)
                .siGunGu(regionCsvDto.siGunGu)
                .eupMyeonDongGu(regionCsvDto.eupMyeonDongGu)
                .eupMyeonLeeDong(regionCsvDto.eupMyeonLeeDong)
                .lee(regionCsvDto.lee)
                .latitude(regionCsvDto.latitude)
                .longitude(regionCsvDto.longitude)
                .build();
    }
}
