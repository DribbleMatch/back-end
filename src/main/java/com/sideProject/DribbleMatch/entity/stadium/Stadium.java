package com.sideProject.DribbleMatch.entity.stadium;

import com.sideProject.DribbleMatch.entity.region.Region;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Stadium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    @NotNull
    private String name;

    @ManyToOne
    @JoinColumn(name = "region_id")
    @NotNull
    private Region region;

    @Column
    @NotNull
    private String detailAddress;

    @Column
    @NotNull
    private int rentalFee;

    @Builder
    public Stadium(String name, Region region, String detailAddress, int rentalFee) {
        this.name = name;
        this.region = region;
        this.detailAddress = detailAddress;
        this.rentalFee = rentalFee;
    }
}
