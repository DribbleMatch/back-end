var interval;

function check_nickname_exist() {
    var nickname = $('#nickname').val();

    if (!nickname) {
        alert("닉네임을 입력해주세요.");
        return;
    }

    $.ajax({
        url: '/rest/signup/checkNickName',
        type: 'POST',
        data: { nickName: nickname},
        success: function (response) {
            if (response) {
                $('#nickname').prop('readonly', true);
                $('#check-nickname-btn').prop('disabled', true).text('사용 가능');
                alert(response.data);
            }
        },
        error: function(xhr, status, error) {
            var response = xhr.responseJSON;
            if (response.code === '1104') {
                alert(response.message);
            } else {
                alert('닉네임 중복 확인 오류. 잠시 후에 다시 시도하세요.');
            }
        }
    })
}

function check_email_exist() {
    var email = $('#email').val();

    if (!email) {
        alert("이메일을 입력해주세요.");
        return;
    }

    $.ajax({
        url: '/rest/signup/checkEmail',
        type: 'POST',
        data: { email: email},
        success: function (response) {
            if (response) {
                $('#email').prop('readonly', true);
                $('#check-email-btn').prop('disabled', true).text('사용 가능');
                alert(response.data);
            }
        },
        error: function(xhr, status, error) {
            var response = xhr.responseJSON;
            if (response.code === '1100') {
                alert(response.message);
            } else {
                alert('이메일 중복 확인 오류. 잠시 후에 다시 시도하세요.');
            }
        }
    })
}

function validate_birth() {
    var birth = $('#birth').val();

    if (/^\d{8}$/.test(birth)) {
        $('#fault_birth').hide();
    } else {
        // 8자리 숫자가 아닌 경우
        $('#fault_birth').show();
    }
}

function validate_password() {
    var password = $('#password').val();
    var passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;

    if (passwordPattern.test(password)) {
        $('#fault_password').hide();  // 유효한 경우 오류 메시지를 숨김
    } else {
        $('#fault_password').show();  // 유효하지 않은 경우 오류 메시지를 표시
    }
}

function validate_password_check() {
    var password = $('#password').val();
    var password_check = $('#password_check').val();

    if (password === password_check) {
        $('#fault_password_check').hide();
    } else {
        $('#fault_password_check').show();
    }
}

function send_verification_code() {
    var phone = $('#phone').val();

    if (!phone) {
        alert("전화번호를 입력해주세요.");
        return;
    }

    $.ajax({
        url: '/rest/signup/sendAuthMessage',
        type: 'POST',
        data: { phone: phone},
        success: function (response) {
            if (response) {
                $('#phone').prop('readonly', true);
                $('#send_message_btn').prop('disabled', true)
                alert(response.data);
                $('.info-container-hide').show();
                start_timer(300);
            }
        },
        error: function(error) {
                alert('인증번호 전송 실패. 잠시 후에 다시 시도하세요.');
        }
    })
}

function check_verification_code() {
    var phone = $('#phone').val();
    var auth_code = $('#pin_num').val();

    if (!auth_code) {
        alert("인증번호를 입력해주세요.");
        return;
    }

    $.ajax({
        url: '/rest/signup/getAuth',
        type: 'POST',
        data: {
            phone: phone,
            authCode: auth_code
        },
        success: function (response) {
            if (response) {
                $('#pin_num').prop('readonly', true);
                $('#get_auth_btn').prop('disabled', true).text('인증 완료');
                clearInterval(interval);
                $('#timer').hide();
                alert(response.data);
            }
        },
        error: function(xhr, status, error) {
            var response = xhr.responseJSON;
            if (response.code === '1200') {
                alert(response.message);
            } else {
                alert('인증코드 확인 오류. 잠시 후에 다시 시도하세요.');
            }
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

        $('#timer').text(minutes + ":" + seconds);

        if (--timer < 0) {
            clearInterval(interval);
            $('.info-container-hide').hide(); // 인증번호 입력 필드 숨김
            $('#phone').prop('readonly', false); // 전화번호 입력 다시 가능
            $('#send_message_btn').prop('disabled', false).text('인증 재요청');
            alert('인증 시간이 만료되었습니다.');
        }
    }, 1000);
}

function check_input() {
    var nickname = $('#nickname').val();
    var email = $('#email').val();
    var birth = $('#birth').val();
    var password = $('#password').val();
    var password_check = $('#password_check').val();
    var phone = $('#phone').val();
    var pin_num = $('#pin_num').val();

    if (!nickname) {
        alert("닉네임을 입력해주세요.");
        return false;
    }

    if (!$('#nickname').prop('readonly')) {
        alert("닉네임 중복 확인을 해주세요.");
        return false;
    }

    if (!email) {
        alert("이메일을 입력해주세요.");
        return false;
    }

    if (!$('#email').prop('readonly')) {
        alert("이메일 중복 확인을 해주세요.");
        return false;
    }

    if (!birth) {
        alert("생년월일을 입력해주세요.");
        return false;
    }

    if ($('#fault_birth').is(':visible')) {
        alert("생년월일 형식이 올바르지 않습니다.");
        return false;
    }

    if (!password) {
        alert("비밀번호를 입력해주세요.");
        return false;
    }

    if ($('#fault_password').is(':visible')) {
        alert("비밀번호 형식이 올바르지 않습니다.");
        return false;
    }

    if (!password_check) {
        alert("비밀번호 확인을 입력해주세요.");
        return false;
    }

    if (password !== password_check) {
        alert("비밀번호가 일치하지 않습니다.");
        return false;
    }

    if (!phone) {
        alert("휴대폰 번호를 입력해주세요.");
        return false;
    }

    if (!$('#phone').prop('readonly')) {
        alert("휴대폰 인증을 해주세요.");
        return false;
    }

    if (!pin_num) {
        alert("인증번호를 입력해주세요.");
        return false;
    }

    if ($('#pin_num').prop('readonly') === false) {
        alert("인증 확인을 완료해주세요.");
        return false;
    }
}