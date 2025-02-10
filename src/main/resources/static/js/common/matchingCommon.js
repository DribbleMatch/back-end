const weekdays = ["일", "월", "화", "수", "목", "금", "토"];
let isAnimating = false;

function slideLeft(moveNum) {

    if (isAnimating) {
        return;
    }

    const $dateDivList = $(".date-div-list");
    const $middleDateDiv = $dateDivList.children(".date-div").eq(3);
    var middleDate = $middleDateDiv.attr('date');

    var newDate = new Date(middleDate);
    newDate.setDate(newDate.getDate() + 2);
    const day = newDate.getDate();
    const weekday = weekdays[newDate.getDay()];

    const formattedDate = `${day} / (${weekday})`;

    const newDateDiv = $("<div>")
        .addClass("date-div")
        .attr("date", newDate.toISOString().split('T')[0])
        .text(formattedDate);

    const $firstChild = $dateDivList.children(".date-div").first();

    const newDate2 = new Date(newDate);
    newDate2.setDate(newDate2.getDate() + 1);

    $firstChild.animate(
        {marginLeft: `-=${18 * moveNum}rem`},
        500,
        function () {
            $dateDivList.append(newDateDiv);
            $firstChild.remove();
            $firstChild.css("margin-left", "0");
            isAnimating = false;

            if (moveNum === 2) {
                var $secondChild = $dateDivList.children(".date-div").first();
                $secondChild.remove();

                const day2 = newDate2.getDate();
                const weekday2 = weekdays[newDate2.getDay()];

                const formattedDate2 = `${day2} / (${weekday2})`;

                const newDateDiv2 = $("<div>")
                    .addClass("date-div")
                    .attr("date", newDate2.toISOString().split('T')[0])
                    .text(formattedDate2);

                $dateDivList.append(newDateDiv2);

                newDate2.setDate(newDate2.getDate() - 2)
                middleDate = newDate2.toISOString().split('T')[0];
            }

            replaceListByDate(middleDate);
        }
    );
}

function slideRight(moveNum) {

    if (isAnimating) {
        return;
    }

    const $dateDivList = $(".date-div-list");

    const $beforeDateDiv = $dateDivList.children(".date-div").eq(2);

    if (new Date(Date.now() - offset).toISOString().split('T')[0] === new Date($beforeDateDiv.attr('date')).toISOString().split('T')[0]) {
        return;
    } else if (new Date(Date.now() - offset + (24 * 60 * 60 * 1000)).toISOString().split('T')[0] === new Date($beforeDateDiv.attr('date')).toISOString().split('T')[0] && moveNum === 2) {
        return;
    }

    const $middleDateDiv = $dateDivList.children(".date-div").eq(1);
    var middleDate = $middleDateDiv.attr('date');

    const newDate = new Date(middleDate);
    newDate.setDate(newDate.getDate() - 2);
    const day = newDate.getDate();
    const weekday = weekdays[newDate.getDay()];

    var formattedDate = `${day} / (${weekday})`;

    if (newDate.toISOString().split('T')[0] < new Date(new Date(Date.now() - offset)).toISOString().split('T')[0]) {
        formattedDate = '';
    }

    const newDateDiv = $("<div>")
        .addClass("date-div")
        .attr("date", newDate.toISOString().split('T')[0])
        .text(formattedDate);

    const $lastChild = $dateDivList.children(".date-div").eq(4);

    const newDate2 = new Date(newDate);
    newDate2.setDate(newDate2.getDate() - 1);

    $lastChild.animate(
        {marginRight: `-=${18 * moveNum}rem`},
        500,
        function () {
            $dateDivList.prepend(newDateDiv);
            $lastChild.remove();
            $lastChild.css("margin-left", "0");
            isAnimating = false;

            if (moveNum === 2) {
                var $fourthChild = $dateDivList.children(".date-div").last();
                $fourthChild.remove();

                const day2 = newDate2.getDate();
                const weekday2 = weekdays[newDate2.getDay()];

                var formattedDate2 = `${day2} / (${weekday2})`;

                if (newDate2.toISOString().split('T')[0] < new Date(new Date(Date.now() - offset)).toISOString().split('T')[0]) {
                    formattedDate2 = '';
                }

                const newDateDiv2 = $("<div>")
                    .addClass("date-div")
                    .attr("date", newDate2.toISOString().split('T')[0])
                    .text(formattedDate2);

                $dateDivList.prepend(newDateDiv2);

                newDate2.setDate(newDate2.getDate() + 2)
                middleDate = newDate2.toISOString().split('T')[0];
            }

            replaceListByDate(middleDate);
        }
    );
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