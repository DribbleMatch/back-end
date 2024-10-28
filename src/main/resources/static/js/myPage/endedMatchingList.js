function viewPage(pageNum) {
    var gameKind = $('#game-kind').val();
    $.ajax({
        url: '/page/matching/replace/endedMatchingList/' + gameKind + '?page=' + (pageNum-1),
        type: 'GET',
        success: function (fragment) {
            $('#matching-list').replaceWith(fragment);
            window.scrollTo(0, 0);
        },
        error: function (xhr, status, error) {
            commonErrorCallBack(xhr, status, error);
        }
    })
}

function toggleGameKind(gameKind) {

    $.ajax({
        url: '/page/matching/replace/endedMatchingList/' + gameKind,
        type: 'GET',
        success: function (fragment) {
            if (gameKind === "TEAM") {
                $('#team-match').addClass('selected');
                $('#personal-match').removeClass('selected');
            } else {
                $('#team-match').removeClass('selected');
                $('#personal-match').addClass('selected');
            }
            $('#matching-list').replaceWith(fragment);
        },
        error: function (xhr, status, error) {
            commonErrorCallBack(xhr, status, error);
        }
    })
}