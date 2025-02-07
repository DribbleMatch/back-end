$(document).ready(function() {
    $('#email').focus();
    $('#loginForm').submit(function(event) {
        event.preventDefault();

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
    });
});