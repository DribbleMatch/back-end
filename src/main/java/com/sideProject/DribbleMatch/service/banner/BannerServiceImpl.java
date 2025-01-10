package com.sideProject.DribbleMatch.service.banner;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.common.util.FileUtil;
import com.sideProject.DribbleMatch.dto.banner.BannerMainResponseDto;
import com.sideProject.DribbleMatch.dto.post.request.PostCreateRequestDto;
import com.sideProject.DribbleMatch.dto.post.response.PostListResponseDto;
import com.sideProject.DribbleMatch.entity.Post.ENUM.HasBanner;
import com.sideProject.DribbleMatch.entity.Post.Post;
import com.sideProject.DribbleMatch.entity.banner.Banner;
import com.sideProject.DribbleMatch.repository.banner.BannerRepository;
import com.sideProject.DribbleMatch.repository.post.PostRepository;
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
