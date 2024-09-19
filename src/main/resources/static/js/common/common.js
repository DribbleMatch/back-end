// REST API JWT 처리

var originalRequestSettings = null;

$.ajaxSetup({
    beforeSend: function(xhr, settings) {
        // 각 요청의 설정 정보를 저장해둠
        originalRequestSettings = settings;
    }
})

function commonErrorCallBack(xhr, status, error) {

    // JSON 응답을 파싱
    var response = xhr.responseJSON;

    // 특정 필드(status)가 "success"일 때만 처리
    if (response && response.code === '0201') {
        alert("재로그인이 필요합니다.");
        location.href = "/login/page"
    } else if (response && response.code === '0202') {
        // 기존 요청 재시도
        if (originalRequestSettings) {
            $.ajax(originalRequestSettings);
        }
    } else {
        alert("서버 오류. 고객센터에 문의하세요.");
    }
}

function activeMenu(menuId) {
    $('.menu-items').removeClass('active');

    $('#' + menuId).addClass('active');
}

function goToPage(url) {
    location.href = url;
}

function activeFlowStep(step) {

    var steps = $('.step');
    steps.removeClass('step-active');
    steps.removeClass('step-now');

    if (step === 1) {
        $('#step-agree').addClass('step-now');
    } else if (step === 2) {
        $('#step-agree').addClass('step-active');
        $('#step-user-info').addClass('step-now');
    } else if (step === 3) {
        $('#step-agree').addClass('step-active');
        $('#step-user-info').addClass('step-active');
        $('#step-player-info').addClass('step-now');
    } else {
        $('#step-agree').addClass('step-active');
        $('#step-user-info').addClass('step-active');
        $('#step-player-info').addClass('step-active');
        $('#step-complete').addClass('step-now');
    }
}