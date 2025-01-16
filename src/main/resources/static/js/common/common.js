/** REST API JWT 처리 **/
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

/** REST API JWT 처리 끝 **/

/** 메뉴 처리 **/
const mediaQuery = window.matchMedia("(max-width: 767px)");

function openMenu() {
    $.ajax({
        url: '/api/login/checkLogIn',
        type: 'GET',
        success: function (response) {
            if (response.data) {
                $('#user-nickname').text(response.data);
                $('#on-login').show();
                $('#not-login').removeClass("d-flex").hide();
            } else {
                $('#user-nickname').text("");
                $('#on-login').removeClass("d-flex").hide();
                $('#not-login').show();
            }

            $('#dark-area').removeClass("d-none");

            if (mediaQuery.matches) {
                $('#left-menu').animate({
                    top: '5%'
                }, 500, function() {
                    $(this).addClass('left-menu').css({
                        top: '',
                        left: ''
                    });
                });
            } else  {
                $('#left-menu').addClass('left-menu')
            }
        },
        error: function (xhr, status, error) {
            alert("서버 오류. 고객센터에 문의하세요.");
        }
    })
}

function closeMenu() {
    if (mediaQuery.matches) {
        $('#left-menu').animate({top: "100%"}, 300, function () {
            $(this).css({
                top: '',
                left: ''
            }).removeClass("left-menu").addClass("left-menu-hide");
        });
    } else  {
        $('#left-menu').removeClass("left-menu").addClass("left-menu-hide");
    }

    $('#dark-area').addClass("d-none");
}

$(document).ready(function() {
    if (mediaQuery.matches) {
        $('#left-menu').draggable({
            axis: "y",
            cancel: "#left-menu-content",
            stop: function(event, ui) {
                const screenHeight = $(window).height();
                const menuTop = ui.position.top;

                if (menuTop > (screenHeight * 1 / 3)) {
                    closeMenu();
                } else {
                    $(this).draggable('disable');
                    $(this).css("top", "5%");
                    $(this).draggable('enable');
                }
            }
        });

        $('#left-menu-content').on('touchstart touchmove touchend', function(e) {
            e.stopPropagation();
        });
    }
});

/** 메뉴 처리 끝 **/

/** 네비게이션바 처리 **/
function activeMenu(menuId) {
    $('.menu-items').removeClass('active');

    $('#' + menuId).addClass('active');
}

/** 회원 가입 스텝 UI 처리 **/
function activeFlowStep(stepNumber) {
    const steps = document.querySelectorAll('.step');
    const checkmarkSVG = `
        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="5" stroke-linecap="round" stroke-linejoin="round">
            <path d="M20 6L9 17l-5-5"></path>
        </svg>
    `;

    steps.forEach((step, index) => {
        const circle = step.querySelector('.flow-circle');
        const activeCircle = step.querySelector('.active-circle');

        activeCircle.style.display = 'none';

        if (index + 1 < stepNumber) {
            step.classList.add('completed');
            step.classList.remove('active');
            circle.innerHTML = checkmarkSVG;
        } else if (index + 1 === stepNumber) {
            step.classList.add('active');
            step.classList.remove('completed');
            circle.textContent = stepNumber;

            activeCircle.style.display = 'block';
        } else {
            step.classList.remove('completed', 'active');
            circle.textContent = index + 1;
        }
    });
}

/** 페이지 이동 **/
function goToPage(url) {
    location.href = url;
}

/** 로그아웃 **/
function logout() {
    $.ajax({
        url: '/api/login/logout',
        type: 'GET',
        success: function () {
            location.reload();
        },
        error: function (xhr, status, error) {
            commonErrorCallBack(xhr, status, error);
        }
    })
}