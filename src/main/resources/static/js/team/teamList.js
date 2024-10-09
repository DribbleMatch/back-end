$(document).ready(function () {
    activeMenu('team-menu');
})

function viewPage(pageNum) {

    var searchWord = $('#search-word').val();

    $.ajax({
        url: '/page/team/replace/teamList?page=' + (pageNum-1),
        type: 'POST',
        data: { searchWord: searchWord },
        success: function (fragment) {
            $('#team-list').replaceWith(fragment);
        },
        error: function (xhr, status, error) {
            commonErrorCallBack(xhr, status, error);
        }
    })
}

function showMyTeam() {
    $.ajax({
        url: '/page/team/replace/myTeamListView',
        type: 'GET',
        success: function (fragment) {
            $('#team-list').replaceWith(fragment);
        },
        error: function (xhr, status, error) {
            commonErrorCallBack(xhr, status, error);
        }
    })
}

function goToRecruitment() {
    location.href = "/page/recruitment";
}

function goCreateTeamPage() {
    location.href = "/page/team/createTeam";
}

function viewTeamDetail(id) {
    location.href = "/page/team/teamDetail/" + id;
}

function requestJoin() {

    var id = $('#team-id').val();
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
            var response = xhr.responseJSON;

            if (response && (response.code === '8100' || response.code === '8101' || response.code === '3100' || response.code === '1301' || response.code === '2300')) {
                alert(response.message);
            } else {
                commonErrorCallBack(xhr, status, error);
            }
        }
    })
}

function openRequestPop(teamId) {
    $('#team-id').val(teamId);
    document.getElementById("request-popup").style.display = "flex";
}

function closeRequestPop() {
    document.getElementById("request-popup").style.display = "none";
}

function searchTeam() {
    var searchWord = $('#search-word').val();

    $.ajax({
        url: '/page/team/replace/teamList',
        type: 'POST',
        data: { searchWord: searchWord },
        success: function (fragment) {
            $('#team-list').replaceWith(fragment);
        },
        error: function (xhr, status, error) {
            commonErrorCallBack(xhr, status, error);
        }
    })
}