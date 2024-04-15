package com.sideProject.DribbleMatch.domain;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.common.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class TestController {

    @GetMapping("/")
    public ApiResponse<?> test() {
        throw new CustomException(ErrorCode.TEST_ERROR);
    }
}
