$(document).ready(function () {
    activeMenu('match-menu');
})

function openPersonalMatchJoinPop() {
    document.getElementById("personal-match-join-pop").style.display = "flex";
}

function closePersonalMatchJoinPop() {
    document.getElementById("personal-match-join-pop").style.display = "none";
}

function openTeamMatchJoinPop() {
    document.getElementById("team-match-join-pop").style.display = "flex";
}

function closeTeamMatchJoinPop() {
    document.getElementById("team-match-join-pop").style.display = "none";
}

function joinPersonalMatch() {

    if (!confirm("해당 경기에 참여하시겠습니까?")) {
        return;
    }

    var team = $('input[name="team-select"]:checked').val();
    var matchingId = $('#matching-id').val();

    $.ajax({
        url: '/api/personalMatchJoin',
        type: 'POST',
        data: {
            team: team,
            matchingId: matchingId
        },
        success: function (fragment) {
            alert("경기 참가 완료");
            location.reload();
        },
        error: function (xhr, status, error) {
            commonErrorCallBack(xhr, status, error);
        }
    })
}

function joinTeamMatch() {

    if (!confirm("해당 경기에 참여하시겠습니까?")) {
        return;
    }

    var teamName = $('#team-select').val();
    var matchingId = $('#matching-id').val();

    $.ajax({
        url: '/api/teamMatchJoin',
        type: 'POST',
        data: {
            teamName: teamName,
            matchingId: matchingId
        },
        success: function (fragment) {
            alert("경기 참가 완료");
            location.reload();
        },
        error: function (xhr, status, error) {
            commonErrorCallBack(xhr, status, error);
        }
    })
}

function joinPersonalMatchTeam(team) {

    if (team === "UP_TEAM") {
        if (!confirm("해당 경기의 A팀으로 참여하시겠습니까?")) {
            return;
        }
    } else {
        if (!confirm("해당 경기의 B팀으로 참여하시겠습니까?")) {
            return;
        }
    }

    var matchingId = $('#matching-id').val();

    $.ajax({
        url: '/api/personalMatchJoin',
        type: 'POST',
        data: {
            team: team,
            matchingId: matchingId
        },
        success: function (fragment) {
            alert("경기 참가 완료");
            location.reload();
        },
        error: function (xhr, status, error) {
            commonErrorCallBack(xhr, status, error);
        }
    })
}