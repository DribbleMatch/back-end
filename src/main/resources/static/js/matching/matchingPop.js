function inputScore(button) {

    const cardDiv = $(button).closest('.card');

    const matchingId = cardDiv.find('#matching-id').val();
    const upTeamScore = cardDiv.find('input.basic-input').first().val();
    const downTeamScore = cardDiv.find('input.basic-input').last().val();

    if (!upTeamScore || !downTeamScore) {
        alert("점수를 입력해주세요");
        return;
    }

    if (!confirm("점수를 입력하시겠습니까?")) {
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
            location.reload();
        },
        error: function(xhr, status, error) {
            commonErrorCallBack(xhr, status, error);
        }
    })
}

function notMatching(button) {

    const cardDiv = $(button).closest('.card');
    const matchingId = cardDiv.find('#matching-id').val();

    if (!confirm("해당 매칭이 실제로 진행되지 않았습니까? 경기가 취소처리 됩니다.")) {
        return;
    }

    $.ajax({
        url: '/api/matching/notFinishMatching/' + matchingId,
        type: 'GET',
        success: function(response) {
            location.reload();
        },
        error: function(xhr, status, error) {
            commonErrorCallBack(xhr, status, error);
        }
    })
}