function showMyTeam() {
    $.ajax({
        url: '/team/page/myTeamListView',
        type: 'GET',
        success: function (fragment) {
            $('#team-list').replaceWith(fragment);
        },
        error: function (xhr, status, error) {
            commonErrorCallBack(xhr, status, error);
        }
    })
}

function goCreateTeamPage() {
    location.href = "/team/page/createTeam";
}

function viewTeamDetail(id) {
    location.href = "/team/page/teamDetail/" + id;
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
        url: '/team/page/teamListView',
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