package com.sideProject.ClutchShot.service.banner;

import com.sideProject.ClutchShot.common.error.CustomException;
import com.sideProject.ClutchShot.common.error.ErrorCode;
import com.sideProject.ClutchShot.common.util.FileUtil;
import com.sideProject.ClutchShot.dto.banner.BannerMainResponseDto;
import com.sideProject.ClutchShot.dto.post.request.PostCreateRequestDto;
import com.sideProject.ClutchShot.entity.Post.ENUM.HasBanner;
import com.sideProject.ClutchShot.entity.Post.Post;
import com.sideProject.ClutchShot.entity.banner.Banner;
import com.sideProject.ClutchShot.repository.banner.BannerRepository;
import com.sideProject.ClutchShot.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BannerServiceImpl implements BannerService {

    @Value("${spring.dir.bannerImagePath}")
    public String path;

    private final FileUtil fileUtil;

    private final PostRepository postRepository;
    private final BannerRepository bannerRepository;

    @Override
    @Transactional
    public Long createBanner(PostCreateRequestDto requestDto, Long postId) {

        Post post = postRepository.findById(postId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_POST));

        if (post.getHasBanner().equals(HasBanner.NO_BANNER)) {
            return null;
        }

        return bannerRepository.save(Banner.builder()
                        .imagePath(fileUtil.saveImage(requestDto.getBannerImage(), path, requestDto.getTitle()))
                        .post(post)
                        .startAt(requestDto.getBannerStartAt())
                        .endAt(requestDto.getBannerEndAt())
                .build()).getId();
    }

    @Override
    public List<BannerMainResponseDto> getMainPageBannerList() {
        List<Banner> bannerList = bannerRepository.selectMainBannerList();

        return bannerList.stream()
                .map(banner -> BannerMainResponseDto.builder()
                        .imagePath(banner.getImagePath())
                        .postId(banner.getPost().getId())
                        .build())
                .collect(Collectors.toList());
    }
}
