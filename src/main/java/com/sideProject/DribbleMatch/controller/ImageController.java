package com.sideProject.DribbleMatch.controller;

import com.sideProject.DribbleMatch.common.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image")
public class ImageController {

    private final FileUtil fileUtil;

    @GetMapping
    public ResponseEntity<Resource> getImage(@RequestParam String imagePath) {
        return fileUtil.getImage(imagePath);
    }
}
