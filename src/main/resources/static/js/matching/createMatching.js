$(document).ready(function () {
    activeMenu('match-menu');
    setTime();

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

    $('.time-picker-element').on('click', function() {
        var parentList = $(this).closest('.time-picker-list');
        parentList.find('.time-picker-element').removeClass('select');
        $(this).addClass('select');
        setTime();
    });

    /** for mobile **/
    $("#date").on("change", function () {
        const selectedDate = new Date($(this).val());
        const tomorrow = new Date(Date.now() - offset)
        tomorrow.setDate(tomorrow.getDate() + 1);
        tomorrow.setHours(0, 0, 0, 0);

        if (selectedDate < tomorrow) {
            alert("오늘 이후의 날짜의 경기만 생성 가능합니다");
            $(this).val(tomorrow.toLocaleDateString('en-CA'));
        }
    });

    /** scroll **/
    let lastScrollTop = 0;
    let $bottomFollow = $("#bottomFollow");

    $(window).on("scroll touchmove", function () {
            setTimeout(function () {
                let currentScroll = document.documentElement.scrollTop || document.body.scrollTop;

                if (currentScroll < lastScrollTop) {
                    $bottomFollow.css({bottom: "0", opacity: "1"});
                } else if (currentScroll > lastScrollTop) {
                    $bottomFollow.css({bottom: "-100px", opacity: "0"});
                }

                lastScrollTop = currentScroll;
            }, 100);

        setTimeout(function () {
            let currentScroll = document.documentElement.scrollTop || document.body.scrollTop;

            if (currentScroll < lastScrollTop) {
                $bottomFollow.css({bottom: "0", opacity: "1"});
            } else if (currentScroll > lastScrollTop) {
                $bottomFollow.css({bottom: "-100px", opacity: "0"});
            }

        }, 1000);
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

function showTimePicker() {
    $('#time-picker').css('display', 'flex');
}

function setTime() {
    var $time = $('#time');
    var period = $('#period-list .select').attr('id');
    var hours = $('#hour-list .select').attr('id');
    var minutes = $('#minute-list .select').attr('id');
    
    if (period === 'am') {
        $time.val('오전 ' + hours + ":" + minutes);
    } else {
        $time.val('오후 ' + hours + ":" + minutes);
    }
}

function createMatching() {
    var name = $('#name')
    var playNum = $('input[name="play-num"]:checked');
    var maxNum = $('#max-num');
    var date = $('#date');
    var time = $('#time');
    var hour = $('#hour');
    var gameKind = $('input[name="game-kind"]:checked').val();
    var teamSelect = $('#team-select');
    var isOnlyWomen;
    var stadiumRegionSelect = $('input[name="stadium_region_select"]:checked').val();
    var stadiumLoadAddress = $('#stadium-load-address');
    var stadiumJibunAddress = $('#stadium-jibun-address');
    var detailAddress = $('#stadium-detail-address');
    var siDoString = $('#siDo').val();
    var siGunGuString = $('#siGunGu').val();

    if (!name.val()) {
        alert("경기명을 입력해주세요");
        name.focus();
        return;
    }
    if (!playNum.val()) {
        alert("경기 참여 인원을 입력해주세요");
        $('input[name="play-num"]').first().focus();
        return;
    }
    if (!maxNum.val()) {
        alert("최대 모집 인원을 입력해수제요");
        maxNum.focus();
        return;
    }
    if (parseInt(maxNum.val(), 10) < parseInt(playNum.val(), 10)) {
        alert("경기 참여 최대 인원이 경기 인원 보다 적을 수 없습니다");
        maxNum.focus();
        return;
    }

    if (!date.val()) {
        alert("경기 날짜를 입력해주세요.");
        date.focus();
        return;
    }
    if (!time.val()) {
        alert("경기 시간을 입력해주세요.");
        time.focus();
        return;
    }
    if (hour.val() < 1) {
        alert("경기 진행 시간은 최소 한시간입니다.");
        hour.focus();
        return;
    }

    if (gameKind === 'TEAM' && !teamSelect.val()) {
        alert("팀을 선택해주세요.");
        teamSelect.focus();
        return;
    }
    if($('#is-only-women').is(':checked')) {
        isOnlyWomen = "ONLY_WOMEN"
    } else {
        isOnlyWomen = "NOT_ONLY_WOMEN"
    }

    var regionString = "";
    if (stadiumRegionSelect === "stadium") {
        if (!stadiumLoadAddress.val()) {
            alert("경기장 주소를 입력해주세요.");
            stadiumLoadAddress.focus();
            return;
        }
        if (!detailAddress.val()) {
            detailAddress.focus();
            alert("상세 주소를 입력해주세요.");
            return;
        }
        if (!stadiumJibunAddress.val()) {
            alert("잘못된 접근. 고객센터에 문의하세요.");
            return;
        }
    }

    if (stadiumRegionSelect === "region") {
        stadiumLoadAddress.val("");
        stadiumJibunAddress.val("");
        detailAddress.val("");
        if (!siDoString || !siGunGuString) {
            alert("지역을 선택해주세요.");
            return;
        }
        regionString = siDoString + " " + siGunGuString;
    }

    var formData = {
        "name": name.val(),
        "playNum": playNum.val(),
        "maxNum": maxNum.val(),
        "startAt": date.val() + "T" + convertTime(time.val()),
        "hour": hour.val(),
        "gameKind": gameKind,
        "teamName": teamSelect.val(),
        "isOnlyWomen": isOnlyWomen,
        "stadiumLoadAddress": stadiumLoadAddress.val(),
        "stadiumJibunAddress": stadiumJibunAddress.val(),
        "detailAddress": detailAddress.val(),
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