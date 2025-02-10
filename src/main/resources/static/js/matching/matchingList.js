$(document).ready(function () {
    activeMenu('match-menu');

    $(".plus-icon").click(function (event) {
        $(".plus-menu").toggleClass("hide-toggle");
        event.stopPropagation();
    });

    $(document).click(function () {
        $(".plus-menu").addClass("hide-toggle");
    });

    $(".plus-menu").click(function (event) {
        event.stopPropagation();
    });
})

function viewPage(pageNum) {

    const $dateDivList = $(".date-div-list");
    const $middleDateDiv = $dateDivList.children().eq(2);
    const date = $middleDateDiv.attr('date');

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

    var date = $(".date-div-list").children().eq(2).attr('date');
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