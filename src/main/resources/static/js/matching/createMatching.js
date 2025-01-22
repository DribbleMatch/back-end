$(document).ready(function () {
    activeMenu('match-menu');

    $(document).click(function(event) {
        if (!$(event.target).closest('#time-picker').length && !$(event.target).is('#time')) {
            $('#time-picker').hide();
        }
    });

    $('input[name="game-kind"]').on('change', function() {
        var teamSelect = $('#team-select');

        if (this.value === 'TEAM') {
            teamSelect.prop('disabled', false);
        } else {
            teamSelect.val('');
            teamSelect.prop('disabled', true);
        }
    });

    $('input[name="stadium_region_select"]').on('change', function() {
        if (this.id === 'stadium') {
            $('#region-info').hide();
            $('#region-info2').hide();
            $('#stadium-info').show();
            $('#stadium-info2').show();
        } else if (this.id === 'region') {
            $('#region-info').css('display', 'flex');
            $('#region-info2').css('display', 'flex');
            $('#stadium-info').hide()
            $('#stadium-info2').hide();
        }
    });
})

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
    var playNum = $('input[name="play-num"]:checked').val();
    var maxNum = $('#max-num').val();
    var date = $('#date').val();
    var time = $('#time').val();
    var hour = $('#hour').val();
    var gameKind = $('input[name="game-kind"]:checked').val();
    var teamSelect = $('#team-select').val();
    var isOnlyWomen;
    var stadiumRegionSelect = $('input[name="stadium_region_select"]:checked').val();
    var stadiumLoadAddress = $('#stadium-load-address').val();
    var stadiumJibunAddress = $('#stadium-jibun-address').val();
    var detailAddress = $('#stadium-detail-address').val();
    var siDoString = $('#siDo').val();
    var siGunGuString = $('#siGunGu').val();

    if (!name) {
        alert("경기명을 입력해주세요");
        return;
    }
    if (!playNum) {
        alert("경기 참여 인원을 입력해주세요");
        return;
    }
    if (!maxNum) {
        alert("최대 모집 인원을 입력해수제요");
    }
    if (parseInt(maxNum, 10) < parseInt(playNum, 10)) {
        alert("경기 참여 최대 인원이 경기 인원 보다 적을 수 없습니다");
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

    var currentDate = new Date();
    var targetDateTime = new Date(date + " " + time);
    currentDate.setHours(currentDate.getHours() + 2);
    if (currentDate >= targetDateTime) {
        alert("두 시간 이후의 경기만 생성 가능합니다");
        return
    }

    if (gameKind === 'TEAM' && !teamSelect) {
        alert("팀을 선택해주세요.");
        return;
    }
    if($('#is-only-women').is(':checked')) {
        isOnlyWomen = "ONLY_WOMEN"
    } else {
        isOnlyWomen = "NOT_ONLY_WOMEN"
    }

    var regionString = "";
    if (stadiumRegionSelect === "stadium") {
        if (!stadiumLoadAddress) {
            alert("경기장 주소를 입력해주세요.");
            return;
        }
        if (!detailAddress) {
            alert("상세 주소를 입력해주세요.");
            return;
        }
        if (!stadiumJibunAddress) {
            alert("잘못된 접근. 고객센터에 문의하세요.");
            return;
        }
    }

    if (stadiumRegionSelect === "region") {
        stadiumLoadAddress = "";
        stadiumJibunAddress = "";
        detailAddress = "";
        if (!siDoString || !siGunGuString) {
            alert("지역을 선택해주세요.");
            return;
        }
        regionString = siDoString + " " + siGunGuString;
    }

    var formData = {
        "name": name,
        "playPeople": playNum,
        "maxPeople": maxNum,
        "startAt": date + "T" + time,
        "hour": hour,
        "gameKind": gameKind,
        "teamName": teamSelect,
        "isOnlyWomen": isOnlyWomen,
        "stadiumLoadAddress": stadiumLoadAddress,
        "stadiumJibunAddress": stadiumJibunAddress,
        "detailAddress": detailAddress,
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

function showTimePicker() {
    $('#time-picker').css('display', 'flex');
}

function selectPeriod(button) {

    $('.time-picker-list:first-child .time-picker-element').removeClass('select');
    $(button).addClass('select');

    const timeInput = $('#time');
    const currentTime = timeInput.val().split(':');
    let hour = parseInt(currentTime[0]);

    const period = $('.time-picker-list:first-child .time-picker-element.select').text();

    if (period === '오후' && hour < 12) {
        hour += 12;
    } else if (period === '오전' && hour >= 12) {
        hour -= 12;
    }

    const minute = currentTime[1];
    timeInput.val(hour.toString().padStart(2, '0') + ':' + minute);
}

function selectHour(button) {

    const period = $('.time-picker-list:first-child .time-picker-element.select').text();

    $('.time-picker-list:nth-child(2) .time-picker-element').removeClass('select');
    $(button).addClass('select');

    const timeInput = $('#time');
    const currentTime = timeInput.val().split(':');
    const minute = currentTime[1];
    let hour = parseInt($(button).text());

    if (period === '오후' && hour < 12) {
        hour += 12;
    } else if (period === '오전' && hour === 12) {
        hour = 0;
    }

    timeInput.val(hour.toString().padStart(2, '0') + ':' + minute);
}

function selectMinute(button) {
    $('.time-picker-list:nth-child(3) .time-picker-element').removeClass('select');
    $(button).addClass('select');

    const timeInput = $('#time');
    const currentTime = timeInput.val().split(':');
    const hour = currentTime[0];
    const minute = $(button).text();

    timeInput.val(hour + ':' + minute);
}