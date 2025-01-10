$(document).ready(function () {

    const $banner = $('#banner');
    const $pages = $('.banner-page');
    const banners = $banner.children();
    const bannerCount = banners.length;
    let currentIndex = 0;

    // 1. 앞뒤로 복제 슬라이드 추가
    $banner.append(banners.first().clone());
    $banner.prepend(banners.last().clone());

    const totalBanners = bannerCount + 2; // 복제된 배너 포함
    const bannerWidth = 100; // 각 배너의 너비(%)
    $banner.css('transform', `translateX(-${bannerWidth}%)`); // 첫 번째 배너로 이동

    function showBanner(index) {
        $banner.css('transition', 'transform 0.5s ease-in-out');
        const translateX = -bannerWidth * (index + 1); // 복제 슬라이드 고려
        $banner.css('transform', `translateX(${translateX}%)`);

        $pages.removeClass('banner-page-now');
        $pages.eq(index).addClass('banner-page-now');
    }

    function nextBanner() {
        currentIndex++;
        showBanner(currentIndex);

        // 3. 마지막 슬라이드에서 첫 번째로 순간 이동
        if (currentIndex === bannerCount) {
            setTimeout(() => {
                $banner.css('transition', 'none'); // 애니메이션 제거
                $banner.css('transform', `translateX(-${bannerWidth}%)`); // 첫 번째 슬라이드 위치
                currentIndex = 0;

                $pages.removeClass('banner-page-now');
                $pages.eq(currentIndex).addClass('banner-page-now');
            }, 500); // CSS 전환 시간과 일치
        }
    }

    // 4. 3초마다 배너 이동
    let bannerInterval = setInterval(nextBanner, 3000);

    // 페이지 클릭 시 이동
    $pages.on('click', function () {
        clearInterval(bannerInterval);
        currentIndex = $(this).index();
        showBanner(currentIndex);
        bannerInterval = setInterval(nextBanner, 3000);
    });
});

function viewPage(pageNum) {

    var date = $('#matching-date-select').val();

    $.ajax({
        url: '/page/matching/replace/matchingList?page=' + (pageNum-1),
        type: 'POST',
        data: {
            date: date,
            searchWord: ""
        },
        success: function (fragment) {
            $('#matching-list').replaceWith(fragment);
            window.scrollTo(0, 0);
        },
        error: function (xhr, status, error) {
            commonErrorCallBack(xhr, status, error);
        }
    })
}

function goPostDetail(postId) {
    location.href = "/page/post/detail/" + postId;
}