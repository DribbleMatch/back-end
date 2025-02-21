package com.sideProject.ClutchShot.controller;

import com.sideProject.ClutchShot.common.util.CommonUtil;
import com.sideProject.ClutchShot.dto.matching.response.MatchingSimpleResponseDto;
import com.sideProject.ClutchShot.service.banner.BannerService;
import com.sideProject.ClutchShot.service.matching.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@RequestMapping("/page")
public class MainController {

    private final MatchingService matchingService;
    private final BannerService bannerService;

    @GetMapping
    public String index(ModelMap model,
                        @PageableDefault(page = 0, size = 10) Pageable pageable) {

        Page<MatchingSimpleResponseDto> matchingList = matchingService.searchMatchings("", pageable, LocalDate.now());

        model.addAttribute("bannerList", bannerService.getMainPageBannerList());
        model.addAttribute("dateList", CommonUtil.getDateList(LocalDate.now()));
        model.addAttribute("mobileDateList", IntStream.range(0, 14)
                .mapToObj(i -> LocalDate.now().plusDays(i))
                .collect(Collectors.toList()));
        model.addAttribute("matchingList", matchingList);
        model.addAttribute("currentPage", matchingList.getPageable().getPageNumber());
        model.addAttribute("totalPage", matchingList.getTotalPages());

        return "index";
    }

    @GetMapping("/openLater")
    public String openLater(ModelMap model) {

        return "openLater";
    }
}