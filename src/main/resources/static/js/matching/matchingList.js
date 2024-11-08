$(document).ready(function () {
    activeMenu('match-menu');
})

function viewPage(pageNum) {

    var date = $('#matching-date-select').val();
    var searchWord = $('#search-word').val();

    $.ajax({
        url: '/page/matching/replace/matchingList?page=' + (pageNum-1),
        type: 'POST',
        data: {
            date: date,
            searchWord: searchWord
        },
        success: function (fragment) {
            $('#matching-list').replaceWith(fragment);
            window.scrollTo(0, 0);
        },
        error: function (xhr, status, error) {
            commonErrorCallBack(xhr, status, error);
        }
    })
}

function searchMatching() {

    var date = $('#matching-date-select').val();
    var searchWord = $('#search-word').val();

    $.ajax({
        url: '/page/matching/replace/matchingList',
        type: 'POST',
        data: {
            date: date,
            searchWord: searchWord
        },
        success: function (fragment) {
            $('#matching-list').replaceWith(fragment);
        },
        error: function (xhr, status, error) {
            commonErrorCallBack(xhr, status, error);
        }
    })
}

function previousDate() {
    var dateInput = $('#matching-date-select');
    var currentDate = new Date(dateInput.val());

    if (!isNaN(currentDate.getTime())) {
        currentDate.setDate(currentDate.getDate() - 1);
        var previousDate = currentDate.toISOString().split('T')[0];
        dateInput.val(previousDate);
    } else {
        console.error("유효한 날짜가 아닙니다.");
    }

    replaceListByDate(previousDate);
}

function nextDate() {
    var dateInput = $('#matching-date-select');
    var currentDate = new Date(dateInput.val());

    if (!isNaN(currentDate.getTime())) {
        currentDate.setDate(currentDate.getDate() + 1);
        var nextDate = currentDate.toISOString().split('T')[0];
        dateInput.val(nextDate);
    } else {
        console.error("유효한 날짜가 아닙니다.");
    }

    replaceListByDate(nextDate);
}

function getMatchingList() {
    var dateInput = $('#matching-date-select').val();
    replaceListByDate(dateInput);
}

function replaceListByDate(date) {
    var searchWord = $('#search-word').val();

    $.ajax({
        url: '/page/matching/replace/matchingList',
        type: 'POST',
        data: {
          date: date,
          searchWord: searchWord
        },
        success: function (fragment) {
            $('#matching-list').replaceWith(fragment);
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

function goMatchingDetail(matchingId) {
    location.href = "/page/matching/matchingDetail/" + matchingId;
}