// REST API JWT 처리

var originalRequestSettings = null;

$.ajaxSetup({
    beforeSend: function(xhr, settings) {
        originalRequestSettings = settings;
    }
})

function commonErrorMessageCallBack(response) {

    if (!response) {
        alert("서버 오류. 고객센터에 문의하세요.");
    } else if (response.code === '9301') {
        alert("지역을 찾기 못했습니다. 고객센터에 문의해주세요");
    } else if (response.code === '1300' || response.code === '1103') {
        alert("이메일 혹은 비밀번호를 다시 확인하세요.");
    } else {
        if (response.code) {
            alert(response.message);
        } else {
            alert("서버 오류. 고객센터에 문의하세요."); // 서버에서 처리하지 못한 오류
        }
    }
}

function commonErrorCallBack(xhr, status, error) {

    var response = xhr.responseJSON;

    if (response && response.code === '0201') {
        alert("재로그인이 필요합니다.");
        location.href = "/page/login"
    } else if (response && response.code === '0202') {
        if (originalRequestSettings) {
            $.ajax(originalRequestSettings);
        }
    } else {
        commonErrorMessageCallBack(response);
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