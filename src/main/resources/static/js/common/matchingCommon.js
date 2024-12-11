const weekdays = ["일", "월", "화", "수", "목", "금", "토"];
let isAnimating = false;

function slideLeft() {

    if (isAnimating) {
        return;
    }

    const $dateDivList = $(".date-div-list");
    const $middleDateDiv = $dateDivList.children().eq(3);
    const middleDate = $middleDateDiv.attr('date');

    $.ajax({
        url: '/api/date/next',
        type: 'GET',
        data: {
            selectedDate: middleDate
        },
        beforeSend: function () {
            isAnimating = true;
        },
        success: function (response) {
            const newDate = response.data;

            const date = new Date(newDate);
            const day = date.getDate();
            const weekday = weekdays[date.getDay()];

            const formattedDate = `${day} / (${weekday})`;
            const newDateDiv = $("<div>")
                .addClass("date-div")
                .attr("date", newDate)
                .text(formattedDate);

            if (newDate === new Date().toISOString().split('T')[0]) {
                newDateDiv.addClass('date-selected-div')
            }

            const $firstChild = $dateDivList.children().first();

            $firstChild.animate(
                {marginLeft: "-=35%"},
                500,
                function () {
                    $dateDivList.append(newDateDiv);
                    $firstChild.remove();
                    $firstChild.css("margin-left", "0");
                    isAnimating = false;
                }
            );

            replaceListByDate(middleDate);
        },
        error: function (xhr, status, error) {
            commonErrorCallBack(xhr, status, error);
        }
    })
}

function slideRight() {

    if (isAnimating) {
        return;
    }

    const $dateDivList = $(".date-div-list");
    const $middleDateDiv = $dateDivList.children().eq(1);
    const middleDate = $middleDateDiv.attr('date');

    if (!middleDate) {
        return;
    }

    $.ajax({
        url: '/api/date/pre',
        type: 'GET',
        data: {
            selectedDate: middleDate
        },
        beforeSend: function () {
            isAnimating = true;
        },
        success: function (response) {

            const newDate = response.data;

            const date = new Date(newDate);
            const day = date.getDate();
            const weekday = weekdays[date.getDay()];

            const formattedDate = `${day} / (${weekday})`;
            const newDateDiv = $("<div>")
                .addClass('date-div')
                .attr('date', newDate)
                .text(newDate == null ? '' : formattedDate);

            if (newDate === new Date().toISOString().split('T')[0]) {
                newDateDiv.addClass('date-selected-div')
            }

            const $lastChild = $dateDivList.children().last();

            $lastChild.animate(
                { marginRight: "-=35%" },
                500,
                function () {
                    $dateDivList.prepend(newDateDiv);
                    $lastChild.remove();
                    isAnimating = false;
                }
            );

            replaceListByDate(middleDate);
        },
        error: function (xhr, status, error) {
            commonErrorCallBack(xhr, status, error);
        }
    })
}

function replaceListByDate(date) {

    let searchWord;

    if ($('#search-word').length > 0) {
        searchWord = $('#search-word').val();
    } else {
        searchWord = '';
    }

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
    location.href = "/page/matching/detail/" + matchingId;
}

function changeSelectDateStyle(clickedElement) {

    const date = $(clickedElement).attr('date');

    replaceListByDate(date);

    $(".select-date-div").removeClass("select-date-div");
    $(clickedElement).addClass("select-date-div");
}