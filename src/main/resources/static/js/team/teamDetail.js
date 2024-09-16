function openRequestListPop() {

    var id = $('#team-id').val();

    $.ajax({
        url: '/teamApplication/page/' + id,
        type: 'GET',
        success: function (fragment) {
            $('#request-list-popup').replaceWith(fragment);
            document.getElementById("request-list-popup").style.display = "flex";
        },
        error: function (xhr, status, error) {
            commonErrorCallBack(xhr, status, error);
        }
    })
}

function closeRequestListPop() {
    document.getElementById("request-list-popup").style.display = "none";
}

function toggleIntroduce(element) {
    const $item = $(element).closest('.request-item');
    const $introduce = $item.find('.introduce');

    // 토글 상태에 따라 introduce의 표시 여부를 설정
    if ($introduce.is(':visible')) {
        $introduce.slideUp(300, function() {
            $item.css('height', 'auto'); // 내용이 숨겨졌으므로 높이를 자동으로 조정
            $introduce.css('margin-bottom', '0');
        });
    } else {
        $introduce.slideDown(300);
        $item.css('height', 'auto'); // 현재 높이를 설정하여 애니메이션 효과를 줌
        $introduce.css('margin-bottom', '1rem');
    }

    // 화살표 모양 반전 상태 토글
    $(element).toggleClass('open');
}

function approvalTeamApplication() {

    var id = $('#team-application-id').val();

    if (!confirm("수락하시겠습니까?")) {
        return;
    }

    $.ajax({
        url: '/teamApplication/rest/approval/' + id,
        type: 'GET',
        success: function (response) {
            alert("팀원이 등록되었습니다.");
            openRequestListPop();
        },
        error: function (xhr, status, error) {
            var response = xhr.responseJSON;

            if (response && (response.code === '8300' || response.code === '3100' || response.code === '1301' || response.code === '2300')) {
                alert(response.message);
            } else {
                commonErrorCallBack(xhr, status, error);
            }
        }
    })
}

function refuseTeamApplication() {

    var id = $('#team-application-id').val();

    if (!confirm("거절하시겠습니까?")) {
        return;
    }

    $.ajax({
        url: '/teamApplication/rest/refuse/' + id,
        type: 'GET',
        success: function (response) {
            alert("가입 요청이 거절되었습니다.");
            openRequestListPop();
        },
        error: function (xhr, status, error) {
            var response = xhr.responseJSON;

            if (response && (response.code === '8300' || response.code === '3100' || response.code === '1301' || response.code === '2300')) {
                alert(response.message);
            } else {
                commonErrorCallBack(xhr, status, error);
            }
        }
    })
}

function openRequestPop() {
    document.getElementById("request-popup").style.display = "flex";
}

function closeRequestPop() {
    document.getElementById("request-popup").style.display = "none";
}

function requestJoin() {

    var id = $('#team-id').val();
    var introduce = $('#introduce').val();

    $.ajax({
        url: '/teamApplication/rest/requestJoin',
        type: 'POST',
        data: {
            id: id,
            introduce: introduce
        },
        success: function (response) {
            alert("요청이 완료되었습니다.");
            closeRequestPop();
        },
        error: function (xhr, status, error) {
            var response = xhr.responseJSON;

            if (response && (response.code === '1301' || response.code === '2300' || response.code === '3100' || response.code === '8100' || response.code === '8101')) {
                alert(response.message);
            } else {
                commonErrorCallBack(xhr, status, error);
            }
        }
    })
}

function goCreateRecruitmentPage() {
    var teamId = $('#team-id').val();
    location.href = "/recruitment/page/create/" + teamId;
}