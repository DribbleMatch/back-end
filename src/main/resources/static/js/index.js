function viewPage(pageNum) {

    var date = $('#matching-date-select').val();

    $.ajax({
        url: '/page/matching/replace/matchingList?page=' + (pageNum-1),
        type: 'POST',
        data: {
            date: date,
            searchWord: ""
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
