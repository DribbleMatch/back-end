$(document).ready(function() {
    // input 필드에서 Enter 키를 눌렀을 때 form이 제출되도록 설정
    $('#email, #password').keydown(function(event) {
        if (event.key === "Enter") {
            $('.login-btn').click(); // 로그인 버튼 클릭
        }
    });
});

function user_login() {
    var email = $('#email').val();
    var password = $('#password').val();

    if (!email) {
        alert("이메일을 입력하세요.");
        return;
    }

    if (!password) {
        alert("비밀번호를 입력하세요.");
        return;
    }

    var formData = {
        "email": email,
        "password": password
    }

    $.ajax({
        url: '/api/login',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(formData),
        success: function (response) {
            location.href = "/page";
        },
        error: function (xhr, status, error) {
            commonErrorCallBack(xhr, status, error);
        }
    })
}