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