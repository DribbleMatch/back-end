function click_stadium() {
    $('#region-info').css('display', 'none');
    $('#stadium-info').show();
}

function click_region() {
    $('#region-info').show();
    $('#stadium-info').css('display', 'none');
}

function createMatching() {
    var name = $('#name').val();
    var play_people = $('input[name="member-num"]:checked').val();
    var max_people = $('#max-num').val();
    var date = $('#date').val();
    var time = $('#time').val();
    var hour = $('#hour').val();
    var stadium_region_select = $('input[name="stadium_region_select"]:checked').val();
    var stadium_string = $('#stadium').val();
    var siDo_string = $('#siDo').val();
    var siGunGu_string = $('#siGunGu').val();

    if (!name) {
        alert("경기명을 입력해주세요.");
        return;
    }

    if (max_people < play_people) {
        alert("경기 참여 최대 인원이 경기 인원 보다 적을 수 없습니다.");
        return;
    }

    if (!date) {
        alert("경기 날짜를 입력해주세요.");
        return;
    }

    if (!time) {
        alert("경기 시간을 입력해주세요.");
        return;
    }

    if (hour < 1) {
        alert("경기 진행 시간은 최소 한시간입니다.");
        return;
    }

    if (stadium_region_select === "stadium") {
        siDo_string = "";
        siGunGu_string = "";
        if (!stadium_string) {
            alert("경기장을 선택해주세요.");
            return;
        }
    }

    if (stadium_region_select === "region") {
        stadium_string = "";
        if (!siDo_string || !siGunGu_string) {
            alert("지역을 선택해주세요.");
            return;
        }
    }

    var formData = {
        "name": name,
        "playPeople": play_people,
        "maxPeople": max_people,
        "startAt": date + "T" + time,
        "hour": hour,
        "stadiumString": stadium_string,
        "regionString": siDo_string + " " + siGunGu_string
    }

    $.ajax({
        url: '/matching/createMatching',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(formData),
        success: function (data) {
            alert('경기 생성 완료');

            // 어디로 보낼지 다시 정하기
            location.reload();
        },
        error: function () {
            alert('경기 생성에 실패하였습니다. 잠시 후에 다시 시도하세요.');
        }
    })
}