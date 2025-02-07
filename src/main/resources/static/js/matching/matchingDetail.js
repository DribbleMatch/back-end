$(document).ready(function () {
    activeMenu('match-menu');

    /** scroll **/
    let lastScrollTop = 0;
    let $topFollow = $("#topFollow");

    $(window).on("scroll touchmove touchend", function () {
            setTimeout(function () {
                let currentScroll = document.documentElement.scrollTop || document.body.scrollTop;

                if (currentScroll > 110) {
                    $topFollow.css({top: "0", opacity: "1"});
                } else if (currentScroll < 110 || currentScroll <= 0) {
                    $topFollow.css({top: "-100px", opacity: "0"});
                }

            }, 100);

            setTimeout(function () {
                let currentScroll = document.documentElement.scrollTop || document.body.scrollTop;

                if (currentScroll <= 110) {
                    $topFollow.css({top: "-100px", opacity: "0"});
                }

            }, 1000);
    });
})

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

    var teamName = $('#team-select').val();
    var matchingId = $('#matching-id').val();

    if (!confirm("해당 경기에 참여하시겠습니까?")) {
        return;
    }

    if (!teamName) {
        alert("경기가 참가할 팀을 선택해주세요");
        return;
    }

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