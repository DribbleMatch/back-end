var interval;

$(document).ready(function () {
    $('.custom-radio-btn').on('click', function() {

        var name = $('#name');
        var email = $('#email');

        $('.custom-radio-btn').removeClass('active');
        $(this).addClass('active');

        if ($(".custom-radio-btn.active").attr("id") === 'email-radio') {
            name.show();
            email.hide();
        } else {
            name.hide();
            email.show();
        }
    });

    $('#findEmailForm').on("submit", function (event) {
        event.preventDefault();

        var $form = this;
        var activeRadio = $(".custom-radio-btn.active").attr("id");
        var name  = $('#name').val();
        var email  = $('#email').val();
        var phoneNum  = $('#phone-num').val();
        var pinNum = $('#pin-num').val();
        var completeBtn = $('#complete-btn');

        if (activeRadio === 'email-radio' && !name) {
            alert("이름을 입력해주세요");
            return;
        }

        if (activeRadio === 'password-radio' && !email) {
            alert("이메일을 입력해주세요");
            return;
        }

        if (!phoneNum) {
            alert("전화번호를 입력해주세요");
            return;
        }

        if (completeBtn.text() === '인증번호 전송') {
            sendPinNum();
            return;
        }

        if (!pinNum) {
            alert("인증번호를 입력해주세요");
            return;
        }

        $.ajax({
            url: '/api/signup/getAuth',
            type: 'POST',
            data: {
                phone: phoneNum,
                authCode: pinNum
            },
            success: function (response) {
                alert(response.data)

                if (response && activeRadio === 'email-radio') {
                    $form.action = '/page/login/findEmailResult';
                    $form.submit();
                } else if (response && activeRadio === 'password-radio') {
                    $form.action = '/page/login/resetPassword';
                    $form.submit();
                }
            },
            error: function(xhr, status, error) {
                commonErrorCallBack(xhr, status, error);
            }
        })
    })
})

function sendPinNum() {
    var phone = $('#phone-num').val();

    $.ajax({
        url: '/api/signup/sendAuthMessage',
        type: 'POST',
        data: { phone: phone},
            success: function (response) {
                if (response) {
                    alert(response.data);
                    $('#name').prop('readonly', true);
                    $('#phone-num').prop('readonly', true);
                    $('#pin-num-div').show();
                    $('#complete-btn').text('이메일 찾기');
                    start_timer(300);
                }
        },
        error: function(xhr, status, error) {
            commonErrorCallBack(xhr, status, error);
        }
    })
}

function start_timer(duration) {
    var timer = duration, minutes, seconds;

    interval = setInterval(function () {
        minutes = parseInt(timer / 60, 10);
        seconds = parseInt(timer % 60, 10);

        minutes = minutes < 10 ? "0" + minutes : minutes;
        seconds = seconds < 10 ? "0" + seconds : seconds;

        $('#timer').show();
        $('#timer').text(minutes + ":" + seconds);

        if (--timer < 0) {
            clearInterval(interval);
            $('#timer').hide();
            $('#pin-num-div').hide();
            $('#name').prop('readonly', false);
            $('#phone-num').prop('readonly', false);
            $('#complete-btn').text('인증번호 전송');
            alert('인증 시간이 만료되었습니다.');
        }
    }, 1000);
}