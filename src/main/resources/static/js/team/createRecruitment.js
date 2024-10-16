$(document).ready(function () {
    activeMenu('team-menu');
})

function createRecruitment() {
    var teamId = $('#team-id').val();
    var title = $('#title').val();
    var content = $('#content').val();
    var positionString = $('input[name="positionString"]:checked').map(function() {
        return $(this).val();
    }).get().join(',');
    var date = $('#date').val();

    if (!title) {
        alert("모집글의 제목을 입력해주세요");
        return;
    }

    if (!content) {
        alert("상세 요강을 작성해주세요");
        return;
    }

    if (!positionString) {
        alert("모집 포지션을 1개 이상 선택해주세요");
        return;
    }

    if (!date) {
        alert("모집 종료 날짜를 선택해주세요");
    }

    var formData = {
        "teamId": teamId,
        "title": title,
        "content": content,
        "positionString": positionString,
        "expireDate": date
    }

    $.ajax({
        url: '/api/recruitment/createRecruitment',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(formData),
        success: function (data) {
            alert('팀원 모집글 생성 완료');

            location.href = "/page/recruitment";
        },
        error: function (xhr, status, error) {
            commonErrorCallBack(xhr, status, error);
        }
    })
}