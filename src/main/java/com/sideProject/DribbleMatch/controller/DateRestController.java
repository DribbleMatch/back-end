package com.sideProject.DribbleMatch.controller;

import com.sideProject.DribbleMatch.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/date")
public class DateRestController {

    @GetMapping("/next")
    public ApiResponse<LocalDate> getNextDateString(@RequestParam(name = "selectedDate") LocalDate selectedDate) {
        LocalDate returnDate = selectedDate.plusDays(2);
        return ApiResponse.ok(returnDate);
    }

    @GetMapping("/pre")
    public ApiResponse<LocalDate> getPreDateString(@RequestParam(name = "selectedDate") LocalDate selectedDate) {
        LocalDate returnDate = selectedDate.minusDays(2);
        return ApiResponse.ok(returnDate);
    }
}
