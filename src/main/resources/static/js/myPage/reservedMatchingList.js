function viewPage(pageNum) {
    var gameKind = $('#game-kind').val();
    $.ajax({
        url: '/page/matching/replace/reservedMatchingList/' + gameKind + '?page=' + (pageNum-1),
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

function showMemberNum(upDown) {
    if (upDown === 'up') {
        $('#up-member-num-bubble').show();
    } else {
        $('#down-member-num-bubble').show();
    }
}

function hideMemberNum(upDown) {
    if (upDown === 'up') {
        $('#up-member-num-bubble').hide();
    } else {
        $('#down-member-num-bubble').hide();
    }
}

function toggleGameKind(gameKind) {

    $.ajax({
        url: '/page/matching/replace/reservedMatchingList/' + gameKind,
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