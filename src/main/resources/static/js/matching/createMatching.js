$(document).ready(function () {
    activeMenu('match-menu');
})

function click_stadium_address() {
    $('#region-info').css('display', 'none');
    $('#stadium-info').show();
}

function click_region() {
    $('#region-info').show();
    $('#stadium-info').css('display', 'none');
}

function selectAddress() {

    new daum.Postcode({
        oncomplete: function (data) {

            $('#stadium-load-address').val(data.address);

            if (!data.jibunAddress) {
                $('#stadium-jibun-address').val(data.autoJibunAddress);
            } else {
                $('#stadium-jibun-address').val(data.jibunAddress);
            }
        }
    }).open();
}

function createMatching() {
    var name = $('#name').val();
    var play_people = $('input[name="member-num"]:checked').val();
    var max_people = $('#max-num').val();
    var date = $('#date').val();
    var time = $('#time').val();
    var hour = $('#hour').val();
    var gameKind = $('input[name="game-kind"]:checked').val();
    var teamSelect = $('#team-select').val();
    var is_only_women;
    var stadium_region_select = $('input[name="stadium_region_select"]:checked').val();
    var stadium_load_address = $('#stadium-load-address').val();
    var stadium_jibun_address = $('#stadium-jibun-address').val();
    var detail_address = $('#stadium-detail-address').val();
    var siDo_string = $('#siDo').val();
    var siGunGu_string = $('#siGunGu').val();

    if (!name) {
        alert("경기명을 입력해주세요.");
        return;
    }

    if (parseInt(max_people, 10) < parseInt(play_people, 10)) {
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

    if (gameKind === 'TEAM' && !teamSelect) {
        alert("팀을 선택해주세요.");
        return;
    }

    if($('#is-only-women').is(':checked')) {
        is_only_women = "ONLY_WOMEN"
    } else {
        is_only_women = "NOT_ONLY_WOMEN"
    }

    var regionString = "";

    if (stadium_region_select === "stadium") {
        if (!stadium_load_address) {
            alert("경기장 주소를 입력해주세요.");
            return;
        }
        if (!detail_address) {
            alert("상세 주소를 입력해주세요.");
            return;
        }

        if (!stadium_jibun_address) {
            alert("잘못된 접근. 고객센터에 문의하세요.");
            return;
        }
    }

    if (stadium_region_select === "region") {
        stadium_load_address = "";
        stadium_jibun_address = "";
        detail_address = "";
        if (!siDo_string || !siGunGu_string) {
            alert("지역을 선택해주세요.");
            return;
        }
        regionString = siDo_string + " " + siGunGu_string;
    }

    var formData = {
        "name": name,
        "playPeople": play_people,
        "maxPeople": max_people,
        "startAt": date + "T" + time,
        "hour": hour,
        "gameKind": gameKind,
        "teamName": teamSelect,
        "isOnlyWomen": is_only_women,
        "stadiumLoadAddress": stadium_load_address,
        "stadiumJibunAddress": stadium_jibun_address,
        "detailAddress": detail_address,
        "regionString": regionString
    }

    $.ajax({
        url: '/api/matching/create',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(formData),
        success: function (data) {
            alert('경기 생성 완료');
            location.href = "/page/matching/matchingList";
        },
        error: function (xhr, status, error) {
            commonErrorCallBack(xhr, status, error);
        }
    })
}

function toggleTeamSelect(radioButton) {
    var teamSelect = $('#team-select');

    if (radioButton.value === 'TEAM') {
        teamSelect.prop('disabled', false);
    } else {
        teamSelect.val('');
        teamSelect.prop('disabled', true);
    }
}