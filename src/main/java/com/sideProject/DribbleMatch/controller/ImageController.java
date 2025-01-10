package com.sideProject.DribbleMatch.controller;

import com.sideProject.DribbleMatch.common.response.ApiResponse;
import com.sideProject.DribbleMatch.common.util.CommonUtil;
import com.sideProject.DribbleMatch.common.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image")
public class ImageController {

    @Value("${spring.dir.postImagePath}")
    public String path;

    private final FileUtil fileUtil;

    @GetMapping
    public ResponseEntity<Resource> getImage(@RequestParam(name = "imagePath") String imagePath) {
        return fileUtil.getImage(imagePath);
    }

    @PostMapping
    public ApiResponse<String> uploadImage(@RequestParam("file") MultipartFile file) {
        return ApiResponse.ok(fileUtil.saveImage(file, path, FilenameUtils.removeExtension(file.getOriginalFilename()) + LocalDate.now()));
    }
}
