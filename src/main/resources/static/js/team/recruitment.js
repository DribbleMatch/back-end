$(document).ready(function () {
    activeMenu('team-menu');
})

function viewPage(pageNum) {

    var searchWord = $('#search-word').val();

    $.ajax({
        url: '/recruitment/page?page=' + (pageNum-1),
        type: 'POST',
        data: { searchWord: searchWord },
        success: function (fragment) {
            $('#recruitment-list').replaceWith(fragment);
        },
        error: function (xhr, status, error) {
            commonErrorCallBack(xhr, status, error);
        }
    })
}

function openContentPopup(button) {
    const $card = $(button).closest('.recruitment-card');

    const $title = $card.find('.recruitment-info').find('#recruitment-title').text();
    const $content = $card.find('#recruitment-content').val();

    $("#recruitment-title-h2").text($title);
    $("#recruitment-content-textarea").val($content);

    document.getElementById("content-popup").style.display = "flex";
}

function closeContentPopup() {
    document.getElementById("content-popup").style.display = "none";
}

function viewTeamDetail(id) {
    location.href = "/team/page/teamDetail/" + id;
}

function openRequestPop(button) {
    const $card = $(button).closest('.recruitment-card');

    const $teamId = $card.find('#team-id').val();
    $("#team-id-popup").val($teamId);

    document.getElementById("request-popup").style.display = "flex";
}

function closeRequestPop() {
    document.getElementById("request-popup").style.display = "none";
}

function requestJoin() {

    var id = $('#team-id-popup').val();
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

function searchRecruitment() {

    var searchWord = $('#search-word').val();

    $.ajax({
        url: '/recruitment/page',
        type: 'POST',
        data: { searchWord: searchWord },
        success: function (fragment) {
            $('#recruitment-list').replaceWith(fragment);
        },
        error: function (xhr, status, error) {
            commonErrorCallBack(xhr, status, error);
        }
    })
}

//todo: 로그인 중 로그인 페이지 접근 시 메인 페이지로?? 설계 후 구현 필요