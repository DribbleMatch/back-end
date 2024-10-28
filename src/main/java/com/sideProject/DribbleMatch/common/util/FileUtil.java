package com.sideProject.DribbleMatch.common.util;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FileUtil {

    public void fileCheck(String fileFullPath) {
        File file = new File(fileFullPath);

        if (file.exists()) {
            throw new CustomException(ErrorCode.FILE_EXIST);
        }
    }

    public String saveImage(MultipartFile image, String path, String fileName) {

        //todo: 기본 이미지 처리
        try {
            String filePath = path + File.separator + fileName + extractExt(image.getOriginalFilename());

            File file = new File(filePath);
            image.transferTo(file);

            return filePath;
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_SAVE_FAIL);
        }
    }

    public ResponseEntity<Resource> getImage(String imagePath) {

        File file = new File(imagePath);
        if (!file.exists()) {
            throw new CustomException(ErrorCode.FILE_IMPORT_IMAGE);
        }
        Resource resource = new FileSystemResource(file);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "image/png");
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    private String extractExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
