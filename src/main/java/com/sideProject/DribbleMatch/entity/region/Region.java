package com.sideProject.DribbleMatch.entity.region;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    @NotNull
    private String siDo;

    @Column
    private String siGunGu;

    @Column
    private String eupMyeonDongGu;

    @Column
    private String eupMyeonLeeDong;

    @Column
    private String lee;

    @Column
    @NotNull
    private double latitude;

    @Column
    @NotNull
    private double longitude;

    @Builder
    public Region(String siDo, String siGunGu, String eupMyeonDongGu, String eupMyeonLeeDong, String lee, double latitude, double longitude) {
        this.siDo = siDo;
        this.siGunGu = siGunGu;
        this.eupMyeonDongGu = eupMyeonDongGu;
        this.eupMyeonLeeDong = eupMyeonLeeDong;
        this.lee = lee;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
