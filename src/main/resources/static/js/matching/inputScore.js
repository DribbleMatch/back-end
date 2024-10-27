$(document).ready(function() {

    alert("생성 후 진행이 완료된 경기의 점수를 입력하여야 경기 이용이 가능합니다.");
});

function inputScore(button) {

    const matchingDiv = $(button).closest('.matching');

    const matchingId = matchingDiv.find('#matching-id').val();
    const upTeamScore = matchingDiv.find('.team-info-div .team-score').first().val();
    const downTeamScore = matchingDiv.find('.team-info-div .team-score').last().val();

    if (!upTeamScore || !downTeamScore) {
        alert("점수를 입력해주세요");
        return;
    }

    $.ajax({
        url: '/api/matching/inputScore',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            id: matchingId,
            upTeamScore: upTeamScore,
            downTeamScore: downTeamScore
        }),
        success: function(response) {
            if (response.data) {
                location.href = "/page";
            } else {
                location.reload();
            }
        },
        error: function(xhr, status, error) {
            commonErrorCallBack(xhr, status, error);
        }
    })
}

function notMatching(button) {

    const matchingDiv = $(button).closest('.matching');
    const matchingId = matchingDiv.find('#matching-id').val();

    if (!confirm("해당 매칭이 실제로 진행되지 않았습니까? 경기가 취소처리 됩니다.")) {
        return;
    }

    $.ajax({
        url: '/api/matching/notFinishMatching/' + matchingId,
        type: 'GET',
        success: function(response) {
            if (response.data) {
                location.href = "/page";
            } else {
                location.reload();
            }
        },
        error: function(xhr, status, error) {
            commonErrorCallBack(xhr, status, error);
        }
    })
}