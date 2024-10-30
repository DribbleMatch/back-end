package com.sideProject.DribbleMatch.controller.region.restController;

import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/region")
@RequiredArgsConstructor
public class RegionRestController {

    private final RegionRepository regionRepository;

    @PostMapping("/getSiGunGuList")
    public List<String> getSiGunGuList(@RequestParam(name = "siDo") String siDo) {
        return regionRepository.findAllSiGunGuBySiDo(siDo);
    }
}
