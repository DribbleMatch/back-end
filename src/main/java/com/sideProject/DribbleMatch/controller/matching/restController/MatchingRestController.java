package com.sideProject.DribbleMatch.controller.matching.restController;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.dto.matching.request.MatchingCreateRequestDto;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.stadium.Stadium;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import com.sideProject.DribbleMatch.service.matching.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matching")
@RequiredArgsConstructor
public class MatchingRestController {

    private final MatchingService matchingService;

    @PostMapping("/createMatching")
    public Long createMatching(@RequestBody MatchingCreateRequestDto requestDto) {
        return matchingService.createMatching(requestDto);
    }
}
