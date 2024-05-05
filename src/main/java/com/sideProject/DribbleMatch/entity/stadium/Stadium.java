package com.sideProject.DribbleMatch.entity.stadium;

import com.sideProject.DribbleMatch.entity.region.Region;
import jakarta.persistence.*;

@Entity
public class Stadium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @OneToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @Column
    private String detailAddress;

    @Column
    private int rentalFee;
}
