function viewTeamDetail(id) {
    location.href = "/team/page/teamDetail/" + id;
}

function joinTeam(id) {
    $.ajax({
        url: "/team/rest/joinTeam/" + id,
        method: "GET",
        success: function () {
            alert("팀원이 되었습니다.");
        },
        error: function (xhr, status, error) {
            var response = xhr.responseJSON;
            if (response.code === '3100') {
                alert("이미 해당 팀의 팀원입니다.");
            } else {
                commonErrorCallBack(xhr, status, error);
            }
        }
    })
}