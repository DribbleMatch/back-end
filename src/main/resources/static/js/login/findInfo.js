var interval;

function checkFindEmailInput() {
    var birth  = $('#birth').val()
    var phoneNum  = $('#phone-num').val()

    if (!birth) {
        alert("생년월일을 입력해주세요");
        return false;
    }

    if (!/^\d{8}$/.test(birth)) {
        alert("생년월일 형식이 올바르지 않습니다(YYYYMMDD)");
        return false;
    }

    if (!phoneNum) {
        alert("전화번호를 입력해주세요");
        return false;
    }
}

function checkFindPasswordInput() {

    var email = $('#email').val();
    var phone = $('#phone').val();
    var authCode = $('#auth-code').val();

    if (!email) {
        alert("이메일을 입력해주세요.");
        return false;
    }

    if (!phone) {
        alert("전화번호를 입력해주세요.");
        return false;
    }

    if (!authCode) {
        alert("인증번호를 입력해주세요.");
        return false;
    }

    if ($('#auth-code').prop('readonly') === false) {
        alert("인증 확인을 완료해주세요.");
        return false;
    }
}

function requestAuth() {
    var phone = $('#phone').val();

    if (!phone) {
        alert("전화번호를 입력해주세요.");
        return;
    }

    $.ajax({
        url: '/api/signup/sendAuthMessage',
        type: 'POST',
        data: { phone: phone},
        success: function (response) {
            if (response) {
                $('#phone').prop('readonly', true);
                $('#send_message_btn').prop('disabled', true);
                $('#auth-code').prop('readonly', false);
                $('#auth-btn').prop('disabled', false);
                alert(response.data);
                start_timer(300);
            }
        },
        error: function(xhr, status, error) {
            commonErrorCallBack(xhr, status, error);
        }
    })
}

function checkAuth() {
    var phone = $('#phone').val();
    var authCode = $('#auth-code').val();

    if (!authCode) {
        alert("인증번호를 입력해주세요.");
        return;
    }

    $.ajax({
        url: '/api/signup/getAuth',
        type: 'POST',
        data: {
            phone: phone,
            authCode: authCode
        },
        success: function (response) {
            if (response) {
                $('#auth-code').prop('readonly', true);
                $('#auth-btn').prop('disabled', true).text('인증 완료');
                clearInterval(interval);
                $('#timer').hide();
                alert(response.data);
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
            $('#phone').prop('readonly', false);
            $('#send_message_btn').prop('disabled', false).text('인증 재요청');
            $('#auth-code').prop('readonly', true);
            $('#auth-btn').prop('disabled', true);
            alert('인증 시간이 만료되었습니다.');
        }
    }, 1000);
}

function changePassword() {
    var userId = $('#user-id').val();
    var password = $('#password').val();
    var rePassword = $('#re-password').val();

    if (!password) {
        alert("변경할 비밀번호를 입력해주세요");
        return;
    }

    if (!rePassword) {
        alert("비밀번호 확인을 입력해주세요");
        return;
    }

    if (password !== rePassword) {
        alert("비밀번호와 비밀번호 확인이 다릅니다");
        return;
    }

    var formData = {
        'userId': userId,
        'password': password,
        'rePassword': rePassword
    }

    $.ajax({
        url: '/api/login/changePassword',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(formData),
        success: function (response) {
            alert(response.data);
            location.href = "/page/login";
        },
        error: function(xhr, status, error) {
            commonErrorCallBack(xhr, status, error);
        }
    })
}