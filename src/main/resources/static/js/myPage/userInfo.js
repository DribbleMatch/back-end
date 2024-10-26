function goToReservedMatchingList(gameKind) {
    location.href = "/page/matching/reservedMatchingList/" + gameKind;
}

function goToEndedMatchingList(gameKind) {
    location.href = "/page/matching/endedMatchingList/" + gameKind;
}

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