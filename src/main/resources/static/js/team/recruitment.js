$(document).ready(function () {
    activeMenu('team-menu');
})

function viewPage(pageNum) {

    var searchWord = $('#search-word').val();

    $.ajax({
        url: '/page/recruitment/replace/recruitmentList?page=' + (pageNum-1),
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
    location.href = "/page/team/detail/" + id;
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
        url: '/api/teamApplication/requestJoin',
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
                commonErrorCallBack(xhr, status, error);
        }
    })
}

function searchRecruitment() {

    var searchWord = $('#search-word').val();

    $.ajax({
        url: '/page/recruitment/replace/recruitmentList',
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